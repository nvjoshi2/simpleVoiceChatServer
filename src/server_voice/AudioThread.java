package server_voice;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AudioThread extends Thread{
    private DatagramSocket socketIn;
    byte[] audioData = new byte[512];


    public AudioThread(DatagramSocket setSocketIn) {
        this.socketIn = setSocketIn;
    }
    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(audioData, audioData.length);
        while (Main.calling) {
            try {
                socketIn.receive(packet);
                audioData = packet.getData();

                // send audio to all clients


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
