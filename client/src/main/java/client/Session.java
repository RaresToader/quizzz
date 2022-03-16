package client;


public class Session {

    private static String nickname;
    private static String serverAddr;
    private static int questionNum;

    public static String getNickname() {
        return Session.nickname;
    }

    public static String getServerAddr() {
        return Session.serverAddr;
    }

    public static int getQuestionNum() {
        return Session.questionNum;
    }

    public static void setNickname(String nickname) {
        Session.nickname = nickname;
    }

    public static void setServerAddr(String serverAddr) {
        Session.serverAddr = serverAddr;
    }

    public static void setQuestionNum(int questionNum) {
        Session.questionNum = questionNum;
    }
}
