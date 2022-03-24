package server.api;

import commons.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityRepository activityRepository;

    /**
     * Constructor for the Activity Controller class
     *
     * @param activityRepository the activity repository
     */
    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Get all activities
     *
     * @return the list of all activities
     */
    @GetMapping("/all")
    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    /**
     * Checks if an activity with the same id already exists
     *
     * @param id the id
     * @return a boolean, true if an activity with that id already exists, false otherwise
     */
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable String id) {
        if (id == null || activityRepository.findById(id).isEmpty()) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/add")
    public ResponseEntity<Activity> addActivity(@RequestBody Activity toBeAdded) {
        if (toBeAdded == null || toBeAdded.getId() == null || toBeAdded.getSource() == null || toBeAdded.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }
        Activity newActivity = activityRepository.save(toBeAdded);
        return ResponseEntity.ok(newActivity);
    }

    /**
     * Get an activity by its id
     *
     * @param id the id of the activity
     * @return the activity, if it exists in the database
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable String id) {
        if (this.activityRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(this.activityRepository.findById(id).get());
    }

    /**
     * Deletes all activities
     *
     * @return a response entity
     */
    @DeleteMapping("/all")
    public ResponseEntity<Activity> deleteAllActivities() {
        this.activityRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    /**
     * Delete an activity
     *
     * @param toBeDeleted the to be deleted Activity
     * @return the deleted activity
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Activity> deleteActivityByObject(@RequestBody Activity toBeDeleted) {
        if (toBeDeleted == null) {
            return ResponseEntity.badRequest().build();
        }
        activityRepository.delete(toBeDeleted);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete activity by its id
     *
     * @param id the id of the activity
     * @return the deleted activity
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Activity> deleteActivityById(@PathVariable String id) {
        if (activityRepository.findById(id).isEmpty())
            return ResponseEntity.badRequest().build();
        activityRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Modify an activity
     *
     * @param activity the new activity
     * @return the new activity
     */
    @PutMapping("/modify")
    public ResponseEntity<Activity> modifyActivity(@RequestBody Activity activity) {

        if (activity == null || activity.getId() == null || activityRepository.findById(activity.getId()).isEmpty() || activity.getTitle() == null || activity.getSource() == null)
            return ResponseEntity.badRequest().build();
        Activity toBeModified = activityRepository.getById(activity.getId());
        toBeModified.setConsumption_in_wh(activity.getConsumption_in_wh());
        toBeModified.setSource(activity.getSource());
        toBeModified.setTitle(activity.getTitle());
        toBeModified.setImage_path(activity.getImage_path());
        activityRepository.deleteById(activity.getId());
        Activity saved = activityRepository.save(toBeModified);
        return ResponseEntity.ok(saved);
    }

    /**
     * Gets a random set of activities
     *
     * @return a list of 60 activities
     */
    @GetMapping("/randomset")
    public ResponseEntity<List<Activity>> get60RandomActivities() {
        List<Activity> list = this.activityRepository.findAll();
        if (list.size() < 60)
            return ResponseEntity.badRequest().build();
        Collections.shuffle(list);
        return ResponseEntity.ok(list.subList(0, 60));
    }

    /**
     * Loads activity from activities.json file and retrieves them
     */
    @GetMapping("/load")
    public ResponseEntity<List<Activity>> loadActivities() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        List<Activity> listOfActivities = new ArrayList<>();
        JSONArray array;
        array = (JSONArray) parser.parse(new InputStreamReader(Objects.requireNonNull(ActivityController.class.getResourceAsStream("/activities.json"))));
        for (Object object : array) {
            JSONObject helper = (JSONObject) object;
            String id = (String) helper.get("id");
            String image_path = (String) helper.get("image_path");
            String title = (String) helper.get("title");
            Long cost = (Long) helper.get("consumption_in_wh");
            String source = (String) helper.get("source");
            Activity activity = new Activity(id, image_path, title, cost, source);
            listOfActivities.add(activity);
            activityRepository.save(activity);
        }
        return ResponseEntity.ok(listOfActivities);
    }

}
