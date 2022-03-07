package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class QuestionScreenCtrl {

    public String chosenAnswer;
    public String correctAnswer;
    public int points = 0;

    @FXML
    private Button firstChoice;

    @FXML
    private Button secondChoice;

    @FXML
    private Button thirdChoice;

    
    public void chooseFirst(){
        chosenAnswer = firstChoice.toString();
        setBackground(firstChoice, secondChoice, thirdChoice);
    }

    public void chooseSecond(){
        chosenAnswer = secondChoice.toString();
        setBackground(secondChoice, firstChoice, thirdChoice);
    }

    public void chooseThird(){
        chosenAnswer = thirdChoice.toString();
        setBackground(thirdChoice, firstChoice, secondChoice);
    }

    // Changes the color of the background so the player can see if they answered the question correctly
    public void setBackground(Button yourAnswer, Button nextAnswer, Button lastAnswer){
        if(chosenAnswer.equals(correctAnswer)){
            yourAnswer.setStyle("-fx-background-color: green;");
        }
        else{
            yourAnswer.setStyle("-fx-background-color: red;");
            if(correctAnswer.equals(nextAnswer.toString())){
                nextAnswer.setStyle("-fx-background-color: green;");
            }
            else{
                lastAnswer.setStyle("-fx-background-color: green;");
            }
        }
    }
}
