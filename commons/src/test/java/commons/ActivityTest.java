package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {
    @Test
    public void constructorTest() {
        Activity activity = new Activity("test", 10, "test");
        assertNotNull(activity);
    }

    @Test
    public void getTitleTest() {
        Activity activity = new Activity("test", 10, "test");
        assertEquals(activity.getTitle(), "test");
    }

    @Test
    public void getConsumptionTest() {
        Activity activity = new Activity("test", 10, "test");
        assertEquals(10, activity.getConsumption_in_wh());
    }

    @Test
    public void getSourceTest() {
        Activity activity = new Activity("test", 10, "test");
        assertEquals(activity.getSource(), "test");
    }

    @Test
    public void equalsTest1() {
        Activity activity = new Activity("test", 10, "test");
        assertEquals(activity, activity);
    }

    @Test
    public void equalsTest2() {
        Activity activity = new Activity("test", 10, "test");
        Activity activity2 = new Activity("test", 10, "test");
        assertEquals(activity, activity2);

    }

    @Test
    public void equalsTest3() {
        Activity activity = new Activity("test", 10, "test");
        Activity activity2 = new Activity("test2", 100, "test2");
        assertNotEquals(activity, activity2);
    }

    @Test
    public void setTitleTest() {
        Activity activity = new Activity("test", 10, "test");
        activity.setTitle("new");
        assertEquals("new", activity.getTitle());
    }

    @Test
    public void setConsumptionTest() {
        Activity activity = new Activity("test", 10, "test");
        activity.setConsumption_in_wh(500);
        assertEquals(activity.getConsumption_in_wh(), 500);
    }

    @Test
    public void setSourceTest() {
        Activity activity = new Activity("test", 10, "test");
        activity.setSource("new");
        assertEquals(activity.getSource(), "new");
    }
    @Test
    public void toStringTest(){
        Activity activity = new Activity("test", 10, "test");
        String result ="Activity{title='test', consumption_in_wh=10, source='test'}";
        assertEquals(activity.toString(),result);
    }
}
