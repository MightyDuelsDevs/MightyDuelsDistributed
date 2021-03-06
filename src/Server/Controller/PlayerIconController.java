/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import Server.Database.Database;
import Shared.Domain.Icon;
import Server.Domain.Player;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Ramòn Janssen
 */
public class PlayerIconController {

    static private List<Icon> icons;
    static private int ratingMultiplier = 25;

    /**
     * Initialise the PlayerIcon Controller
     */
    public static void playerIconControllerInit() {
        icons = new ArrayList<>();
        createIcons();
    }

    /**
     * Function that gets all existing icons from the database This function
     * fills the List of Icon 'icons'
     */
    private static void createIcons() {
        String statement = "SELECT * FROM ICON";

        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);

                for (List<String> column : resultSet) {
                    int id = Integer.parseInt(column.get(0));
                    int ratingLock = Integer.parseInt(column.get(1));
                    String fileName = column.get(2);

                    icons.add(new Icon(id, ratingLock, fileName));
                }
            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Change the icon of an player in the database
     *
     * @param playerID the id of the player
     * @param iconID the id of the icon the player wants
     * @return true if the update was successful.
     */
    public static boolean changePlayerIcon(int playerID, int iconID) {
        String statement = "UPDATE PLAYER SET ICONID = " + iconID + " WHERE ID = " + playerID;
        try {
            if (Database.checkConnection()) {
                Database.DMLRecordIntoTable(statement);
            } else {
                System.out.println("Database connection is lost.");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Function to check if a displayname already exists
     *
     * @param displayname input username for the player
     * @return boolean true if there is nu existing player with the same
     * displayname. False if there is an existing player.
     */
    private static boolean checkIfPlayerExists(String displayname) {
        String statement = "SELECT * FROM PLAYER WHERE DISPLAYNAME = '" + displayname.toUpperCase() + "'";
        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);
                if (resultSet.isEmpty()) {
                    return true;
                }
            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Function to sign up
     *
     * @param email
     * @param displayname
     * @param password
     * @param passcheck
     * @return Returns an integer that has the following values: 0 = unexpected
     * error. 1 = passcheck is not the same as password. 2 = username already in
     * use. 3 = successfully created a new player!
     */
    public static int signUpPlayer(String email, String displayname, String password, String passcheck) {
        String statement = "INSERT INTO PLAYER(ID, ICONID, EMAIL, DISPLAYNAME, PASSWORD, RATING, MATCHES, WINS, LOSSES, SELDECKID) VALUES (null, 1, '"
                + email + "','" + displayname.toUpperCase() + "','" + hashGenerator(password) + "',1200,0,0,0,1)";
        try {
            if (Database.checkConnection()) {
                if (!password.equals(passcheck)) {
                    return 1;
                }
                if (Database.checkConnection()) {
                    if (checkIfPlayerExists(displayname.toUpperCase())) {
                        Database.DMLRecordIntoTable(statement);
                        return 3;
                    } else {
                        System.out.println("Displayname is already in use!");
                        return 2;
                    }
                }
            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return 0;
    }

    /**
     * Function to Login
     *
     * @param displayname
     * @param password
     * @return Player the player information from the person that just logged
     * in.
     */
    public static Player logInPlayer(String displayname, String password) {
        Player player = null;
        String statement = "SELECT * FROM PLAYER WHERE DISPLAYNAME = '" + displayname.toUpperCase() + "' AND PASSWORD = '" + hashGenerator(password) + "'";
        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);
                if (!resultSet.isEmpty()) {
                    List<String> column = resultSet.get(0);

                    String username = column.get(3);
                    int id = Integer.parseInt(column.get(0));
                    int iconId = Integer.parseInt(column.get(1));
                    int rating = Integer.parseInt(column.get(5));
                    int wins = Integer.parseInt(column.get(7));
                    int losses = Integer.parseInt(column.get(8));
                    int matches = Integer.parseInt(column.get(6));

                    player = new Player(id, username, iconId, rating, wins, losses, matches);
                }
            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return player;
    }

    /**
     * Creates a new player object from the database and puts it in a list for
     * future reference
     *
     * @param displayname
     * @return the instance of the player
     */
    public static Player createPlayer(String displayname) {
        Player player = null;
        String statement = "SELECT * FROM PLAYER WHERE DISPLAYNAME = '" + displayname.toUpperCase() + "'";
        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);

                List<String> column = resultSet.get(0);

                String username = column.get(3);
                int id = Integer.parseInt(column.get(0));
                int iconId = Integer.parseInt(column.get(1));
                int rating = Integer.parseInt(column.get(5));
                int wins = Integer.parseInt(column.get(7));
                int losses = Integer.parseInt(column.get(8));
                int matches = Integer.parseInt(column.get(6));

                player = new Player(id, username, iconId, rating, wins, losses, matches);

            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return player;
    }

    /**
     * Creates a new player object from the database and puts it in a list for
     * future reference
     *
     * @param playerId
     * @return the instance of the player
     */
    public static Player createPlayerWithId(int playerId) {
        Player player = null;
        String statement = "SELECT * FROM PLAYER WHERE ID = " + playerId;
        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);

                List<String> column = resultSet.get(0);

                String username = column.get(3);
                int id = Integer.parseInt(column.get(0));
                int iconId = Integer.parseInt(column.get(1));
                int rating = Integer.parseInt(column.get(5));
                int wins = Integer.parseInt(column.get(7));
                int losses = Integer.parseInt(column.get(8));
                int matches = Integer.parseInt(column.get(6));

                player = new Player(id, username, iconId, rating, wins, losses, matches);

            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return player;
    }

    /**
     * Function to get all the current unlocked icons
     *
     * @param rating Current rating of the player
     * @return Returns all unlocked icons according to rating
     */
    public static List<Icon> getIcons(int rating) {
        List<Icon> unlockedIcons = new ArrayList<>();
        unlockedIcons.addAll(icons.stream().filter((i) -> i.getRatingLock() <= rating).collect(Collectors.toList()));
//        for (Icon icon : icons) {
//            if (icon.getRatingLock() <= rating) {
//                unlockedIcons.add(icon);
//            }
//        }
        return unlockedIcons;
    }

                /**
     * Function that updates the rating of two players in the database.
     *
     * @param playerOneId, The ID of the first player.
     * @param playerTwoId, The ID of the second player.
     * @param playerOneWon, true if the first player won.
     * @return returns true if the update was successful.
     */
    public static boolean updateRating(int playerOneId, int playerTwoId, boolean playerOneWon) {
        String statementOne;
        String statementTwo;
        String statementThree;
        String statementFour;

        Player playerOne = createPlayerWithId(playerOneId);
        Player playerTwo = createPlayerWithId(playerTwoId);

        int ratingDifferenceWon = (int) ((1 - 0.5 / (1 + (10 * Math.abs(playerTwo.getRating() - playerOne.getRating())) / 400)) * ratingMultiplier);
        
        if (playerOneWon) {

            if(playerOne.getRating() >= playerTwo.getRating())
            {
                statementOne = String.format("UPDATE PLAYER SET RATING = %1$s WHERE ID = %2$s", playerOne.getRating() + (ratingMultiplier - ratingDifferenceWon), playerOneId);
                statementTwo = String.format("UPDATE PLAYER SET RATING = %1$s WHERE ID = %2$s", Math.max(800, playerTwo.getRating() - (ratingMultiplier - ratingDifferenceWon)), playerTwoId);
                statementThree = String.format("UPDATE PLAYER SET WINS = WINS + 1 WHERE ID = %1$s", playerOne.getId());
                statementFour = String.format("UPDATE PLAYER SET LOSSES = LOSSES + 1 WHERE ID = %1$s", playerTwo.getId());
            } else {
                statementOne = String.format("UPDATE PLAYER SET RATING = %1$s WHERE ID = %2$s", playerOne.getRating() + ratingDifferenceWon, playerOneId);
                statementTwo = String.format("UPDATE PLAYER SET RATING = %1$s WHERE ID = %2$s", Math.max(800, playerTwo.getRating() - ratingDifferenceWon), playerTwoId);
                statementThree = String.format("UPDATE PLAYER SET WINS = WINS + 1 WHERE ID = %1$s", playerOne.getId());
                statementFour = String.format("UPDATE PLAYER SET LOSSES = LOSSES + 1 WHERE ID = %1$s", playerTwo.getId());
            }
        } else {

            if(playerOne.getRating() <= playerTwo.getRating())
            {
                statementOne = String.format("UPDATE PLAYER SET RATING = %1$s WHERE ID = %2$s", playerTwo.getRating() + (ratingMultiplier - ratingDifferenceWon), playerTwoId);
                statementTwo = String.format("UPDATE PLAYER SET RATING = %1$s WHERE ID = %2$s", Math.max(800, playerOne.getRating() - (ratingMultiplier - ratingDifferenceWon)), playerOneId);
                statementThree = String.format("UPDATE PLAYER SET WINS = WINS + 1 WHERE ID = %1$s", playerTwo.getId());
                statementFour = String.format("UPDATE PLAYER SET LOSSES = LOSSES + 1 WHERE ID = %1$s", playerOne.getId());
            } else {
                statementOne = String.format("UPDATE PLAYER SET RATING = %1$s WHERE ID = %2$s", playerTwo.getRating() + ratingDifferenceWon, playerTwoId);
                statementTwo = String.format("UPDATE PLAYER SET RATING = %1$s WHERE ID = %2$s", Math.max(800, playerTwo.getRating() - ratingDifferenceWon), playerOneId);
                statementThree = String.format("UPDATE PLAYER SET WINS = WINS + 1 WHERE ID = %1$s", playerTwo.getId());
                statementFour = String.format("UPDATE PLAYER SET LOSSES = LOSSES + 1 WHERE ID = %1$s", playerOne.getId());
            }
        }

        try {
            if (Database.checkConnection()) {
                Database.DMLRecordIntoTable(statementOne);
                Database.DMLRecordIntoTable(statementTwo);
                Database.DMLRecordIntoTable(statementThree);
                Database.DMLRecordIntoTable(statementFour);
            } else {
                System.out.println("Database connection is lost.");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Function that hashes an input string into a string.
     * @param hashableValue, The value of the hashable string.
     * @return A hashed string.
     */
    public static String hashGenerator(String hashableValue) {
        StringBuilder sb = new StringBuilder();
        byte[] data = hashByteGenerator(hashableValue);
        //convert to an hex string with leading zero's
        for (byte d : data) {
            String hex = Integer.toHexString(0xff & d);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * Function that hashes an input string into a byte array.
     * @param hashableValue, The value of the hashable string.
     * @return A hashed byte array.
     */
    public static byte[] hashByteGenerator(String hashableValue) {
        //source:
        //http://stackoverflow.com/a/25243174/2675935
        try {
            //digest the password with MDPass as salt so existing databases are useless for decryption
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(("MDPass" + hashableValue).getBytes("UTF-16"));
            byte[] data = md.digest();

            return data;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            //schould never occure, if so the application runs in an unsupported Java VM or Operating system
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, "The algorithm or charset is not found!", ex);
            return null;
        }
    }
}
