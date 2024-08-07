package main;

import client.IPAddress;
import java.io.IOException;
import server.Server;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         
       
            IPAddress userip = new IPAddress();
            userip.setVisible(true);

 
        Server server = new Server();
        try {
            server.start();
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        
    }

}
