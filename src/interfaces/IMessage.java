package interfaces;


import java.util.ArrayList;
import queue.Message;

public interface IMessage {

    //This method will create a file for user's data
    void createMessageFile();

    //This method will save user's data in file 
    void saveMessageData(ArrayList<Message> msgs);

    //This method will get user's data from file 
    ArrayList<Message> getMessage();

    
}
