package server_voice;

public class Main {
    public static boolean calling = true;
    public static void main(String[] args) {
        // write your code here
        Server server = new Server(8000, 6000);
        server.listenMsgData();
        server.listenAudioData();
//        server.initAudio();

    }
}
