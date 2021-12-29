package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{

        LoginScene.getLoginScene().setParameterStage(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }

    /*@Override
    public void handle(ActionEvent event)
    {
        if(event.getSource() == submitButton)
            buildUI();
    }*/
}
