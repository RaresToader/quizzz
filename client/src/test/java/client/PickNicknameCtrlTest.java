package client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PickNicknameCtrlTest {
    @Test
    public void emptyConstructorTest() {
        PickNicknameCtrl x = new PickNicknameCtrl();
        assertNotNull(x);
    }

    @Test
    public void setNicknameTest() {
        PickNicknameCtrl x = new PickNicknameCtrl();
        x.setNickname("test");
        assertEquals("test",x.getNickname());
    }

    //Couldn't test nicknameConstructor as that demanded init of NicknameTextField
}
