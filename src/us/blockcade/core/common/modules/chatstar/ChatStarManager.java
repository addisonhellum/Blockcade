package us.blockcade.core.common.modules.chatstar;

import org.bukkit.Bukkit;
import us.blockcade.core.util.sql.BlockcadeSql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class ChatStarManager {

    public static void createChatStarTable() {
        String query = "CREATE TABLE IF NOT EXISTS chat_star ("
                + " uuid VARCHAR(60) NOT NULL,"
                + " id INTEGER )";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasChatStarRecord(UUID uuid) {
        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet result = st.executeQuery("SELECT * FROM chat_star WHERE uuid='" + uuid.toString() + "'");

            return result.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void registerUser(UUID uuid) {
        String query = "INSERT INTO chat_star (uuid,id) VALUES ('" + uuid + "', 0)";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ChatStar getChatStar(UUID uuid) {
        String query = "select * from chat_star where uuid='" + uuid + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.next();
            return getChatStar(rs.getInt("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ChatStar.GRAY;
    }

    public static void setChatStar(UUID uuid, int id) {
        String query = "update chat_star set id=" + id + " where uuid='" + uuid + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ChatStar getChatStar(int id) {
        for (ChatStar star : ChatStar.values())
            if (star.getId() == id) return star;
        return ChatStar.GRAY;
    }

}
