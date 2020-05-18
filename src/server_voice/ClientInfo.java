package server_voice;

import java.net.InetAddress;

public class ClientInfo {

    private String name;
    private int id;
    private InetAddress address;
    private int port;


    public ClientInfo(String setName, int setId, InetAddress setAddress, int setPort) {
        this.name = setName;
        this.id = setId;
        this.address = setAddress;
        this.port = setPort;

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
