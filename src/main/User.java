package main;

import java.io.Serializable;
//We use Serializable because it encryptes the data in txt file
public class User implements Serializable{
    
    private String userName;
    private String password;
    private String fullName;
    private String imagePath;
    private String email;
    
    //User Constructor
    public User(String userName, String password, String fullName,  String email,String imagePath) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.imagePath = imagePath;
        this.email = email;
    }
    //User Constructor (OVERLOADING)
    public User(){
        
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName) {
        this.fullName = firstName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    
    @Override
    public String toString() {
        return "{ " + "userName = " + userName + "  |  password = " + password + "  |  firstName = " + fullName +"  |  email = " + email + " }";
    }
    
    
}
