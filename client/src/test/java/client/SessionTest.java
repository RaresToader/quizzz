package client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {
    @Test
    public void emptyConstructorTest() {
        Session x = new Session();
        assertNotNull(x);
    }

    @Test
    public void setNicknameTest() {
        Session.setNickname("test");
        assertEquals("test",Session.getNickname());
    }

    @Test
    public void setServerAddrTest() {
        Session.setServerAddr("test");
        assertEquals("test",Session.getServerAddr());
    }

    @Test
    public void setQuestionNumTest() {
        Session.setQuestionNum(5);
        assertEquals(5,Session.getQuestionNum());
    }
}
