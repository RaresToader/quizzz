package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsteadOfQuestionTest {
    private final Activity test = new Activity("test", "test", "test", 0L, "test");
    private final Activity test2 = new Activity("test2", "test2", "test2", 0L, "test2");
    private final Activity test3 = new Activity("test3", "test3", "test3", 0L, "test3");
    private final Activity test4 = new Activity("test4", "test4", "test4", 0L, "test4");

    @Test
    public void defaultConstructorTest() {
        var question = new InsteadOfQuestion();
        assertNotNull(question);
    }

    @Test
    public void constructorTest() {
        var question = new InsteadOfQuestion(test, test2, test3, test4);
        assertNotNull(question);
    }

    @Test
    public void getQuestionTest() {
        var question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question.getQuestion(), "Instead of..., you could do instead...");
    }

    @Test
    public void getPromptActivityTest() {
        var question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question.getPromptActivity(), test);
    }

    @Test
    public void getFirstChoiceTest() {
        var question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question.getFirstChoice(), test2);
    }

    @Test
    public void getSecondChoiceTest() {
        var question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question.getSecondChoice(), test3);
    }

    @Test
    public void getThirdChoiceTest() {
        var question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question.getThirdChoice(), test4);
    }

    @Test
    public void testToString() {
        var question = new InsteadOfQuestion(test, test2, test3, test4);
        String testString = "InsteadOfQuestion{question='Instead of..., you could do instead...', promptActivity=Activity{id='test', title='test', consumption_in_wh=0, source='test', image_path='test'}, firstChoice=Activity{id='test2', title='test2', consumption_in_wh=0, source='test2', image_path='test2'}, secondChoice=Activity{id='test3', title='test3', consumption_in_wh=0, source='test3', image_path='test3'}, thirdChoice=Activity{id='test4', title='test4', consumption_in_wh=0, source='test4', image_path='test4'}}";
        assertEquals(question.toString(), testString);
    }

    @Test
    public void testEqualsTest1() {
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question, question);
    }

    @Test
    public void testEqualsTest2() {
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        InsteadOfQuestion question2 = new InsteadOfQuestion(test2, test, test3, test4);
        assertNotEquals(question2, question);
    }

    @Test
    public void testEqualsTest3() {
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        InsteadOfQuestion question2 = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question, question2);
    }
    @Test
    public void testEqualsTest4() {
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        assertNotEquals(question, "test");
    }
    @Test
    public void testEqualsTest5() {
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        InsteadOfQuestion question2 = new InsteadOfQuestion(test, test, test3, test4);
        assertNotEquals(question2, question);
    }
    @Test
    public void testEqualsTest6() {
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        InsteadOfQuestion question2 = new InsteadOfQuestion(test, test2, test, test4);
        assertNotEquals(question2, question);
    }
    @Test
    public void testEqualsTest7() {
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        InsteadOfQuestion question2 = new InsteadOfQuestion(test, test2, test3, test2);
        assertNotEquals(question2, question);
    }
    @Test
    public void getCorrectQuestionTest(){
        test.setConsumption_in_wh(100);
        test2.setConsumption_in_wh(120);
        test3.setConsumption_in_wh(200);
        test4.setConsumption_in_wh(50);
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question.getCorrectChoice(),test2);
    }
    @Test
    public void getCorrectQuestionTest2(){
        test.setConsumption_in_wh(100);
        test2.setConsumption_in_wh(120);
        test3.setConsumption_in_wh(110);
        test4.setConsumption_in_wh(50);
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question.getCorrectChoice(),test3);
    }
    @Test
    public void getCorrectQuestionTest3(){
        test.setConsumption_in_wh(100);
        test2.setConsumption_in_wh(120);
        test3.setConsumption_in_wh(200);
        test4.setConsumption_in_wh(98);
        InsteadOfQuestion question = new InsteadOfQuestion(test, test2, test3, test4);
        assertEquals(question.getCorrectChoice(),test4);
    }
}