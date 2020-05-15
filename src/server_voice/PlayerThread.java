package server_voice;

import javax.sound.sampled.SourceDataLine;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class PlayerThread extends Thread {
    public DatagramSocket socketIn;
    public SourceDataLine audioOut;
    byte[] data = new byte[512];

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(data, data.length);
        while (Main.calling) {
            try {
                socketIn.receive(packet);

                data = packet.getData();
                audioOut.write(data, 0, data.length);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        audioOut.close();
        audioOut.drain();
        System.out.println("stopped");
    }
}
