


package commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSelection {
    private List<QuizzQuestion> listOfQuestions;
    int index = 0;

    /**
     * From 60 randomly selected activities, creates 20 quizzz questions
     * @param database the list of activities from the database
     */
    public RandomSelection(List<Activity> database) {
        Random random = new Random();
        System.out.println(database.size());
        listOfQuestions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int type = random.nextInt(2);

            if(type == 0) {
                listOfQuestions.add(new QuizzQuestion("What activity costs more?", database.get(3 * i),
                        database.get(3 * i + 1), database.get(3 * i + 2), 0));
            }

            if(type == 1) {
                int correct = random.nextInt(3);

                listOfQuestions.add(new QuizzQuestion("How much does this cost: " + database.get(3 * i + correct).getTitle(), database.get(3 * i),
                        database.get(3 * i + 1), database.get(3 * i + 2), 1));
            }
        }
    }
    public RandomSelection(){
        Long num = (long) 2214321;
        Activity act1 = new Activity("d","lol","what",num,"what source");
        Activity act2 = new Activity("r","lol","yo",num,"wahhg");
        listOfQuestions = new ArrayList<>();
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act1,act2,act1,0));
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act2,act1,act2,0));
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act1,act2,act1,0));



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








