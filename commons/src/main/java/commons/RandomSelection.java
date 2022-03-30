


package commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomSelection {
    private List<Question> listOfQuestions;
    int index = 0;

    /**
     * From 60 randomly selected activities, creates 20 quizzz questions
     *
     * @param database the list of activities from the database
     */
    public RandomSelection(List<Activity> database) {
        Random random = new Random();
        Collections.shuffle(database);
        System.out.println(database.size());
        listOfQuestions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int type = random.nextInt(4);

            /**
             * If the type is 0, create a question of type "QuizzQuestion"
             */
            if (type == 0) {
                listOfQuestions.add(new QuizzQuestion("What activity costs more?", database.get(3 * i),
                        database.get(3 * i + 1), database.get(3 * i + 2)));
                database.remove(3*i);
                database.remove(3*i+1);
                database.remove(3*i+2);
            }

            /**
             * If the type is 1, create a question of type "ConsumpQuestion", with the correct answer put in a random
             * slot and the other two options randomly generated close to the correct answer,
             * rounded to a multiple of 5
             */
            else if (type == 1) {
                long correct = database.get(3 * i).getConsumption_in_wh();
                double lower = correct * 0.7;
                double higher = correct * 1.3;
                long temp = (long) (random.nextInt((int) (higher - lower)) + lower);
                long next = temp - (temp % 5);
                while(next == correct || next == 0) {
                    next += 5;
                }
                long temp2 = (long) (random.nextInt((int) (higher - lower)) + lower);
                long last = temp2 - (temp2 % 5);
                while(last == correct || last == next || last == 0) {
                    last += 5;
                }
                int version = random.nextInt(3);
                if (version == 0) {
                    listOfQuestions.add(new ConsumpQuestion("How much energy does this cost?", database.get(3 * i), correct, next, last));
                }

                if (version == 1) {
                    listOfQuestions.add(new ConsumpQuestion("How much energy does this cost?", database.get(3 * i), next, correct, last));
                }

                if (version == 2) {
                    listOfQuestions.add(new ConsumpQuestion("How much energy does this cost?", database.get(3 * i), next, last, correct));

                }
                database.remove(3*i);
            }

            /**
             * If the type is 2, create a question of type "GuessQuestion"
             */
            else if (type == 2) {
                listOfQuestions.add(new GuessQuestion("Guess the cost of the activity", database.get(3 * i)));
                database.remove(3*i);
            }
            else{
                listOfQuestions.add(new InsteadOfQuestion(database.get(3*i),database.get(3*i+1),database.get(3*i+2),database.get(3*i+3)));
                database.remove(3*i);
                database.remove(3*i+1);
                database.remove(3*i+2);
                database.remove(3*i+3);
            }
        }
    }

    public RandomSelection() {
        Long num = (long) 2214321;
        Activity act1 = new Activity("d", "lol", "what", num, "what source");
        Activity act2 = new Activity("r", "lol", "yo", num, "wahhg");
        listOfQuestions = new ArrayList<>();
        listOfQuestions.add(new QuizzQuestion("What activity costs more?", act1, act2, act1));
        listOfQuestions.add(new QuizzQuestion("What activity costs more?", act2, act1, act2));
        listOfQuestions.add(new QuizzQuestion("What activity costs more?", act1, act2, act1));


    }

    public List<Question> getListOfQuestions() {
        return listOfQuestions;
    }

    public void setListOfQuestions(List<Question> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }

    public Question next() {
        this.index++;
        return this.listOfQuestions.get(index - 1);
    }

    public boolean hasNext() {
        if (index >= this.listOfQuestions.size()) {
            return false;
        }
        return true;
    }

    public int getIndex() {
        return index;
    }
}








