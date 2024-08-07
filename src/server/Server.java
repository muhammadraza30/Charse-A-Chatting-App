package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void start() throws IOException {
        // Create a new server that listens on port 5555
        ServerSocket ss = new ServerSocket(5555);
        System.out.println("Listening...");

        while (true) {
            // Listen and accept a connection
            Socket client = ss.accept();
            String username = new DataInputStream(client.getInputStream()).readUTF();
            System.out.println(username + " connected");

            // Create a new thread for each client that connects to our server so
            // that we can handle incoming messages from that client without blocking
            // our server
           
            new Thread(new MessageHandler(client, username)).start();
        }
    }
    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
