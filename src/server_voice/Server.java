package server_voice;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
    public int audioPort = 8000;
    public int msgPort = 6000;
    public DatagramSocket audioSocket;
    public DatagramSocket msgSocket;
    public SourceDataLine audioOut;
    private int clientId;

    private boolean isRunning = true;

    private ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();

    public Server(int setAudioPort, int setMsgPort) {
        try {
            this.audioPort = setAudioPort;
            this.msgPort = setMsgPort;
            audioSocket = new DatagramSocket(setAudioPort);
            msgSocket = new DatagramSocket(setMsgPort);
            System.out.println("Server started on Ports " + setAudioPort + "(audio), " + setMsgPort + "(messages)");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 16;
        int channel = 2;
        boolean isSigned = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channel, isSigned, bigEndian);

    }

    private void broadcast(byte[] audioData) {
        for (ClientInfo info : clients) {
            send(audioData, info.getAddress(), info.getPort());
        }
    }

    private void send(byte[] audioData, InetAddress address, int port) {
        try {
//            message +=  "\\e";
//            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(audioData, audioData.length, address, port);
            audioSocket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void initAudio() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Not supported");
                System.exit(0);
            }

            audioOut = (SourceDataLine) AudioSystem.getLine(info);
            audioOut.open(format);
            audioOut.start();

            PlayerThread p = new PlayerThread();
            p.socketIn = new DatagramSocket(audioPort);
            p.audioOut = audioOut;
            Main.calling = true;
            p.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // listens on socket
    public void listenAudioData() {
        Thread audioThread = new Thread("Thread for recieveing and transmitting audio data") {
            public void run() {
                byte[] audioData = new byte[512];
                DatagramPacket packet = new DatagramPacket(audioData, audioData.length);
                while (isRunning) {
                    try {

                        audioSocket.receive(packet);
                        audioData = packet.getData();
                        broadcast(audioData);
                    } catch (Exception e) {
                        e.printStackTrace();
//                        return;
                    }

                }
            }
        };
        audioThread.start();


    }
    /*
    SERVER COMMAND LIST
    \con:[name] -> connects client to server
    \dis:[id] -> disconnect client from server
     */
    //START HERE, make new
    public void listenMsgData() {
        //threads can be run concurently while the program is running
        Thread msgThread = new Thread("Chat Program Listener") {
            public void run() {
                byte[] data = new byte[1024];

                //writes
                DatagramPacket packet = new DatagramPacket(data, data.length);
                try {
                    while (isRunning) {


                        System.out.println("reached line 107");
                        msgSocket.receive(packet); //this line hangs until it recieves a message. listens on socket and puts message in packet

                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e"));
                        System.out.println("messaged recieved: " + message);
                        //Manage message

                        if (message.startsWith("\\con:")) {
                            String name = message.substring((message.indexOf(":") + 1));
                            clients.add(new ClientInfo(name, clientId++, packet.getAddress(), packet.getPort()));
                            System.out.println(name + " connected");
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        msgThread.start();
    }

    public void stop() {
        isRunning = false;
    }
}
