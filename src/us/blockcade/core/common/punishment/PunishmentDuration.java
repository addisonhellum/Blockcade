package us.blockcade.core.common.punishment;

public enum PunishmentDuration {

    NO_DURATION(0L),
    MINUTES_30(1800000L), MINUTES_60(3600000L), HOURS_3(10800000L),
    HOURS_6(21600000L), HOURS_12(43200000L), DAYS_1(86400000L),
    DAYS_2(172800000L), DAYS_3(259200000L), DAYS_5(432000000L),
    DAYS_7(604800000L), DAYS_12(1036800000L), DAYS_14(1209600000L),
    DAYS_21(1814400000L), MONTHS_1(2592000000L), MONTHS_2(5184000000L),
    MONTHS_6(15552000000L), YEARS_1(31104000000L), PERMANENT(-1);

    private long millis;

    private PunishmentDuration(long millis) {
        this.millis = millis;
    }

    public long asMillis() { return this.millis; }
    public long asSeconds() { return asMillis() / 1000; }
    public long asMinutes() { return asSeconds() / 60; }
    public long asHours() { return asMinutes() / 60; }
    public long asDays() { return asHours() / 24; }

    public long asTimeFromNow() {
        return System.currentTimeMillis() + asMillis();
    }

}
