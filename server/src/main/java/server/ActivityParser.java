package server;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.FileReader;
import java.io.IOException;

public class ActivityParser {

    private final JSONParser jsonParser;

    public ActivityParser(JSONParser parser) {
        this.jsonParser = parser;
    }

    /**
     * This will parse all the activities and create a list of activities.
     */
    public void activityParse(){
        try {
            JSONArray array = (JSONArray) jsonParser.parse(new FileReader("server/src/main/resources/activity-bank/76/activities.json"));
            /**
             * listOfActivities.add(new Activity(title,consumption,source));
             * in the end returns the list of activities, or saves them locally
             * on the repository
             */
            for (Object o : array) {

                JSONObject object = (JSONObject) o;
                String title = (String) object.get("title");
                long consumption = (long) object.get("consumption_in_wh");
                String source = (String) object.get("source");
                /**
                 * listOfActivities.add(new Activity(title,consumption,source));
                 * in the end returns the list of activities, or saves them locally
                 * on the repository
                 */
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
