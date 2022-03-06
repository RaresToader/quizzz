//This shouldn't be here, but I couldn't put it in commons for some reason.

package commons;

import java.util.Objects;

public class Activity {
    private String title;
    private int consumption_in_wh;
    private String source;

    public Activity(String title,int consumption_in_wh,String source) {
        this.title= title;
        this.consumption_in_wh=consumption_in_wh;
        this.source=source;
    }

    public String getTitle() {
        return title;
    }

    public int getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public String getSource() {
        return source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setConsumption_in_wh(int consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return consumption_in_wh == activity.consumption_in_wh && Objects.equals(title, activity.title) && Objects.equals(source, activity.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, consumption_in_wh, source);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "title='" + title + '\'' +
                ", consumption_in_wh=" + consumption_in_wh +
                ", source='" + source + '\'' +
                '}';
    }
}






