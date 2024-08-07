package client;

import java.io.*;
import java.net.*;

/*

    This class is responsible for connecting to server
    and sending and receving messages to and from server

*/

public class Client {
    private Socket socket;
    private DataOutputStream dout;
    private static Client instance;
    private static String ipAddress; 

    public Client() {
        ipAddress = IPAddress.ipAdd;
    }
    
    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        
        return instance;
    }    

    public Socket connect(String username) throws IOException  {
        this.socket = new Socket(ipAddress, 5555);
        this.sendMessage(username);
        return this.socket;
    }

    public void sendMessage(String message) throws IOException {
        this.dout = new DataOutputStream(this.socket.getOutputStream());
        dout.writeUTF(message);
        dout.flush();
    }
    
    public boolean isConnected() {
        return this.socket.isConnected();
    }
    
    public void disconenct() throws IOException {
        this.socket.close();
    }

    public Socket getSocket() {
        return socket;
    }
}
