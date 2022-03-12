package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {
    private Activity activity = new Activity("test", "test", 10, "test", "path");

    @Test
    public void constructorTest() {
        assertNotNull(activity);
    }

    @Test
    public void getTitleTest() {
        assertEquals(activity.getTitle(), "test");
    }

    @Test
    public void getConsumptionTest() {
        assertEquals(10, activity.getConsumption_in_wh());
    }

    @Test
    public void getSourceTest() {
        assertEquals(activity.getSource(), "test");
    }

    @Test
    public void equalsTest1() {
        assertEquals(activity, activity);
    }

    @Test
    public void equalsTest2() {
        Activity activity2 = new Activity("test", "test", 10, "test", "path");
        assertEquals(activity, activity2);

    }

    @Test
    public void equalsTest3() {
        Activity activity2 = new Activity("test2", "test2", 100, "test2", "path");
        assertNotEquals(activity, activity2);
    }

    @Test
    public void setTitleTest() {
        activity.setTitle("new");
        assertEquals("new", activity.getTitle());
    }

    @Test
    public void setConsumptionTest() {
        activity.setConsumption_in_wh(500);
        assertEquals(activity.getConsumption_in_wh(), 500);
    }

    @Test
    public void setSourceTest() {
        activity.setSource("new");
        assertEquals(activity.getSource(), "new");
    }

    @Test
    public void toStringTest() {
        String result = "Activity{id='test', title='test', consumption_in_wh=10, source='test', image_path='path'}";
        assertEquals(activity.toString(), result);
    }
}
