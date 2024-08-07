package queue;

import client.PanelLoginAndRegister;
import java.util.ArrayList;

public class Queue {

    private ArrayList<Message> messages;
    private int front, rear;
     MessageFileHandling messageFileHandling = new MessageFileHandling();

    public Queue() {
        this.messages = messageFileHandling.getMessage();
        this.front = -1;
        this.rear = -1;
    }

    public boolean isEmpty() {
        return front == -1 && rear == -1;
    }

    public void enQueue(String message) {
       
        if (isEmpty()) {
            front = 0;
            rear = 0;
        } else {
            rear++;
        }
        messages.add(rear, new Message(PanelLoginAndRegister.userN,message)); // Assuming Message has a constructor that takes a String
        messageFileHandling.saveMessageData(messages);
        messageFileHandling.getMessage();
        
        
    }

    public void deQueue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
        } else {
            messages.remove(front);
            front++;
            if (front > rear) {
                front = -1;
                rear = -1;
            }
        }
    }
   
    public static void main(String[] args) {
        
        MessageFileHandling messageFileHandling = new MessageFileHandling();
        messageFileHandling.getMessage();
    }
            
            

}
