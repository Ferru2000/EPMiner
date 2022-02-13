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

import java.util.Objects;

/**
 * Questa classe gestisce l'interfaccia utente per l'inserimento dei parametri
 * del server.
 */
public class LoginScene {

    private String address;
    private String port;

    private TextField addressText,portText;
    private Stage stage;
    private AnchorPane anchorPane;
    private Button submitButton, defaultButton;
    private static LoginScene instance = null;

    /**
     * Metodo che restituisce il singleton della classe.
     * @return Singleton della classe
     */
    public static LoginScene getLoginScene(){
        if(instance == null)
            instance = new LoginScene();
        return instance;
    }

    /**
     * Costruttore privato.
     */
    private LoginScene(){

    }

    /**
     * Metodo che costruisce l'interfaccia per l'inserimento dei parametri.
     * @param primaryStage Stage su cui inserire la scena
     */
    public void buildLoginScene(Stage primaryStage) {
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
                    MainScene.getMainScene().buildMainScene();
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
        String css = Objects.requireNonNull(this.getClass().getResource("Application.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("EPMiner");
        stage.show();
    }

    /**
     * Metodo che restituisce l'indirizzo inserito nel TextField addressText.
     * @return Indirizzo presente nel TextField addressText
     */
    public String getAddress() {
        return address;
    }

    /**
     * Metodo che restituisce la porta inserita nel TextField portText.
     * @return Porta presente nel TextField portText
     */
    public String getPort() {
        return port;
    }

    /**
     * Metodo che controlla se entrambi i campia siano stati compilati.
     * @return Booleano che indica se entrambi i campi sono compilati o meno
     */
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
