package queue;

import client.PanelLoginAndRegister;
import interfaces.IMessage;
import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MessageFileHandling implements IMessage {

    private File file;
    private String defaultPath;
    private String messageFilePath;

    public MessageFileHandling() {
        defaultPath = String.valueOf(Paths.get("src", "data"));
        messageFilePath = defaultPath + File.separator + "message.txt";
    }

    private static ArrayList<Message> msg = new ArrayList<>();

    @Override
    public void createMessageFile() {
        try {
            file = new File(messageFilePath);
//            System.out.println(messageFilePath);
            if (!file.exists()) {
                String result = file.createNewFile() ? "File Created" : "Issue";
                System.out.println(result);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveMessageData(ArrayList<Message> msgs) {
        createMessageFile();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(messageFilePath))) {
            out.writeObject(msgs);
            System.out.println("Object added");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Message> getMessage() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(messageFilePath))) {
            Object obj = in.readObject();
            ArrayList<Message> users = (ArrayList<Message>) obj;
            Collections.reverse(users);
//            System.out.println("Object rec" + users);

            return users;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

//    @Override
//    public void deleteUser(String userName) {
//        ArrayList<Message> messages = getMessage();
//        messages.removeIf(message -> message.getSenderUsername().equals(userName) || message.getReceiverUsername().equals(userName));
//        saveMessageData(messages);
//    }
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");

    public void storeMessageInFile(String fileName, String senderUsername, String message) throws IOException {
        Message msg1 = new Message(senderUsername, message);

        File prifile = new File(fileName);
        if (!prifile.exists()) {
            prifile.createNewFile();
        }

        ArrayList<Message> messages = new ArrayList<>();
        if (prifile.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(prifile))) {
                Object obj = in.readObject();
                messages = (ArrayList<Message>) obj;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        messages.add(msg1);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(prifile))) {
            out.writeObject(messages);
            System.out.println("Object added");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void storeMessages(String senderUsername, String message) throws IOException {
        String fileName = new MessageFileHandling().getFileName(PanelLoginAndRegister.userN, senderUsername);
        new MessageFileHandling().storeMessageInFile(fileName, PanelLoginAndRegister.userN, message);
    }

    public ArrayList<Message> getMessages(String senderUsername, String receiverUsername) {
        String fileName = getFileName(senderUsername, receiverUsername);
        File prifile = new File(fileName);
        ArrayList<Message> messages = new ArrayList<>();
        if (prifile.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(prifile))) {
                Object obj = in.readObject();
                messages = (ArrayList<Message>) obj;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (messages.size() == 1) {
            return new ArrayList<>(Collections.singletonList(messages.get(0)));
        } else {
            return messages;
        }
    }

    public String getFileName(String user1, String user2) {
        String[] usernames = new String[]{user1, user2};
        Arrays.sort(usernames);
        return defaultPath + File.separator + usernames[0] + "_" + usernames[1] + ".txt";
    }
}
