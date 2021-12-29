package app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginScene {

    private String address;
    private String port;

    private TextField addressText,portText;
    private Stage stage;
    private AnchorPane anchorPane;
    private Button submitButton, defaultButton;
    private static LoginScene instance = null;

    public static LoginScene getLoginScene(){
        if(instance == null)
            instance = new LoginScene();
        return instance;
    }

    private LoginScene(){

    }

    public void setParameterStage(Stage primaryStage) {
        stage = primaryStage;

        anchorPane = new AnchorPane();

        // TextField
        addressText = new TextField();
        portText = new TextField();

        addressText.setPromptText("inserisci l'indirizzo");
        portText.setPromptText("inserisci il numero di porta");

        addressText.setAlignment(Pos.CENTER);
        portText.setAlignment(Pos.CENTER);

        addressText.setFocusTraversable(false);
        portText.setFocusTraversable(false);

        addressText.setPrefWidth(180);
        portText.setPrefWidth(180);

        AnchorPane.setTopAnchor(addressText, (double) 100 );
        AnchorPane.setLeftAnchor(addressText, (double) 105 );
        AnchorPane.setTopAnchor(portText, (double) 150 );
        AnchorPane.setLeftAnchor(portText, (double) 105 );

        // Buttons
        submitButton = new Button("Invio");
        submitButton.setAlignment(Pos.CENTER);
        submitButton.setFocusTraversable(false);

        AnchorPane.setTopAnchor(submitButton, (double) 250 );
        AnchorPane.setLeftAnchor(submitButton, (double) 175 );

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                address = addressText.getText();
                port = portText.getText();

                if(check()) {
                    MainScene.getMainScene().buildUI();
                    stage.close();
                }

            }
        });


        defaultButton = new Button("Usa impostazioni di default");
        defaultButton.setAlignment(Pos.CENTER);
        defaultButton.setFocusTraversable(false);

        AnchorPane.setTopAnchor(defaultButton, (double) 200 );
        AnchorPane.setLeftAnchor(defaultButton, (double) 115 );

        defaultButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                addressText.setText("localhost");
                portText.setText("8080");
            }
        });

        anchorPane.getChildren().addAll(addressText, portText, submitButton, defaultButton);

        Scene scene = new Scene(anchorPane, 400, 400);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }

    private boolean check() {
        if(address.isEmpty() || port.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Attenzione, è necessario compilare entrambi i campi");
            alert.show();
            return false;
        }
        return true;
    }

}
