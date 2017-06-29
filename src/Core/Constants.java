package Core;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

//This is a interface class which holds all the values used in the gui creation
public interface Constants {

    //Text size options
    int smallTextSize = 15;
    int mediumTextSize = 20;
    int largeTextSize = 30;
    int extraLargeTextSize = 50;

    //Text type
    String fontFamily = "verdana";

    //Font types
    Font smallFont = Font.font(fontFamily, smallTextSize);
    Font mediumFont = Font.font(fontFamily, mediumTextSize);
    Font largeFont = Font.font(fontFamily, largeTextSize);
    Font extraLargeFont = Font.font(fontFamily, extraLargeTextSize);

    //Margins
    Insets smallMargin = new Insets(0,0,5,0);
    Insets mediumMargin = new Insets(0,0,10,0);
    Insets largeMargin = new Insets(0,0,20,0);
    Insets extraLargeMargin = new Insets(0,0,55,0);
    Insets rightMargin = new Insets(10,0,0,420);
    Insets smallRightMargin = new Insets(0, 20, 0,0);
    Insets leftMargin = new Insets(10,420,0,0);
    Insets zeroMargin = new Insets(-10,-420,0,0);

    //Borders
    Border blackBorder = new Border(new BorderStroke(
            Paint.valueOf("black"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT
    ));

    //Backgrounds
    Background redBackground = new Background(new BackgroundFill(Paint.valueOf("Crimson"), CornerRadii.EMPTY, Insets.EMPTY));

    //Textfield lengths
    int textFieldLength = 150;
    int largeTextFieldLength = 200;

    //Image sizes
    int photoHeights = 100;
    int smallPhotoHeights = 70;

    //Regex patterns
    String emailPattern = "[@]{1}[.]+";
    String usernamePattern = "^\\w";
    String passwordPattern = "";
    String namePattern = "";
    String sqlPattern = "";

    String lower_alpha = "";
    String upper_alpha = "";
    String digit = "";

    //The screen sizes
    int menuWidth = 650;
    int menuHeight = 550;
    int chessWidth = 830;
    int chessHeight = 680;
}
