package us.blockcade.core.util.math;

public class TimeUtil {

    public static String asTimeString(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        String time = days + "d " + hours % 24 + "h " + minutes % 60 + "m " + seconds % 60 + "s";
        return time;
    }

}
