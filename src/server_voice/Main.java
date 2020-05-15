package server_voice;

public class Main {
    public static boolean calling = true;
    public static void main(String[] args) {
        // write your code here
        Server server = new Server();
        server.initAudio();
    }
}
