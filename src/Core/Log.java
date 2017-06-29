package Core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//This class is in control of logging error and status of different parts of the program
//  This allow debug and error testing
public class Log {

    //These are constant values which check printing and saving of data to a file
    private static final boolean printLog = true;
    private static final boolean fileLog = false;

    //The file location of the text file
    private static final String fileName = "src/Core/log.txt";

    //This logs a basic message
    public static void log(String logMessage){
        if (printLog){
            printLog(logMessage);
        }
        if (fileLog){
            saveLog(logMessage);
        }
    }

    //This logs a basic message with a status
    //  * = Error
    //  + = Good thing
    //  - = Bad thing
    public static void logStatus(String logMessage, char status){
        String message = "[" + status + "] " + logMessage;
        log(message);
    }

    //This logs a basic message with a error status
    public static void logError(String logMessage){
        String message = "[*] " + logMessage;
        log(message);
    }

    //This prints the prints the log message
    private static void printLog(String logMessage){
        System.out.println(logMessage);
    }

    //This saves the log message to a file
    private static void saveLog(String logMessage){
        File file = new File(fileName);
        try {
            FileWriter writer = new FileWriter(file.getAbsoluteFile(), true);
            writer.write(logMessage + "\n");
            writer.flush();
            writer.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }

    //This is the opening line of the start file
    public static void startFile(){
        saveLog("Log file");
    }
}