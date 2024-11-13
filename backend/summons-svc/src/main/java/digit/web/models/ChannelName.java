package digit.web.models;

public enum ChannelName {

    POLICE("Police"), EPOST("Post"), SMS("Sms"), EMAIL("Email"), RPAD("Rpad");

    String channelName;

    ChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public static ChannelName fromString(String channelName) {
        for (ChannelName b : ChannelName.values()) {
            if (b.channelName.equalsIgnoreCase(channelName)) {
                return b;
            }
        }
        return null;
    }
}
