package us.blockcade.core.util.sql;

import us.blockcade.core.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BlockcadeSql {

    private static Connection connection;

    private static String db_host = Main.getConfiguration().getString("sql-data.host");
    private static int db_port = Main.getConfiguration().getInt("sql-data.port");
    private static String db_database = Main.getConfiguration().getString("sql-data.database");
    private static String db_user = Main.getConfiguration().getString("sql-data.username");
    private static String db_pass = Main.getConfiguration().getString("sql-data.password");

    public static Connection getConnection() {
        if (connection == null) {
            openConnection();
        } else {
            try { connection.close(); }
            catch (SQLException e) { e.printStackTrace(); }
            openConnection();
        } return connection;
    }

    public static synchronized void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + db_host + ":" + db_port + "/" + db_database,
                    db_user, db_pass);
            //System.out.println("Connection successfully established to BLOCKCADE DATABASE on port " + db_port + ". " +
            //        "Listening for additional operations.");
        } catch (Exception e) {
            System.out.println("[!] Connection to the BLOCKCADE DATABASE failed! Please check the sql-data in the " +
                    "Blockcade.jar core plugin's configuration file.");
            System.out.println(dataToString());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println("Failed to close connection to BLOCKCADE DATABASE. Perhaps it was never established.");
        }
    }

    public static String dataToString() {
        String data = "\n\nDATABASE CONNECTION INFO.\n";
        data += "Host:     " + db_host + "\n";
        data += "Port:     " + db_port + "\n";
        data += "Database: " + db_database + "\n";
        data += "Username: " + db_user + "\n";
        data += "Password: " + db_pass.substring(0, 5) + "*****\n";
        return data;
    }
}
