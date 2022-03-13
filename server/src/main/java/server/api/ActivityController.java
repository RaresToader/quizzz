package server.api;

import commons.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.util.List;


@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityRepository activityRepository;

    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }


    @GetMapping("/all")
    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Activity> addActivity(@RequestBody Activity toBeAdded) {
        if (toBeAdded == null || toBeAdded.getSource() == null || toBeAdded.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }
        Activity newActivity = activityRepository.save(toBeAdded);
        return ResponseEntity.ok(newActivity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable long id) {
        if (this.activityRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(this.activityRepository.findById(id).get());
    }

    @DeleteMapping("/all")
    public ResponseEntity<Activity> deleteAllActivities() {
        this.activityRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Activity> deleteActivityByObject(@RequestBody Activity toBeDeleted) {
        if (toBeDeleted == null) {
            return ResponseEntity.badRequest().build();
        }
        activityRepository.delete(toBeDeleted);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Activity> deleteActivityById(@PathVariable long id) {
        if (activityRepository.findById(id).isEmpty())
            return ResponseEntity.badRequest().build();
        activityRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/modify")
    public ResponseEntity<Activity> modifyActivity(@RequestBody Activity activity) {

        if (activity == null || activityRepository.findById(activity.getId()).isEmpty() || activity.getTitle() == null || activity.getSource() == null)
            return ResponseEntity.badRequest().build();
        Activity toBeModified = activityRepository.getById(activity.getId());
        toBeModified.setConsumption_in_wh(activity.getConsumption_in_wh());
        toBeModified.setSource(activity.getSource());
        toBeModified.setTitle(activity.getTitle());
        Activity saved = activityRepository.save(toBeModified);
        return ResponseEntity.ok(saved);
    }

}
