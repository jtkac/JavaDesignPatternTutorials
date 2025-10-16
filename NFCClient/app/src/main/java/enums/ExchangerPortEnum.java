package enums;

public enum ExchangerPortEnum {
    DIRECTORYEXCHANGERDIRECTORYHOSTPORT(4546),
    DIRECTORYEXCHANGERDIRECTORYCLIENTPORT(4547),
    DATAEXCHANGE(4545);

    ExchangerPortEnum(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private int port;
}
