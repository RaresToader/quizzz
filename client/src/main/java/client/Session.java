package client;


import commons.Activity;
import commons.Joker;
import commons.QuizzQuestion;
import commons.QuizzQuestionServerParsed;

import java.util.ArrayList;

public class Session {

    private static String nickname;
    private static String serverAddr;
    private static int questionNum;

    public static QuizzQuestionServerParsed emptyQ = new QuizzQuestionServerParsed(new QuizzQuestion("0",new Activity("0","0","0",Long.valueOf(0),"0"),new Activity("0","0","0",Long.valueOf(0),"0"),new Activity("0","0","0",Long.valueOf(0),"0")),-1,-1, new ArrayList<Joker>());


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
