package Database;

import Core.Log;

import java.sql.*;
import java.util.LinkedList;

//This class is superclass controlling the database which TournamentDB_interface and UserDB_interface are subclasses
public abstract class DB_interface {

    //Attributes
    private static final String DB_host = "jdbc:derby://localhost/ChessClub";
    private static final String DB_username = "ChessClub";
    private static final String DB_password = "ChessClub";
    private static final String DB_driver = "org.apache.derby.jdbc.ClientDriver";
    private static Connection DB_conn;

    //Protected variable which is used by the subclasses
    protected static Statement statement;

    //This test that there is connection to the database
    public static boolean test_DBConnection() {
        //Tries to connect to the database
        try {
            Class.forName(DB_driver);
            DB_conn = DriverManager.getConnection(DB_host, DB_username, DB_password);
            DB_conn.close();
            DB_conn = null;
            Log.log("test_DBConnection : Connection success");
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Log.logError("test_DBConnection : Connection unsuccessful");
            Log.log(ex.getMessage());
            return false;
        }
    }

    //Tries to connect to the database which is called by the subclasses
    protected static void connect_DB() throws IllegalArgumentException, ClassNotFoundException, SQLException {
        if (DB_conn != null) {
            throw new IllegalArgumentException("Connect_DB : Already connected to the database");
        } else {
            try {
                Class.forName(DB_driver);
                DB_conn = DriverManager.getConnection(DB_host, DB_username, DB_password);
                statement = DB_conn.createStatement();
                Log.log("Connect_DB : Connection to database successful");
            } catch (ClassNotFoundException ex) {
                throw new ClassNotFoundException("Connect_DB : Driver class is not found");
            } catch (SQLException ex) {
                throw new SQLException("Connect_DB : Can't Connect to Database");
            }
        }
    }

    //Tries to disconnect from the database
    protected static void disconnect_DB() throws IllegalArgumentException, SQLException {
        if (DB_conn != null) {
            try {
                DB_conn.close();
                DB_conn = null;
                statement = null;
                Log.log("Disconnect_DB : Disconnection from database is successful");
            } catch (SQLException ex) {
                Log.logError("Disconnect_DB : Can't Close Database >" + ex.getMessage());
            }
        } else {
            Log.logError("Disconnect_DB : Already disconnected from the database");
        }
    }

    //This is a function which finds all the locations of '?' and replaces with an input value
    //  It is used by the subclasses when creating sql statement to prevent having to add strings
    //  This allows the string without variables to be entered and the variables are the inputs
    protected static String sqlBuilder(String orginalSql, Object... inputs) {
        int inputpos = 0;
        StringBuilder newSql = new StringBuilder();
        //This iterates through the array
        for (char val : orginalSql.toCharArray()) {
            //If '?' then add the input
            if (val == '?') {
                newSql.append(inputs[inputpos]);
                inputpos++;
            } else {
                newSql.append(val);
            }
        }
        if (inputs.length != inputpos) {
            throw new IllegalArgumentException("String inputs may have been missed");
        }
        Log.log("SqlBuilder Result: " + newSql.toString());
        //Returns a complete string with variables
        return newSql.toString();
    }

    //This executes a query on the database that is pass in as an argument
    protected static ResultSet executeQuery(String sql) {
        //Return value
        ResultSet rs;

        //Tests if the application can connect to database
        if (test_DBConnection()) {
            try {
                connect_DB();

                Log.log("ExecuteQuery : Sql statement >" + sql);
                //Executes the query
                rs = statement.executeQuery(sql);

                return rs;
            } catch (SQLException | ClassNotFoundException e) {
                //Exception occurred
                Log.log("ExecuteQuery : Exception >" + e.getMessage());
                return null;
            }
        } else {
            Log.log("ExecuteQuery : Can't connect to DB");
            return null;
        }
    }

    //This updates the database based on a query passed in as an argument
    protected static boolean executeUpdate(String sql) {
        //Test that the application can connect to the database
        if (test_DBConnection()) {
            try {
                connect_DB();

                Log.log("ExecuteUpdate : sql statement >" + sql);
                //Executes the update
                statement.executeUpdate(sql);

                //Returns that the update was successful
                return true;
            } catch (SQLException | ClassNotFoundException e) {
                Log.log("ExecuteUpdate : Exception >" + e.getMessage());

                //Returned that the update was unsuccessful
                return false;
            }
        } else {
            Log.log("ExecuteUpdate : Can't connect to DB");

            //Returns that the update was unsuccessful
            return false;
        }
    }

    //This gets all of the access levels available
    public static LinkedList<String[]> getAccessInfo() {

        //Gets the accessLevel id and names from the access levels table
        String sql = sqlBuilder("SELECT AccessLevelId, AccessLevelName FROM AccessLevels");
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("getAccessInfo : No result set returned");
            return null;
        }

        //As the number of the elements in the table then a dynamic array is required
        LinkedList<String[]> accessInfo = new LinkedList<>();
        try {
            while (rs.next()) {
                //Gets the information and adds to a string array then dynamic array
                String AccessId = rs.getString(1);
                String AccessLevelName = rs.getString(2);
                String[] info = new String[]{AccessId, AccessLevelName};
                accessInfo.add(info);
            }

            if (accessInfo.size() > 0) {
                Log.logStatus("getAccessInfo : Access Level information returned", '+');
                return accessInfo;
            } else {
                Log.logError("getAccessInfo : No User info is returned");
                return null;
            }
        } catch (SQLException e) {
            Log.logError("getAccessInfo : Exception >" + e.getMessage());

            return null;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("getAccessInfo : Exception >" + e.getMessage());
            }
        }

    }

    //This returns a array of string arrays containing a photo id and photo location
    public static LinkedList<String[]> getPhotoInfo() {

        //Selects all of the photo information from profilephotos tables
        String sql = sqlBuilder("SELECT ProfilePhotoId, PhotoLocation FROM ProfilePhotos");
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("getPhotoInfo : No result set returned");
            return null;
        }

        //As the number of elements from the list is unknown then a dynamic array is used
        LinkedList<String[]> photoInfo = new LinkedList<>();
        try {
            while (rs.next()) {
                //Gets the photo information into an array then dynamic array
                String PhotoId = rs.getString(1);
                String PhotoLocation = rs.getString(2);
                String[] info = new String[]{PhotoId, PhotoLocation};
                photoInfo.add(info);
            }

            //Checks if no information is returned
            if (photoInfo.size() > 0) {
                Log.logStatus("getPhotoInfo : Photo information returned", '+');
                return photoInfo;
            } else {
                Log.logError("getPhotoInfo : No User info is returned");
                return null;
            }

        } catch (SQLException e) {
            Log.log("getPhotoInfo : Exception >" + e.getMessage());

            return null;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("getPhotoInfo : Exception >" + e.getMessage());
            }
        }
    }
}