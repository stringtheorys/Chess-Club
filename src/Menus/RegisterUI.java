package Menus;

import Alerts.MessageAlert;
import Database.DB_interface;
import Database.UserDB_interface;
import Core.Constants;
import Core.Log;
import Core.ScreenController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class RegisterUI extends VBox implements Constants {

    //Attribute
    private ScreenController parent;
    private PhotoButton selectedPhotoButton;

    private Label topLabel;
    private Button menuButton;
    private HBox firstBox; //Username email
    private Label usernameLabel;
    private Label emailLabel;
    private TextField usernameTextField;
    private TextField emailTextField;
    private HBox secondBox; //firstName surname
    private Label firstNameLabel;
    private Label surnameLabel;
    private TextField firstNameTextField;
    private TextField surnameTextField;
    private HBox thirdBox; //Password confirm password
    private Label passwordLabel;
    private Label confirmPasswordLabel;
    private TextField passwordTextField;
    private TextField confirmPasswordTextField;
    private HBox accessLevelBox;
    private Label accessLevelsLabel;
    private ComboBox<AccessLevel> accessLevelsCombo;
    private GridPane photosBox;
    private Label selectPhotoLabel;
    private PhotoButton[] photoButtons;
    private HBox buttonsBox;
    private Button createAccountButton;
    private Button signInButton;

    //User inputs
    String username;
    String firstname;
    String surname;
    String email;
    String password;
    String confirmPassword;
    AccessLevel userType;
    PhotoButton profilePhoto;

    //Constructor
    public RegisterUI(ScreenController parent){
        super();

        this.parent = parent;
        this.setAlignment(Pos.TOP_CENTER);

        Log.logStatus("Create registration screen", '+');

        initGui();
    }

    //Create the UI
    private void initGui(){

        LinkedList<String[]> accessLevelInfos = DB_interface.getAccessInfo();
        LinkedList<String[]> PhotoInfo = DB_interface.getPhotoInfo();
        if (accessLevelInfos == null || accessLevelInfos.size() == 0 || PhotoInfo == null || PhotoInfo.size() == 0){
            Log.log("DB connection fail");
            MessageAlert.DataBaseConnectionAlert();
            parent.showPlayChess();
        } else {

            menuButton = new Button("Menu");
            menuButton.setFont(mediumFont);
            menuButton.setOnAction(event -> menuButton());
            this.getChildren().add(menuButton);
            setMargin(menuButton, rightMargin);

            topLabel = new Label("Create Account");
            topLabel.setFont(extraLargeFont);
            this.getChildren().add(topLabel);
            setMargin(topLabel, mediumMargin);

            firstBox = new HBox();
            usernameLabel = new Label("Username :");
            usernameLabel.setFont(mediumFont);
            firstBox.getChildren().add(usernameLabel);
            usernameTextField = new TextField();
            usernameTextField.setMaxWidth(textFieldLength);
            usernameTextField.requestFocus();
            usernameTextField.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.TAB)) {
                    emailTextField.requestFocus();
                }
            });
            firstBox.getChildren().add(usernameTextField);
            HBox.setMargin(usernameTextField, smallRightMargin);
            emailLabel = new Label("Email :");
            emailLabel.setFont(mediumFont);
            firstBox.getChildren().add(emailLabel);
            emailTextField = new TextField();
            emailTextField.setMinWidth(largeTextFieldLength);
            emailTextField.setMaxWidth(largeTextFieldLength);
            emailTextField.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.TAB)) {
                    firstNameTextField.requestFocus();
                }
            });
            firstBox.getChildren().add(emailTextField);
            this.getChildren().add(firstBox);
            setMargin(firstBox, mediumMargin);

            secondBox = new HBox();
            firstNameLabel = new Label("Firstname :");
            firstNameLabel.setFont(mediumFont);
            secondBox.getChildren().add(firstNameLabel);
            firstNameTextField = new TextField();
            firstNameTextField.setMaxWidth(textFieldLength);
            firstNameTextField.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.TAB)) {
                    surnameTextField.requestFocus();
                }
            });
            secondBox.getChildren().add(firstNameTextField);
            HBox.setMargin(firstNameTextField, smallRightMargin);
            surnameLabel = new Label("Surname :");
            surnameLabel.setFont(mediumFont);
            secondBox.getChildren().add(surnameLabel);
            surnameTextField = new TextField();
            surnameTextField.setMaxWidth(textFieldLength);
            surnameTextField.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.TAB)) {
                    passwordTextField.requestFocus();
                }
            });
            secondBox.getChildren().add(surnameTextField);
            this.getChildren().add(secondBox);
            setMargin(secondBox, mediumMargin);

            thirdBox = new HBox();
            passwordLabel = new Label("Password :");
            passwordLabel.setFont(mediumFont);
            thirdBox.getChildren().add(passwordLabel);
            passwordTextField = new TextField();
            passwordTextField.setMaxWidth(textFieldLength);
            passwordTextField.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.TAB)) {
                    confirmPasswordLabel.requestFocus();
                }
            });
            thirdBox.getChildren().add(passwordTextField);
            HBox.setMargin(passwordTextField, smallRightMargin);
            confirmPasswordLabel = new Label("Confirm :");
            confirmPasswordLabel.setFont(mediumFont);
            thirdBox.getChildren().add(confirmPasswordLabel);
            confirmPasswordTextField = new TextField();
            confirmPasswordTextField.setMaxWidth(textFieldLength);
            thirdBox.getChildren().add(confirmPasswordTextField);
            this.getChildren().add(thirdBox);
            setMargin(thirdBox, mediumMargin);

            accessLevelBox = new HBox();
            accessLevelsLabel = new Label("User Type :");
            accessLevelsLabel.setFont(mediumFont);
            accessLevelBox.getChildren().add(accessLevelsLabel);
            HBox.setMargin(accessLevelsLabel, smallRightMargin);

            accessLevelsCombo = new ComboBox<>();
            accessLevelsCombo.setCellFactory(
                    new Callback<ListView<AccessLevel>, ListCell<AccessLevel>>() {
                        @Override
                        public ListCell<AccessLevel> call(ListView<AccessLevel> param) {
                            ListCell<AccessLevel> cell = new ListCell<AccessLevel>() {
                                @Override
                                protected void updateItem(AccessLevel al, boolean empty) {
                                    super.updateItem(al, empty);
                                    if (empty) {
                                        setText("");
                                    } else {
                                        setText(al.getAccessName());
                                    }
                                }
                            };
                            return cell;
                        }
                    });

            accessLevelsCombo.setButtonCell(
                    new ListCell<AccessLevel>() {
                        @Override
                        protected void updateItem(AccessLevel al, boolean bln) {
                            super.updateItem(al, bln);
                            if (bln) {
                                setText("");
                            } else {
                                setText(al.getAccessName());
                            }
                        }
                    });


            for (String[] accessLevelInfo : accessLevelInfos) {
                AccessLevel al = new AccessLevel(Integer.parseInt(accessLevelInfo[0]),
                        accessLevelInfo[1]);
                accessLevelsCombo.getItems().add(al);
            }
            accessLevelsCombo.setValue(accessLevelsCombo.getItems().get(0));
            accessLevelBox.getChildren().add(accessLevelsCombo);
            this.getChildren().add(accessLevelBox);
            setMargin(accessLevelBox, mediumMargin);

            photoButtons = new PhotoButton[PhotoInfo.size()];

            selectPhotoLabel = new Label("Set profile photo");
            selectPhotoLabel.setFont(mediumFont);
            this.getChildren().add(selectPhotoLabel);
            setMargin(selectPhotoLabel, smallMargin);
            photosBox = new GridPane();
            photosBox.setAlignment(Pos.TOP_CENTER);
            photosBox.setHgap(5);
            photosBox.setVgap(5);

            int x = 0;
            int maxX = 3;
            int y = 0;
            for (int i = 0; i < PhotoInfo.size(); i++) {
                if (x > maxX) {
                    x = 0;
                    y += 1;
                }
                PhotoButton pb = new PhotoButton(this, Integer.parseInt(PhotoInfo.get(i)[0]), PhotoInfo.get(i)[1]);
                photosBox.add(pb, x, y);
                photoButtons[i] = pb;
                x += 1;
            }

            this.getChildren().add(photosBox);
            setMargin(photosBox, mediumMargin);

            buttonsBox = new HBox();
            buttonsBox.setAlignment(Pos.TOP_CENTER);
            createAccountButton = new Button("Create Account");
            createAccountButton.setFont(mediumFont);
            createAccountButton.setOnAction(event -> createAccount());
            buttonsBox.getChildren().add(createAccountButton);
            HBox.setMargin(createAccountButton, smallRightMargin);
            signInButton = new Button("Sign In");
            signInButton.setFont(mediumFont);
            signInButton.setOnAction(event -> signIn());
            buttonsBox.getChildren().add(signInButton);
            this.getChildren().add(buttonsBox);
        }
    }

    //Create an account
    private void createAccount(){
        Log.log("Create account button pressed");

        username = usernameTextField.getText();
        firstname = firstNameTextField.getText();
        surname = surnameTextField.getText();
        email = emailTextField.getText();
        password = passwordTextField.getText();
        confirmPassword = confirmPasswordTextField.getText();
        userType = accessLevelsCombo.getValue();
        System.out.println(userType.getAccessName());
        profilePhoto = selectedPhotoButton;

        if (testInputs()){
            int accessLevelId = userType.getAccessId();
            int profilePhotoId = profilePhoto.getPhotoId();
            int result = UserDB_interface.RegisterAccount(username, firstname, surname, email,
                    password, accessLevelId, profilePhotoId);
            if (result == -1){
                Log.log("Error occurred");
                MessageAlert.DataBaseConnectionAlert();
            } else if (result == 0){
                Log.log("username or email exist");
                MessageAlert.UsernameEmailExist();
            } else {
                Log.log("Registration completed");
                parent.showPlayChess(result, accessLevelId);
            }
        }
    }

    //Test the user input if they are legit
    private boolean testInputs(){
        if (username.equals("")){
            MessageAlert.MissingInput("Username");
        } else if (firstname.equals("")){
            MessageAlert.MissingInput("FirstName");
        } else if (surname.equals("")){
            MessageAlert.MissingInput("Surname");
        } else if (email.equals("")){
            MessageAlert.MissingInput("Email");
        } else if (password.equals("")){
            MessageAlert.MissingInput("Password");
        } else if(confirmPassword.equals("")){
            MessageAlert.MissingInput("Confirm Password");
        } else if (userType == null){
            MessageAlert.MissingUserType();
        } else if (profilePhoto == null){
            MessageAlert.MissingPhoto();
        } else if (! password.equals(confirmPassword)) {
            MessageAlert.PasswordNotSame();
        } else if (password.length() <= 7) {
            MessageAlert.PasswordNotLong();
        } else if (!Pattern.matches("[0-9]+", password)) {
            MessageAlert.PasswordNoDigit();
        } else {
            return true;
        }
        return false;
    }

    //Button method for changing screen
    private void signIn(){
        Log.log("Sign in button pressed");
        parent.showSignIn();
    }
    private void menuButton(){
        Log.log("Menu button pressed");
        parent.showPlayChess();
    }

    //Set the selected image
    public void setSelectedPhotoButton(PhotoButton button){
        if (selectedPhotoButton != null){
            selectedPhotoButton.setUnSelectedBorder();
        }
        selectedPhotoButton = button;
    }
    //Unselect the image
    public void unSelectPhoto(){
        selectedPhotoButton = null;
    }

    //Access Level class used within the combo box
    private class AccessLevel {
        private String accessName;
        private int accessId;

        public AccessLevel(int id, String name){
            this.accessName = name;
            this.accessId = id;
            System.out.println(accessId);
        }

        public String getAccessName(){
            return this.accessName;
        }
        public int getAccessId(){
            return this.accessId;
        }
    }

    //Photo image button with the images to select
    private class PhotoButton extends VBox {

        //Attributes
        private final Border selectedBorder = new Border(new BorderStroke(
                Paint.valueOf("red"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT
        ));
        private final Border unSelectedBorder = new Border(new BorderStroke(
                Paint.valueOf("black"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT
        ));

        private int PhotoId;
        private String PhotoLocation;
        private RegisterUI parent;

        //Constructor
        public PhotoButton(RegisterUI parent, int PhotoId, String PhotoLocation){
            super();

            this.PhotoId = PhotoId;
            this.parent = parent;
            this.PhotoLocation = PhotoLocation;

            this.initGui();
            this.setUnSelectedBorder();
        }

        //Create the button
        private void initGui(){
            Image image = new Image(getClass().getResourceAsStream("ProfilePhotos\\" + PhotoLocation));
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(smallPhotoHeights);

            this.getChildren().add(imageView);

            imageView.setOnMousePressed(event -> setSelectedBorder());
        }

        //Sets the border type
        public void setSelectedBorder(){
            Log.log("Select button photoId :" + PhotoId);
            this.setBorder(selectedBorder);
            parent.setSelectedPhotoButton(this);
        }

        //Unselects the button by removing border
        public void setUnSelectedBorder(){
            Log.log("Unselect button photoId :" + PhotoId);
            this.setBorder(unSelectedBorder);
            parent.unSelectPhoto();
        }

        public int getPhotoId(){
            return this.PhotoId;
        }
    }
}
