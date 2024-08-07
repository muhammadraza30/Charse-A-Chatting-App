package main;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailSender {

    private static final String FROM_EMAIL = "mail sending email";
    private static final String FROM_PASSWORD = "its password";
    private static final String[] TO_EMAILS = {"Admin or receiver email"};

    public static void sendEmail(String subject, String message) {
        String host = "smtp.gmail.com";

        Properties pr = System.getProperties();
        pr.put("mail.smtp.host", host);
        pr.put("mail.smtp.port", "465");
        pr.put("mail.smtp.ssl.enable", "true");
        pr.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(pr, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        });
        session.setDebug(true);

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(FROM_EMAIL));
            for (String recipient : TO_EMAILS) {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}