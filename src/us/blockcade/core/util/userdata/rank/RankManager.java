package us.blockcade.core.util.userdata.rank;

public class RankManager {

    public static Rank fromString(String rankTitle) throws NullPointerException {
        for (Rank rank : Rank.values()) {
            if (rank.getName().equalsIgnoreCase(rankTitle)) return rank;
        } return null;
    }

}
