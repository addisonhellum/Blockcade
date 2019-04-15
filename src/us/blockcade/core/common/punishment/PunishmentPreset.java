package us.blockcade.core.common.punishment;

public enum PunishmentPreset {

    HACKS(1, "Use of prohibited modifications.", Punishment.PunishmentType.BAN, PunishmentDuration.MONTHS_1),
    PROFANITY(2, "Targeted or excessive profanity.", Punishment.PunishmentType.MUTE, PunishmentDuration.DAYS_2),
    DISRESPECT(3, "Extreme disrespect toward other players.", Punishment.PunishmentType.MUTE, PunishmentDuration.DAYS_2),
    EXPLOITS(4, "Exploiting glitches/bugs or abuse.", Punishment.PunishmentType.BAN, PunishmentDuration.DAYS_7),
    THREATS(5, "Harassing or threatening behavior.", Punishment.PunishmentType.MUTE, PunishmentDuration.MONTHS_1),
    BYPASS(6, "Bypassing a previous punishment.", Punishment.PunishmentType.BAN, PunishmentDuration.DAYS_7),
    ADVERTISING(7, "Advertising third-party content.", Punishment.PunishmentType.MUTE, PunishmentDuration.DAYS_7),
    DEFAULT_KICK(8, "Kicked for Misconduct.", Punishment.PunishmentType.KICK, PunishmentDuration.NO_DURATION),
    SPAM(9, "Kicked for Spam.", Punishment.PunishmentType.KICK, PunishmentDuration.NO_DURATION);

    private int id;
    private String reason;
    private Punishment.PunishmentType type;
    private PunishmentDuration duration;

    PunishmentPreset(int id, String reason, Punishment.PunishmentType type, PunishmentDuration duration) {
        this.id = id;
        this.reason = reason;
        this.type = type;
        this.duration = duration;
    }

    public int getId() { return id; }
    public String getReason() {
        return reason;
    }
    public Punishment.PunishmentType getPunishmentType() {
        return type;
    }

    public PunishmentDuration getDuration() {
        return duration;
    }

    public static boolean compare(String reason) {
        for(PunishmentPreset p : values()) {
            return p.getReason().equalsIgnoreCase(reason);
        } return false;
    }

}
