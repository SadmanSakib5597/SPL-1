package ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import questionEngine.Option;
import questionEngine.Question;


import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerController implements Initializable {

    @FXML
    AnchorPane rootPane;
    @FXML
    Label questionName;
    @FXML
    Label time;
    @FXML
    AnchorPane optionHolder;
    @FXML
    Label errormsg;
    @FXML
    Button submit;
    @FXML
    Button next;

    @FXML
    AnchorPane status;
    @FXML
    Label correct;
    @FXML
    Label wrong;


    public static Question question;
    public static int correctAnswer;
    public static int wrongAnswer;
    AtomicInteger numOfQus = new AtomicInteger(0);
    AtomicInteger numOfOp = new AtomicInteger(0);
    VBox layout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        status.setVisible(false);
        setQuestion();

    }

    public void setQuestion()
    {
        layout = new VBox();
        errormsg.setText("");

        questionName.setText(question.question.get(numOfQus.get()) + " ?");
        Option opList = question.option.get(numOfQus.get());

        ToggleGroup groupOfRadio = new ToggleGroup();

        layout.setSpacing(15);

        for(int i=0; i<question.numberOfOption; i++)
        {
            RadioButton radio = new RadioButton(opList.option.get(i));
            radio.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
            radio.setUserData(opList.option.get(i));
            radio.setToggleGroup(groupOfRadio);
            layout.getChildren().add(radio);
        }

        optionHolder.getChildren().add(layout);



        groupOfRadio.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(groupOfRadio.getSelectedToggle() != null)
                {
                    layout.setDisable(true);
                    String answer = groupOfRadio.getSelectedToggle().getUserData().toString();

                    if(answer.equals(question.answer.get(numOfQus.get())))
                    {
                        errormsg.setText("*Correct answer.");
                        errormsg.setStyle("-fx-text-fill: green;");
                        correctAnswer++;
                    }
                    else
                    {
                        errormsg.setText("*Wrong answer!");
                        errormsg.setStyle("-fx-text-fill: red;");
                        wrongAnswer++;
                    }

                }
            }
        });
    }

    public void setNext(ActionEvent event)
    {
        if(numOfQus.get() == question.numberOfQuestion-1)
        {
            layout.getChildren().clear();
            layout.setDisable(false);
            questionName.setText("");

            status.setVisible(true);
            correct.setText(correctAnswer+"");
            wrong.setText((question.numberOfQuestion-correctAnswer)+"");

            next.setText("Done");

            next.setOnAction(e3->{
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/ui/chooser.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root);
                Stage currWindow = (Stage)rootPane.getScene().getWindow();
                currWindow.setScene(scene);
            });


        }
        else {
            layout.getChildren().clear();
            layout.setDisable(false);
            numOfQus.incrementAndGet();
            setQuestion();
        }

    }
}