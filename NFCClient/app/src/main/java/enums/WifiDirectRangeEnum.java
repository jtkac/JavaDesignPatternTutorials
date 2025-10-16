package enums;

public enum WifiDirectRangeEnum {
    MINPORT(8099L),
    MAXPORT(8199L);

    WifiDirectRangeEnum(Long port) {
        this.port = port;
    }

    private Long port;

    public Long getPort() {
        return port;
    }
}
