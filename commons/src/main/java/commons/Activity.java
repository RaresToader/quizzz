package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Activity {
    @Id
    private String id;
    private String title;
    private long consumption_in_wh;
    private String source;
    private String image_path;

    public Activity(String id, String title, long consumption_in_wh, String source, String image_path) {
        this.id = id;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
        this.image_path = image_path;
    }


    /**
     * For the Object Mapper
     */
    private Activity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public void setConsumption_in_wh(long consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return getConsumption_in_wh() == activity.getConsumption_in_wh() && getId().equals(activity.getId()) && getTitle().equals(activity.getTitle()) && getSource().equals(activity.getSource()) && getImage_path().equals(activity.getImage_path());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getConsumption_in_wh(), getSource(), getImage_path());
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", consumption_in_wh=" + consumption_in_wh +
                ", source='" + source + '\'' +
                ", image_path='" + image_path + '\'' +
                '}';
    }
}






