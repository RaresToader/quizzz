


package commons;

import java.util.ArrayList;
import java.util.List;

public class RandomSelection {
    private List<QuizzQuestion> listOfQuestions;
    int index = 0;

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
    public RandomSelection(){
        Long num = (long) 2214321;
        Activity act1 = new Activity("d","lol","what",num,"what source");
        Activity act2 = new Activity("r","lol","yo",num,"wahhg");
        listOfQuestions = new ArrayList<>();
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act1,act2,act1));
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act2,act1,act2));
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act1,act2,act1));



    }

    public List<QuizzQuestion> getListOfQuestions() {
        return listOfQuestions;
    }

    public void setListOfQuestions(List<QuizzQuestion> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }

    public QuizzQuestion next(){
        this.index++;
        return this.listOfQuestions.get(index-1);
    }
    public boolean hasNext(){
        if(index>=this.listOfQuestions.size()){
            return false;
        }
        return true;
    }
    public int getIndex(){
        return index;
    }
}








