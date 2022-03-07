//This shouldn't be here, but I couldn't put it in commons for some reason.

package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private long consumption_in_wh;
    private String source;

    public Activity(String title,long consumption_in_wh,String source) {
        this.title= title;
        this.consumption_in_wh=consumption_in_wh;
        this.source=source;
    }

    /**
     * For the Object Mapper
     */
    private Activity(){

    }
    public String getTitle() {
        return title;
    }

    public long getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public String getSource() {
        return source;
    }

    public long getId(){
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setConsumption_in_wh(long consumption_in_wh) {
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
                "id=" + id +
                ", title='" + title + '\'' +
                ", consumption_in_wh=" + consumption_in_wh +
                ", source='" + source + '\'' +
                '}';
    }
}






