package server;

import client.ClientStatus;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import main.User;
import main.UserFileHandling;

/**
 *
 * This class is responsible for receiving messages from clients and
 * broadcasting them to all the other clients
 *
 */
public class MessageHandler implements Runnable {

    /**
     * The "volatile" keyword is used to share data between threads. In our case
     * we are sharing this ConcurretHashMap(CHM) of sockets.
     *
     * ConcurrentHashMap provides thread-safety, that's why it is used instead
     * of ArrayList that does not provide thread-safety.
     *
     * ConcurrentHashMap works like key:value pairs, here key is the username of
     * the client and value is the actual socket instance.
     */
    volatile static ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap();

    // The "final" keyword makes a variable constant, it protects variables from 
    // modifications (don't know why I didn't use "const" keyword here, need to
    // do a bit more research)
    private final Socket socket;
    private final String username;

    public MessageHandler(Socket s, String username) throws IOException {
        this.socket = s;
        this.username = username;

        /**
         * Usernames should be unique because ConcurrentHashMap overwrites the
         * values with same keys, meaning if two clients with same username
         * connects, then only the last one to connect will receive broadcast
         * messages and the first one will not receive any message.
         */
        clients.put(username, s );
    }
public static ArrayList<ClientStatus> getAllClients(String currentUsername) {
    ArrayList<User> users = new UserFileHandling().getUsers();
    ArrayList<ClientStatus> allClients = new ArrayList<>();

    for (User user : users) {
        boolean isConnected = false;
        for (Map.Entry<String, Socket> entry : MessageHandler.clients.entrySet()) {
            if (entry.getKey().equals(user.getUserName())) {
                isConnected = true;
                break;
            }
        }

        if (!user.getUserName().equals(currentUsername)) { // exclude current user
            String status = isConnected ? "online" : "offline";
            allClients.add(new ClientStatus(user, status));
        }
    }

    return allClients;
}
    @Override
    public void run() {
        try {
            this.receiveMessageFromClient();
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    /**
     *
     * Check if there is some data available in socket's input stream. If data
     * is present, then broadcast it otherwise do nothing.
     *
     */
    private void receiveMessageFromClient() throws IOException {
        DataInputStream inputStream = new DataInputStream(this.socket.getInputStream());
        while (true) {
            if (inputStream.available() > 0) {
                this.broadcastMessage(inputStream.readUTF());
            }
        }
    }
    
    private void broadcastMessage(String message) {
        System.out.println("Broadcasting...");
        for (Map.Entry<String, Socket> entry : clients.entrySet()) {
            Socket client = entry.getValue();
            String receiverUsername = entry.getKey();
            
            if (receiverUsername.equals(this.username))
                continue;

            if (client.isConnected()) {
                try {
                    DataOutputStream dout = new DataOutputStream(client.getOutputStream());
                    dout.writeUTF(this.username + ": " + message);
                    dout.flush();
                } catch (IOException ex) {
                    System.out.println("A Client disconnected");
                    // Remove the client from CHM
                    clients.remove(entry.getKey());
                    System.out.println("Total clients: " + clients.size());
                }
            }
        }
    }
    
}
