package Database;

import Core.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class UserDB_interface extends DB_interface {

    public static String[] getUserInfo(int UserId){
        String sql = sqlBuilder("SELECT Users.Username, ProfilePhotos.PhotoLocation FROM Users " +
                "INNER JOIN ProfilePhotos ON Users.ProfilePhotoId = ProfilePhotos.ProfilePhotoId " +
                "WHERE Users.UserId = ?", UserId);

        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("getUserInfo : No result set return");
            return null;
        }
        try {
            if (rs.next()){
                String username = rs.getString(1);
                String photoLocation = rs.getString(2);
                String[] userInfo = new String[]{username, photoLocation};
                Log.logStatus("getUserInfo : User Info >" + username + ", " + photoLocation, '+');
                return userInfo;
            } else {
                Log.logError("getUserInfo : No User info is returned");
                return null;
            }
        } catch (SQLException e) {
            Log.log("getUserInfo : Exception >" + e.getMessage());
            return null;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("getUserInfo : Exception >" + e.getMessage());
            }
        }
    }

    public static int[] LogInUser(String username, String password) {
        final int[] error = new int[]{-1};
        String sql = sqlBuilder("SELECT Users.UserId, Users.Password, AccessLevels.AccessLevel FROM Users " +
                "INNER JOIN AccessLevels ON Users.AccessLevelId = AccessLevels.AccessLevelId " +
                "WHERE Username = '?' OR Email = '?' ", username, username);
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("LogInUser : No result set returned");
            return error;
        }

        try {
            if (rs.next()) {
                int userId = rs.getInt(1);
                String storedPassword = rs.getString(2);
                int AccessLevel = rs.getInt(3);
                Log.logStatus(String.format("LogInUser : UserId found (UserId: %d, Stored password: %s, AccessLevel: %d)", userId, storedPassword, AccessLevel), '+');
                if (Hash.checkPassword(password, storedPassword)) {
                    Log.logStatus("LogInUser : Stored Password MATCH hashed password", '+');
                    return new int[]{userId, AccessLevel};
                } else {
                    Log.logStatus("LogInUser : Stored Password NOT MATCH hashed password", '-');
                    return new int[]{0};
                }
            } else {
                return error;
            }
        } catch (SQLException e) {
            Log.log("LogInUser : Exception >" + e.getMessage());
            return error;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("LogInUser : Exception >" + e.getMessage());
            }
        }
    }

    /*
        -1 : Error Occurred
         0 : Username or Email address already exists
         1 : Successful
     */
    public static int RegisterAccount(String Username, String FirstName, String Surname,
                                      String Email, String Password, int AccessLevelId, int ProfilePhotoId){

        String sql = sqlBuilder("SELECT UserId FROM Users WHERE Username = '?' OR Email ='?'", Username, Email);
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("RegisterAccount : No result set returned");
            return -1;
        }
        try {
            if (rs.next()){
                Log.logStatus("RegisterAccount : Username or Email address exists", '-');
                return 0;
            } else {
                Log.logStatus("RegisterAccount : Username or Email Address does not exists", '+');
                String HashedPassword = Hash.getSaltedHash(Password);
                sql = sqlBuilder("INSERT INTO Users (Username, FirstName, Surname, Email, Password, ProfilePhotoId, AccessLevelId) VALUES ('?','?','?','?','?',?,?)", Username, FirstName, Surname, Email, HashedPassword, Integer.toString(ProfilePhotoId), Integer.toString(AccessLevelId));
                executeUpdate(sql);

                sql = sqlBuilder("SELECT UserId FROM Users WHERE Username = '?'", Username);
                rs = executeQuery(sql);
                if (rs == null) {
                    Log.log("RegisterAccount : No result set returned");
                    return -1;
                }
                if (rs.next()){
                    int UserId = rs.getInt(1);
                    return UserId;
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            Log.log("RegisterAccount : Exception >" + e.getMessage());
            return -1;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("RegisterAccount : Exception >" + e.getMessage());
            }
        }
    }

    public static boolean doesUserExist(String Email){
        String sql = sqlBuilder("SELECT UserId FROM Users WHERE Email = '?'", Email);
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("getUserExist : No result set returned");
            return false;
        }

        try {
            if (rs.next()){
                Log.logStatus("doesUserExist : Username or Email address exists", '+');
                return true;
            } else {
                Log.logStatus("doesUserExist : Username or Email Address does not exists", '-');
                return false;
            }
        } catch (SQLException e) {
            Log.log("doesUserExist : Exception >" + e.getMessage());
            return false;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("doesUserExist : Exception >" + e.getMessage());
            }
        }
    }

    /*
        false : error
        true : changed password success
     */
    public static boolean changePassword(String username, String newPassword) {
        String sql = sqlBuilder("SELECT UserId FROM Users WHERE Email = '?'", username);
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("ChangePassword : No result set returned");
            return false;
        }
        try {
            if (rs.next()) {
                Log.logStatus("ChangePassword : Email info found", '+');
                int UserId = rs.getInt(1);
                String hashedPassword = Hash.getSaltedHash(newPassword);
                Log.log("ChangePassword : New Password hash :" + hashedPassword);

                sql = sqlBuilder("UPDATE Users SET Password = '?' WHERE UserId = ?", hashedPassword, UserId);
                executeUpdate(sql);
                return true;
            } else {
                Log.logStatus("ChangePassword : Username not exists so null email info", '-');
                return false;
            }
        } catch (Exception e) {
            Log.log("ChangePassword : No result set returned");
            return false;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("ChangePassword : Exception >" + e.getMessage());
            }
        }
    }

    public static String getUser_Name(String email) {
        String sql = sqlBuilder("SELECT Firstname, Surname FROM Users WHERE Email = '?'", email);
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("getUser_Name : No result et returned");
            return null;
        }

        try {
            if (rs.next()) {
                Log.log("getUser_name : First and last name found");
                String firstname = rs.getString(1);
                String lastname = rs.getString(2);
                return firstname + " " + lastname;
            } else {
                Log.log("getUser_name : No user firstname and lastname");
                return null;
            }
        } catch (SQLException e) {
            Log.log("getUser_name : Exception >" + e.getMessage());
            return null;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("getUser_name : Exception >" + e.getMessage());
            }
        }
    }
}
