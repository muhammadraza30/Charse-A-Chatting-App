package client;

import custom_components.Button;
import custom_components.ImageAvatar;
import custom_components.MyPasswordField;
import custom_components.MyTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import main.EmailSender;
import main.User;
import main.UserFileHandling;
import net.miginfocom.swing.MigLayout;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    private final Client client;
    private final MyTextField userName = new MyTextField();
    private final MyPasswordField password = new MyPasswordField();
    private final MyTextField fullnameInput = new MyTextField();
    private final MyTextField emailInput = new MyTextField();
    private final MyTextField usernameInput = new MyTextField();
    private final MyPasswordField passwordInput = new MyPasswordField();
    private final MyPasswordField confirmPasswordInput = new MyPasswordField();

    Button imageButton = new Button();
    Button signupButton = new Button();
    Button loginButton = new Button();
    Button button1 = new Button();
    Button showPasswordButton = new Button();
    Button showConfirmPasswordButton = new Button();
    ImageAvatar user_photo = new ImageAvatar();
    private Boolean passwordVisible = false;
    public static String loggedInUserFullName;
    public static String userN;
    //PanelLoginAndRegister Constructor
    private ArrayList<User> users = new ArrayList<>();
    private static final String FILE_NAME = "users_Ser";
    private static final String IMAGE_DIR = "user_Images";
    private String selectedImagePath;
    private static String fileNameOnly;
    private static boolean found = false;

    public PanelLoginAndRegister(boolean value) {
        if (value == false) {
            setVisible(false);
        }
        this.client = Client.getInstance();
    }

    //PanelLoginAndRegister Constructor OVERLOADING
    public PanelLoginAndRegister() {
        initComponents();
        Register();
        Login();
        login.setVisible(false);
        register.setVisible(true);
        this.client = Client.getInstance();
    }

    //Register Designing Function
    private void Register() {

        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]push"));

        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(0, 64, 23));

        fullnameInput.setPrefixIcon(new ImageIcon(getClass().getResource("/Images/user.png")));
        fullnameInput.setHint("Full Name");
        fullnameInput.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    emailInput.requestFocus();
                }
            }
        });

        emailInput.setPrefixIcon(new ImageIcon(getClass().getResource("/Images/mail.png")));
        emailInput.setHint("Email");
        emailInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    usernameInput.requestFocus();
                }
            }
        });

        usernameInput.setPrefixIcon(new ImageIcon(getClass().getResource("/Images/user.png")));
        usernameInput.setHint("Username");
        usernameInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordInput.requestFocus();
                }
            }
        });

        passwordInput.setPrefixIcon(new ImageIcon(getClass().getResource("/Images/pass.png")));
        passwordInput.setHint("Password");
        passwordInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmPasswordInput.requestFocus();
                }
            }
        });

        showPasswordButton.setText("Show Password");
        showPasswordButton.setFont(new Font("sansserif", 1, 12));
        showPasswordButton.setForeground(new Color(65, 62, 62));
        showPasswordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hide.png")));
        showPasswordButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        showPasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                togglePasswordVisibility(passwordInput, showPasswordButton);
                passwordInput.requestFocus();
            }
        });
        showPasswordButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    togglePasswordVisibility(passwordInput, showPasswordButton);
                }
            }
        });

        confirmPasswordInput.setPrefixIcon(new ImageIcon(getClass().getResource("/Images/pass.png")));
        confirmPasswordInput.setHint("Confirm Password");
        confirmPasswordInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    imageButton.requestFocus();
                }
            }
        });

        showConfirmPasswordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hide.png")));
        showConfirmPasswordButton.setText("Show Confirm Password");
        showConfirmPasswordButton.setFont(new Font("sansserif", 1, 12));
        showConfirmPasswordButton.setForeground(new Color(65, 62, 62));
        showConfirmPasswordButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        showConfirmPasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                togglePasswordVisibility(confirmPasswordInput, showConfirmPasswordButton);
                confirmPasswordInput.requestFocus();
            }
        });
        showConfirmPasswordButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    togglePasswordVisibility(confirmPasswordInput, showConfirmPasswordButton);
                }
            }
        });
        user_photo.setPreferredSize(new Dimension(75, 75));
        imageButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/upload.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        imageButton.setBackground(new Color(230, 230, 230));
        imageButton.setForeground(new Color(65, 62, 62));
        imageButton.setText("Upload Image");
        imageButton.setFont(new Font("sansserif", 1, 12));
        imageButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        imageButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            upload();
        });
        imageButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    upload();
                }
            }
        });
        signupButton.setBackground(new Color(0, 64, 23));
        signupButton.setForeground(new Color(250, 250, 250));
        signupButton.setText("SIGN UP");

        // Added click listener and key listener on "SIGNUP" button.
        signupButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            signUp();
        });
        signupButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

                    signUp();

                }
            }
        });

        // Adding components to register page
        register.add(label);
        register.add(fullnameInput, "w 60%");
        register.add(emailInput, "w 60%");
        register.add(usernameInput, "w 60%");
        register.add(passwordInput, "w 60%");
        register.add(showPasswordButton, "w 40, h 10");
        register.add(confirmPasswordInput, "w 60%");
        register.add(showConfirmPasswordButton, "w 40, h 10");
        register.add(user_photo);
        register.add(imageButton, "w 40%, h 40");
        register.add(signupButton, "w 40%, h 40");
    }

    public void upload() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        if (f != null) {
            selectedImagePath = f.getAbsolutePath();
        }

        try {
            BufferedImage bi = ImageIO.read(new File(selectedImagePath));
            Image img = bi.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(img);
            user_photo.setImage(icon);
        } catch (IOException ex) {
            Logger.getLogger(PanelLoginAndRegister.class.getName()).log(Level.SEVERE, null, ex);
        }

        File defaultPath = new File("src/images"); // Specify the default path
        String newImagePath = saveImage(selectedImagePath, defaultPath.getAbsolutePath(), f.getName());
        System.out.println("Image saved to: " + newImagePath);
    }

    private static String saveImage(String sourceImagePath, String destinationFolderPath, String fileName) {
        try {
            File sourceFile = new File(sourceImagePath);
            File destinationFolder = new File(destinationFolderPath);

            // Create the destination folder if it doesn't exist
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            String newImagePath = destinationFolderPath + File.separator + fileName;
            Files.copy(sourceFile.toPath(), Paths.get(newImagePath));
            Path path = Paths.get(sourceImagePath);
            fileNameOnly = path.getFileName().toString();
//        return fileNameOnly;
            return newImagePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return ""; // No extension found
        }
        return fileName.substring(lastIndexOfDot);
    }

    //This function is called in Main Function for showing or visible the login or register form => 
    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    //For visible password
    private void togglePasswordVisibility(MyPasswordField password1, Button button4) {
        passwordVisible = !passwordVisible;
        char echoChar = passwordVisible ? 0 : '*';
        password1.setEchoChar(echoChar);
        if (passwordVisible == true) {
            button4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/unhide.png")));
            button4.setForeground(new Color(255, 51, 51));// NOI18N
        } else {
            button4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hide.png")));
            button4.setForeground(new Color(65, 62, 62));
        }
    }

    // Validations login by default false
    public boolean authenticateUser(String userName, String password) {
        UserFileHandling h = new UserFileHandling();
        boolean value = false;
        for (User user : h.getUsers()) {
            if ((user.getUserName().equals(userName) || user.getEmail().equals(userName))
                    && user.getPassword().equals(password)) {
                loggedInUserFullName = user.getFullName();
                userN = user.getUserName();
                value = true;
            }
        }
        return value;
    }

    //Login Designing Function
    private void Login() {

        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));

        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(0, 64, 23));
        login.add(label);

        userName.setPrefixIcon(new ImageIcon(getClass().getResource("/Images/mail.png")));
        userName.setHint("Email / Username");
        userName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    password.requestFocus();
                }
            }
        });
        login.add(userName, "w 60%");

        password.setPrefixIcon(new ImageIcon(getClass().getResource("/Images/pass.png")));
        password.setHint("Password");
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    signIn();
                }
            }
        });
        login.add(password, "w 60%");

        button1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hide.png")));
        button1.setText("Show Password");
        button1.setFont(new Font("sansserif", 1, 12));
        button1.setForeground(new Color(100, 100, 100));
        button1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                togglePasswordVisibility(password, button1);
                password.requestFocus();
            }
        });
        button1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    togglePasswordVisibility(password, button1);
                }
            }
        });
        login.add(button1, "w 40, h 10");

        loginButton.setBackground(new Color(0, 64, 23));
        loginButton.setForeground(new Color(250, 250, 250));
        loginButton.setText("SIGN IN");
        loginButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    signIn();
                }
            }
        });
        login.add(loginButton, "w 40%, h 40");

        loginButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            signIn();
        });
    }

    //sign in function(validation)
    private void signIn() {
        String username = userName.getText();
        String pass = new String(password.getPassword());

        if (authenticateUser(username, pass)) {
            try {
                // Connect
                this.client.connect(username);
                EmailSender.sendEmail("User LogIn", username + " is Logged In");
                new Menu().setVisible(true);
                userName.setText("");
                password.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(passwordInput, "Unable to connect to server", "Connection failed", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Login Unsuccesful!", "Login Response", JOptionPane.ERROR_MESSAGE);
            userName.setText("");
            password.setText("");
        }
    }

    //ALL Validations for Sign UP / Sign Up Form 
    public void signUp() {
        //using regex .matches ^[a-zA-Z]*$ for not getting symbols and numbers in first and last name
//        String userN = txtUser.getText();
        if (fullnameInput.getText().length() < 30 && fullnameInput.getText().matches("^[a-zA-Z\\s]+$")
                && emailInput.getText().contains("@") && emailInput.getText().contains(".com") && usernameInput.getText().length() < 12 && passwordInput.getText().length() < 12 && passwordInput.getText().equals(confirmPasswordInput.getText()) && (selectedImagePath != null || !selectedImagePath.isEmpty())) {
            UserFileHandling h = new UserFileHandling();

            for (User user1 : h.getUsers()) {
                if (user1.getUserName().equals(usernameInput.getText()) && user1.getEmail().equals(emailInput.getText())) {
                    JOptionPane.showMessageDialog(this, "Username and Email Already Taken!!!", "Already Taken", 1);
                    usernameInput.setText("");
                    emailInput.setText("");
                    emailInput.requestFocus();
                    found = true;
                    break;
                } else if (user1.getUserName().equals(usernameInput.getText())) {
                    JOptionPane.showMessageDialog(this, "Username Already Taken!!!", "Already Taken", 1);
                    usernameInput.setText("");
                    usernameInput.requestFocus();
                    found = true;
                    break;
                } else if (user1.getEmail().equals(emailInput.getText())) {
                    JOptionPane.showMessageDialog(this, "Email Already Taken!!!", "Already Taken", 1);
                    emailInput.setText("");
                    emailInput.requestFocus();
                    found = true;
                    break;
                }
            }
            if (found == false) {
                UserFileHandling userdata = new UserFileHandling();
                User user = new User(usernameInput.getText(), passwordInput.getText(), fullnameInput.getText(), emailInput.getText(), "/images/" + fileNameOnly);
                ArrayList<User> data = userdata.getUsers();
                data.add(user);
                userdata.saveUserData(data);
                ImageIcon icon = new ImageIcon("src/Images/done.png");
                JOptionPane.showMessageDialog(null, "Account Successfully Created",
                        "Response Message", JOptionPane.INFORMATION_MESSAGE, icon);
                fullnameInput.setText("");
                emailInput.setText("");
                usernameInput.setText("");
                passwordInput.setText("");
                confirmPasswordInput.setText("");
                user_photo.setImage(null);
            }
        } else {
            //If all the Text Fields are empty then this runs =>
            if (fullnameInput.getText().isBlank() || emailInput.getText().isBlank() || usernameInput.getText().isBlank() || passwordInput.getText().isBlank() || confirmPasswordInput.getText().isBlank() || fullnameInput.getText().isEmpty() || emailInput.getText().isEmpty() || usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty() || confirmPasswordInput.getText().isEmpty() || selectedImagePath == null || selectedImagePath.isEmpty()) {
                JOptionPane.showMessageDialog(null, "ERROR! The given Fields are empty", "SignUp Response", 0);
            } //If the User Put wrong Commands this will run =>
            else {

                if (fullnameInput.getText().length() >= 30) {
                    JOptionPane.showMessageDialog(null, "First name can't be too long", "SignUp Response", 0);
                    fullnameInput.setText("");
                }

                JOptionPane.showMessageDialog(null, "First name can't contain numbers and symbols", "SignUp Response", 0);
                if (!(fullnameInput.getText().matches("^[a-zA-Z\\s]+$"))) {
                    fullnameInput.setText("");
                }

                //Email
                if (!(emailInput.getText().contains("@")) || !((emailInput.getText().contains(".com")))) {
                    JOptionPane.showMessageDialog(null, "Email must contain @ and .com", "SignUp Response", 0);
                    emailInput.setText("");
                }

                //Username
                if (usernameInput.getText().length() >= 12) {
                    JOptionPane.showMessageDialog(null, "Username must be of 12", "SignUp Response", 0);
                    usernameInput.setText("");
                }

                //Password
                if (passwordInput.getText().length() >= 12) {
                    JOptionPane.showMessageDialog(null, "Password length must be 12 characters", "SignUp Response", 0);
                    passwordInput.setText("");
                }
                //Confirm Password
                if (!(passwordInput.getText().equals(confirmPasswordInput.getText()))) {
                    JOptionPane.showMessageDialog(null, "Password and Confirm Password must be Same", "SignUp Response", 0);
                    confirmPasswordInput.setText("");
                }
                if (selectedImagePath == null || selectedImagePath.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Image is not Uploaded", "SignUp Response", 0);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables

}
