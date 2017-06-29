package Alerts;

import Core.Constants;
import Core.Log;
import Database.UserDB_interface;
import javafx.scene.control.TextInputDialog;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

//This is a class for sending email to the user if they have forgotten password
public class ForgottenPasswordAlert implements Constants {

    //private constructor prevent the creating of this object
    private ForgottenPasswordAlert() {
    }

    //Static function that show the box to the user
    public static void showBox() {
        //Creates the box with title, header and one input of email
        TextInputDialog box = new TextInputDialog();
        box.setTitle("Password Recovery");
        box.setHeaderText("Forgotten Password");
        box.setContentText("Email :");

        //Information returned from the box
        Optional<String> result = box.showAndWait();
        if (result.isPresent()) {
            //Gets the information entered
            String enteredEmail = result.get();
            //Checks that the information is ok
            Log.log(enteredEmail);
            if (enteredEmail.equals("")) {
                MessageAlert.ForgottenPass_NoEmail();
            } else if (UserDB_interface.doesUserExist(enteredEmail)) {
                //Generates the new password
                String newPassword = generateNewPassword();
                //Tries to send the email
                if (SendPasswordEmail(enteredEmail, newPassword)) {
                    MessageAlert.SendPasswordRecoverySuccess();
                    //Changes the password
                    UserDB_interface.changePassword(enteredEmail, newPassword);
                } else {
                    MessageAlert.SendingEmailException();
                }
            } else {
                MessageAlert.ForgottenPass_UnknownEmail();
            }
        }
    }

    //Static function that tries to send an email to the user
    private static boolean SendPasswordEmail(String UserEmail, String newUserPassword){
        Log.log("New Password :" + newUserPassword);

        final String Email = "chesstwy@gmail.com";
        final String Password = "chessclub1";

        String name = UserDB_interface.getUser_Name(UserEmail);

        //Creates the mail properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        //Creates a session for the email to be sent within
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Email, Password);
                    }
                });

        //Tries to send the emai
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Email));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(UserEmail));

            //Email message
            String StringMessage = String.format("Dear %s,\nYour Chess Club password has been changed to : %s\nFrom " +
                    "\nChess Club", name, newUserPassword);

            message.setSubject("Change password");
            message.setText(StringMessage);
            message.setSentDate(new Date());

            Transport.send(message);

            //If successful then return true
            Log.log("SendPasswordEmail : Successful");
            return true;
        } catch (MessagingException e) {
            //Error occur then return false
            Log.log("SendPasswordEmail : Exception >" + e.getMessage());
            return false;
        }
    }

    //Static function that generates a new password of length 8
    private static String generateNewPassword() {
        //Password information
        final int passwordLength = 8;
        final char[] options = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        final Random rnd = new Random();
        String newPassword = "";
        //Loops through till the password length, adding a random character to the password
        for (int i = 0; i < passwordLength; i++){
            newPassword +=  options[rnd.nextInt(options.length)];
        }
        return newPassword;
    }
}