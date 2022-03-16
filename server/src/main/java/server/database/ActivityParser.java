package server.database;


import commons.Activity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStreamReader;

@Configuration
public class ActivityParser {


    @Bean
    public CommandLineRunner parseActivities(ActivityRepository activityRepository) {
        return args -> {
            JSONParser parser = new JSONParser();
            /**
             * Use relative path!
             */
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(ActivityParser.class.getResourceAsStream("/activities.json")));
            for(Object object:array){
                JSONObject helper = (JSONObject) object;
                String id = (String) helper.get("id");
                String image_path = (String) helper.get("image_path");
                String title = (String) helper.get("title");
                Long cost = (Long) helper.get("consumption_in_wh");
                String source = (String) helper.get("source");
                activityRepository.save(new Activity(id,image_path,title,cost,source));
            }
        };
    }

}

