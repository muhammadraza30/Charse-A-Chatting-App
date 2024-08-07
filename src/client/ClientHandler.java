package client;

import custom_components.ChatArea;
import custom_components.ChatBox;
import custom_components.ModelMessage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import main.User;
import main.UserFileHandling;
import queue.Message;
import queue.MessageFileHandling;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private DataInputStream din;
    private final ChatArea chatArea;
    String fileName;
    String defaultPath = String.valueOf(Paths.get("src", "data"));
    Icon icon;
    String image;
    ArrayList<Message> messages;

    public ClientHandler(Socket s, ChatArea chatArea) {
        this.socket = s;
        this.din = null;
        this.chatArea = chatArea;
    }

    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");

    public void receiveMessageFromServer() throws IOException {
        this.din = new DataInputStream(this.socket.getInputStream());

        while (true) {
            if (this.din.available() > 0) {
                String input = this.din.readUTF();
                String[] parts = input.split(":");
                String senderUsername = parts[0];
                String message = parts[1];

//                new MessageFileHandling().storeMessages( this.chatArea.loggedInUser, message);
                UserFileHandling h = new UserFileHandling();
                ArrayList<User> arrlist = h.getUsers();

                for (int i = 0; i < arrlist.size(); i++) {
                    if (arrlist.get(i).getUserName().equals(senderUsername)) {
                        image = arrlist.get(i).getImagePath();
                        break;
                    }
                }
                // Display the message in the chat area
                icon = new ImageIcon(getClass().getResource(image));

                String date = df.format(new Date());
                chatArea.addChatBox(new ModelMessage(icon, senderUsername, date, message), ChatBox.BoxType.LEFT);
            }
        }
    }

    public void retrieveMessages(String senderUsername) throws IOException {
        String fileName = new MessageFileHandling().getFileName(this.chatArea.loggedInUser, senderUsername);
        retrieveMessagesFromFile(fileName);
    }

   private void retrieveMessagesFromFile(String fileName) throws IOException {
    File file = new File(fileName);
    if (file.exists()) {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        try {
            messages = (ArrayList<Message>) ois.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        ois.close();

        for (Message message : messages) {
            String senderUsername = message.getSenderUsername();
            String messageText = message.getMessages();
            String date = df.format(message.getTimestamp());

            UserFileHandling h = new UserFileHandling();
            ArrayList<User> arrlist = h.getUsers();

            for (int i = 0; i < arrlist.size(); i++) {
                if (arrlist.get(i).getUserName().equals(senderUsername)) {
                    image = arrlist.get(i).getImagePath();
                    break;
                }
            }

            Icon icon = new ImageIcon(getClass().getResource(image));
            chatArea.addChatBox(new ModelMessage(icon, senderUsername, date, messageText), ChatBox.BoxType.LEFT);
        }
    }
}
  

    @Override
    public void run() {
        try {
            this.receiveMessageFromServer();
        } catch (IOException ex) {
            System.out.println(PanelLoginAndRegister.userN + " is Disconnected" );
        }
    }
}
