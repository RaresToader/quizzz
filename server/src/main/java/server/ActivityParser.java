package server;


import commons.Activity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class ActivityParser {

    private final JSONParser jsonParser;
    /**
     * For some reason relative path doesn't work...
     */
    private final String path = "server/src/main/resources/activity-bank/76/activities.json";

    public ActivityParser(JSONParser parser) {
        this.jsonParser = parser;
    }

    /**
     * This will parse all the activities and create a list of activities.
     */
    public List<Activity> activityParse() {
        List<Activity> list = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) jsonParser.parse(new FileReader(path));
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
                Activity newActivity = new Activity(title, consumption, source);
                list.add(newActivity);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return list;
    }
}
