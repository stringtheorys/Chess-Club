package Menus;

import Alerts.ChangePasswordAlert;
import Alerts.ForgottenPasswordAlert;
import Alerts.MessageAlert;
import Core.Constants;
import Core.Log;
import Core.ScreenController;
import Database.UserDB_interface;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class SignInUI extends VBox implements Constants {

    //Attirbutes
    private ScreenController parent;

    private Label topLabel;
    private Button menuButton;
    private Label usernameLabel;
    private TextField usernameTextField;
    private Label passwordLabel;
    private PasswordField passwordTextField;

    private Button logInButton;
    private Button registerButton;

    private Button forgetPassword;
    private Button changePassword;

    //Constructor
    public SignInUI(ScreenController parent){
        super();

        this.parent = parent;
        this.setAlignment(Pos.TOP_CENTER);

        Log.logStatus("Creating Sign In screen", '+');

        initGui();
    }

    //Create the UI
    private void initGui() {

        menuButton = new Button("Menu");
        menuButton.setFont(mediumFont);
        menuButton.setOnAction(event -> showMenu());
        this.getChildren().add(menuButton);
        setMargin(menuButton, rightMargin);

        topLabel = new Label("Sign In");
        topLabel.setFont(extraLargeFont);
        this.getChildren().add(topLabel);
        setMargin(topLabel, mediumMargin);

        usernameLabel = new Label("Username");
        usernameLabel.setFont(largeFont);
        this.getChildren().add(usernameLabel);
        setMargin(usernameLabel, smallMargin);

        usernameTextField = new TextField();
        usernameTextField.setMaxWidth(textFieldLength);
        usernameTextField.requestFocus();
        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.TAB)){
                passwordTextField.requestFocus();
            }
        });
        this.getChildren().add(usernameTextField);
        setMargin(usernameTextField, mediumMargin);

        passwordLabel = new Label("Password");
        passwordLabel.setFont(largeFont);
        this.getChildren().add(passwordLabel);
        setMargin(passwordLabel, smallMargin);

        passwordTextField = new PasswordField();
        passwordTextField.setMaxWidth(textFieldLength);
        //passwordTextField.
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)){
                logIn();
            }
        });
        this.getChildren().add(passwordTextField);
        setMargin(passwordTextField, largeMargin);

        logInButton = new Button("Log In");
        logInButton.setFont(mediumFont);
        logInButton.setOnAction(event -> logIn());
        this.getChildren().add(logInButton);
        setMargin(logInButton, mediumMargin);

        registerButton = new Button("Registration");
        registerButton.setFont(mediumFont);
        registerButton.setOnAction(event -> showRegister());
        this.getChildren().add(registerButton);
        setMargin(registerButton, extraLargeMargin);

        forgetPassword = new Button("Forgotten password?");
        forgetPassword.setFont(smallFont);
        forgetPassword.setOnAction(event -> forgetPassword());
        this.getChildren().add(forgetPassword);
        setMargin(forgetPassword, mediumMargin);

        changePassword = new Button("Change Password");
        changePassword.setFont(smallFont);
        changePassword.setOnAction(event -> changePassword());
        this.getChildren().add(changePassword);
    }

    //Show the forgotten password box
    private void forgetPassword(){
        ForgottenPasswordAlert.showBox();
        Log.log("Forgotten password button pressed");
    }

    //Show the change password box
    private void changePassword(){
        ChangePasswordAlert.showBox();
        Log.log("Change password button pressed");
    }

    //Change the screen to the play chess menu
    private void showMenu(){
        Log.log("Show menu button pressed");
        parent.showPlayChess();
    }
    //Change the screen to the register account menu
    private void showRegister(){
        Log.log("Show register button pressed");
        parent.showRegistration();
    }
    //Change the screen to the log in page
    private void logIn(){
        Log.log("Log in button pressed");
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        Log.log("Username :" + username);
        Log.log("Password :" + password);

        //int[] {UserId, AccessLevel}
        int[] result = UserDB_interface.LogInUser(username, password);
        //Successful
        if (result.length == 2) {
            int UserId = result[0];
            int AccessLevel = result[1];
            Log.logStatus("LogIn : Log in successful", '+');
            Log.log(String.format("LogIn : User information > Id = %d, Access = %d", UserId, AccessLevel));
            parent.showPlayChess(UserId, AccessLevel);
        } else if (result[0] == -1) {
            Log.logError("LogIn : Log in user error occurred");
            MessageAlert.DataBaseConnectionAlert();
        } else if (result[0] == 0) {
            Log.logStatus("LogIn : Log in unsuccessful",'-');
            MessageAlert.IncorrectLogIn();
        }
    }
}
