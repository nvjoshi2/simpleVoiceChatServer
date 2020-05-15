package server_voice;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.net.DatagramSocket;

public class Server {
    public int port = 8000;

    public SourceDataLine audioOut;

    public static AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 16;
        int channel = 2;
        boolean isSigned = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channel, isSigned, bigEndian);

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
            p.socketIn = new DatagramSocket(port);
            p.audioOut = audioOut;
            Main.calling = true;
            p.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
