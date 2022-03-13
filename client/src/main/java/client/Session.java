package client;


public class Session {
    private static String nickname;
    private static String serverAddr;

    public static String getNickname() {
        return Session.nickname;
    }

    public static String getServerAddr() {
        return Session.serverAddr;
    }

    public static void setNickname(String nickname) {
        Session.nickname = nickname;
    }

    public static void setServerAddr(String serverAddr) {
        Session.serverAddr = serverAddr;
    }
}
