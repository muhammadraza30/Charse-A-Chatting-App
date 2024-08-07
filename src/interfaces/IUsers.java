package interfaces;

import java.util.ArrayList;
import main.User;

public interface IUsers {

    //This method will create a file for user's data
    void createUserFile();

    //This method will save user's data in file 
    void saveUserData(ArrayList<User> users);

    //This method will get user's data from file 
    ArrayList<User> getUsers();

    //This method will delete user's data from file 
    void deleteUser(String userName);
}
