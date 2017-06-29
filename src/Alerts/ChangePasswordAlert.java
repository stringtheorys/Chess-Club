package Alerts;


import Core.Constants;
import Database.UserDB_interface;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ChangePasswordAlert implements Constants {

    //A private constructor prevent any external classes creating this object
    private ChangePasswordAlert() {
    }

    //Static function that loads the box
    public static void showBox() {
        Dialog<String[]> box = new Dialog<>();

        box.setTitle("Change password");
        box.setHeaderText("please enter usename, old and new password");

        //Change password button
        ButtonType changePassButton = new ButtonType("Change password", ButtonBar.ButtonData.OK_DONE);
        box.getDialogPane().getButtonTypes().add(changePassButton);
        box.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setHgap(10);

        grid.setPadding(new Insets(20, 150, 10, 10));

        //User input fields
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("Old Password");
        PasswordField newPassword1 = new PasswordField();
        newPassword1.setPromptText("New Password");
        PasswordField newPassword2 = new PasswordField();
        newPassword2.setPromptText("Re-enter new password");

        //Adds the input fields to the screen
        grid.add(new Label("Username :"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Old Password :"), 0,1);
        grid.add(oldPassword, 1, 1);
        grid.add(new Label("New Password :"), 0, 2);
        grid.add(newPassword1, 1, 2);
        grid.add(new Label("New Password :"), 0, 3);
        grid.add(newPassword2, 1, 3);

        box.getDialogPane().setContent(grid);

        //When the user clicks the button then calls this method
        box.setResultConverter(button -> {
            if (button == changePassButton){
                //Adds all information to an array to send out of the box
                String[] returnInformation = new String[]{
                        username.getText(),
                        oldPassword.getText(),
                        newPassword1.getText(),
                        newPassword2.getText()
                };
                return returnInformation;
            } else {
                return null;
            }
        });

        //Return data from the box
        Optional<String[]> result = box.showAndWait();

        //Parse the data
        if (result.isPresent()) {
            String[] userInformation = result.get();
            String Username = userInformation[0];
            String OldPassword = userInformation[1];
            String NewPassword1 = userInformation[2];
            String NewPassword2 = userInformation[3];

            //Check the user information is ok
            if (UserDB_interface.LogInUser(Username, OldPassword).length == 2) {
                if (NewPassword1.equals(NewPassword2)) {
                    UserDB_interface.changePassword(Username, NewPassword1);
                    //Change password
                    MessageAlert.PasswordChanged();
                } else {
                    MessageAlert.PasswordNotSame();
                }
            } else {
                MessageAlert.IncorrectLogIn();
            }
        }
    }
}
