package us.blockcade.core.common.punishment;

import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;

import java.util.UUID;

public class Punishment {

    private int id = -1;
    private UUID punished;
    private PunishmentType type;
    private UUID punisher;
    private String reason;
    private long issued;
    private long expires;
    private boolean expired;

    public Punishment(UUID punished, UUID punisher, PunishmentPreset preset) {
        this.punished = punished;
        this.type = preset.getPunishmentType();
        this.punisher = punisher;
        this.reason = preset.getReason();
        this.issued = System.currentTimeMillis();
        this.expires = preset.getDuration().asTimeFromNow();
        this.expired = false;
    }

    public Punishment(UUID punished, PunishmentType type, UUID punisher, String reason, PunishmentDuration duration) {
        this.punished = punished;
        this.type = type;
        this.punisher = punisher;
        this.reason = reason;
        this.issued = System.currentTimeMillis();
        this.expires = duration.asTimeFromNow();
        this.expired = false;
    }

    public Punishment(int id, UUID punished, PunishmentType type, UUID punisher, String reason, long issued, long expires, boolean expired) {
        this.id = id;
        this.punished = punished;
        this.type = type;
        this.punisher = punisher;
        this.reason = reason;
        this.issued = issued;
        this.expires = expires;
        this.expired = expired;
    }

    public enum PunishmentType {
        KICK, MUTE, BAN;
    }

    public int getId() { return id; }
    public BPlayer getPunished() {
        return BlockcadeUsers.getBPlayer(punished);
    }
    public BPlayer getPunisher() {
        return BlockcadeUsers.getBPlayer(punisher);
    }
    public PunishmentType getType() {
        return type;
    }
    public String getReason() {
        return reason;
    }
    public long getWhenIssued() {
        return issued;
    }
    public long getWhenExpires() {
        return expires;
    }
    public boolean isExpired() {
        return expired;
    }

    public long getDuration() {
        return getWhenExpires() - getWhenIssued();
    }

    public long getTimeRemaining() {
        if (isExpired()) return -1;
        return getWhenExpires() - System.currentTimeMillis();
    }

    public void execute() {
        PunishmentManager.executePunishment(this);
    }

    public static PunishmentType getType(String label) {
        for (PunishmentType type : PunishmentType.values())
            if (type.name().equalsIgnoreCase(label)) return type;
        return null;
    }

}
