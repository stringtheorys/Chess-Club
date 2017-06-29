package Alerts;

import javafx.scene.control.Alert;

//This is class for showing alert to the user
public class MessageAlert {

    //Private constructor
    private MessageAlert() {
    }

    //Creates the Error alert version of the message
    private static void ErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
    //Creates the Information alert version of the message
    private static void InformationAlert(String information) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(information);
        alert.showAndWait();
    }

    //These are static functions for different error types
    //  The functions call the constructor to create an alert with the correct error message
    public static void DataBaseConnectionAlert(){
        ErrorAlert("Can't Connect to database");
    }
    public static void IncorrectLogIn(){
        ErrorAlert("Incorrect username or password");
    }
    public static void MissingInput(String input) {
        ErrorAlert("You are required to enter a value into " + input);
    }
    public static void MissingPhoto(){
        ErrorAlert("You need to select a user profile photo");
    }
    public static void MissingUserType(){
        ErrorAlert("You need to select a value in the drop down box for the user level");
    }
    public static void UsernameEmailExist(){
        ErrorAlert("The Username or Email already exists");
    }
    public static void PasswordNotSame() {
        ErrorAlert("Passwords are the not the same");
    }
    public static void MaxTournamentSize(){
        ErrorAlert("You can't add a player as at max tournament size");
    }
    public static void PasswordNotLong() {
        ErrorAlert("The password entered need to be longer than 7 characters");
    }
    public static void PasswordNoDigit() {
        ErrorAlert("The password entered must contain a digit");
    }
    public static void TournamentName() {
        ErrorAlert("Enter a tournament name");
    }
    public static void MaxPlayerNum() {
        ErrorAlert("You can't enter another player, as at tournament size");
    }
    public static void ForgottenPass_NoEmail() {
        ErrorAlert("You need to enter an email address");
    }
    public static void ForgottenPass_UnknownEmail() {
        ErrorAlert("Unknown email address entered");
    }
    public static void SendPasswordRecoverySuccess() {
        InformationAlert("Send email to email address with password");
    }
    public static void SendingEmailException() {
        ErrorAlert("Exception entering email address");
    }
    public static void PasswordChanged() {
        InformationAlert("Your password has been changed");
    }
    public static void IncorrectInput(String message) {
        ErrorAlert("Input " + message + " is incorrect");
    }
    public static void MorePlayersNeeded() {
        ErrorAlert("More players are needed till the tournament player size is reached");
    }
}