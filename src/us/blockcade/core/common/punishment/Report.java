package us.blockcade.core.common.punishment;

import java.util.UUID;

public class Report {

    private String against;
    private String issuer;
    private String hack;
    private String date;
    private String reason;

    public Report(String against, String issuer, String hack, String date, String reason) {
        this.against = against;
        this.issuer = issuer;
        this.hack = hack;
        this.date = date;
        this.reason = reason;
    }

    public UUID getFiledAgainst() { return UUID.fromString(against); }
    public UUID getWhoFiled() { return UUID.fromString(issuer); }
    public String getHack() { return hack; }
    public String getDate() { return date; }
    public String getReason() { return reason; }

}
