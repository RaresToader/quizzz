package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Activity {

    @Id
    private String id;
    private String title;
    private Long consumption_in_wh;
    private String source;
    private String image_path;

    public Activity(String id, String image_path, String title,Long consumption_in_wh, String source) {
        this.id = id;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
        this.image_path = image_path;
    }



    /**
     * For the Object Mapper
     */
    public Activity() {

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
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id) && Objects.equals(title, activity.title) && Objects.equals(consumption_in_wh, activity.consumption_in_wh) && Objects.equals(source, activity.source) && Objects.equals(image_path, activity.image_path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, consumption_in_wh, source, image_path);
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






