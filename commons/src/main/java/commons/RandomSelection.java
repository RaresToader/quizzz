


package commons;

import java.util.ArrayList;
import java.util.List;

public class RandomSelection {
    private List<QuizzQuestion> listOfQuestions;

    public RandomSelection(List<QuizzQuestion> database) {   //have to pass the database somehow
        listOfQuestions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            listOfQuestions.add(QuizzQuestion.randomQuestion(database));
        }
    }

    public List<QuizzQuestion> getListOfQuestions() {
        return listOfQuestions;
    }

    public void setListOfQuestions(List<QuizzQuestion> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }
}








