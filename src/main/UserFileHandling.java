package main;

import interfaces.IUsers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class UserFileHandling implements IUsers {

    //Creating File obj
    File file;
    String defaultPath;
    String userFilePath;

   //UserFileHandling Constructor
    public UserFileHandling() {
        defaultPath = String.valueOf(Paths.get("src", "data"));
        userFilePath = defaultPath + File.separator + "user.txt";
    }
    //Creating ArrayList<User> obj
    public static ArrayList<User> users = new ArrayList<>();
    
    //Function createUserFile 
    @Override
    public void createUserFile() {
        try {
            
            file = new File(userFilePath);
            //If file don't exist then it will create file
            if (!file.exists()) {
                String result = file.createNewFile() ? "File Created" : "Issue";
                System.out.println(result);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
     //Saving data in txt file of User and getting an ArrayList<User>
    @Override
    public void saveUserData(ArrayList<User> users) {
        //Call the createUserFile function which storing data in the file
        createUserFile();
        
        // ObjectOutputStream is to write data in file
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userFilePath))) {
            out.writeObject(users);
            System.out.println("Object added");
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    
    
     //Getting data from txt file of User and use it anywhere
    @Override
public ArrayList<User> getUsers() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(userFilePath))) {
        Object obj = in.readObject();
        ArrayList<User> users = (ArrayList<User>) obj;
        System.out.println("Object rec");
        return users;
    } catch (ClassNotFoundException e) {
        System.out.println("Class not found: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(e);
    }
    return new ArrayList<User>();
}

    //This function is used for deleteUser
    @Override
    public void deleteUser(String userName) {
        //Call the getUser function which gets the data from the txt file
        users = getUsers();
        
        //Iterator is used for catching the item from Array and do the func u want to do =>
        Iterator<User> iterator = users.iterator();
        //iterator.hasNext() check if the Array have another item or not 
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUserName().equalsIgnoreCase(userName)) {
                iterator.remove();
            }
        }

        saveUserData(users);

    }
    

}
