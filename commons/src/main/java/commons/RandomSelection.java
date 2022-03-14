


package commons;

import java.util.ArrayList;
import java.util.List;

public class RandomSelection {
    private List<QuizzQuestion> listOfQuestions;

    /**
     * From 60 randomly selected activities, creates 20 quizzz questions
     * @param database the list of activities from the database
     */
    public RandomSelection(List<Activity> database) {
        listOfQuestions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            //Question is hardcoded right now
            listOfQuestions.add(new QuizzQuestion("What activity costs more?",database.get(3*i),
                    database.get(3*i+1),database.get(3*i+2)));
        }
    }

    public List<QuizzQuestion> getListOfQuestions() {
        return listOfQuestions;
    }

    public void setListOfQuestions(List<QuizzQuestion> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }
}








