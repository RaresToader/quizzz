package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {
    private Activity activity = new Activity("test", "test", "test", 10l, "test");

    @Test
    public void constructorTest() {
        assertNotNull(activity);
    }

    @Test
    public void emptyConstructorTest() {
        Activity x = new Activity();
        assertNotNull(x);
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
    public void equalsTest4() {
        assertFalse(activity.equals(null));
    }

    @Test
    public void equalsTest2() {
        Activity activity2 = new Activity("test", "test", "test", 10l, "test");
        assertEquals(activity, activity2);
    }

    @Test
    public void equalsTest3() {
        Activity activity2 = new Activity("test2", "test", "test", 10l, "path");
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
    public void setIdTest() {
        Activity x = new Activity("test2", "test", "test", 10l, "path");
        x.setId("test");
        assertEquals("test",x.getId());
    }

    @Test
    public void toStringTest() {
        String result = "Activity{id='test', title='test', consumption_in_wh=10, source='test', image_path='test'}";
        assertEquals(activity.toString(), result);
    }
}
