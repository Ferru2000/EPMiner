package app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Questa classe gestisce l'interfaccia utente per l'inserimento dei dati della richiesta
 * da inviare al server.
 */
public class MainScene {
    private RadioButton radioOption_1, radioOption_2;
    private TextField minSupTextField, minGrowTextField, targetTextField,backgroundTextField;
    private TextArea frequentPatternText,emergingPatternText;
    private Stage stage;
    private static QueryClass queryClass;
    private static MainScene instance = null;

     /**
     * Metodo che restituisce il singleton della classe.
     * @return Singleton della classe
     */
    public static MainScene getMainScene(){
        if(instance == null) {
            instance = new MainScene();
            queryClass = new QueryClass();
        }

        return instance;
    }

    /**
     * Costruttore privato.
     */
    private MainScene(){

    }

    /**
     * Metodo che costruisce l'interfaccia per l'inserimento dei dati da inviare al server.
     */
    void buildMainScene() {
        // gridPane setup
        stage = new Stage();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10,10 ,10 ,10));

        // textArea
        frequentPatternText = new TextArea();
        emergingPatternText = new TextArea();

        frequentPatternText.setEditable(false);
        emergingPatternText.setEditable(false);

        frequentPatternText.setPrefSize(500,500);
        emergingPatternText.setPrefSize(500,500);

        GridPane.setMargin(frequentPatternText, new Insets(-400,0,0,200));
        GridPane.setMargin(emergingPatternText, new Insets(-400,0,0,0));

        GridPane.setConstraints(frequentPatternText, 2, 5);
        GridPane.setConstraints(emergingPatternText, 3, 5);

        // Buttons
        Button queryButton = new Button("Avvia ricerca");

        GridPane.setMargin(queryButton, new Insets(20,0,0,0));

        GridPane.setHalignment(queryButton, HPos.CENTER);
        GridPane.setValignment(queryButton, VPos.CENTER);

        GridPane.setConstraints(queryButton, 0, 3);

        queryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    queryClass.initializeConnection(LoginScene.getLoginScene().getAddress(), LoginScene.getLoginScene().getPort());
                    if(check()) {
                        if(!queryClass.executeQuery()) {
                            frequentPatternText.setText("");
                            emergingPatternText.setText("");
                            alertError("Attenzione, qualcosa e' andato storto");
                        }
                    }

                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        Button backButton = new Button("Back");

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                queryClass.closeConnection();
                stage.close();
                LoginScene.getLoginScene().buildLoginScene(new Stage());
            }
        });

        GridPane.setMargin(backButton, new Insets(0,0,0,0));

        GridPane.setHalignment(backButton, HPos.RIGHT);
        GridPane.setValignment(backButton, VPos.CENTER);

        GridPane.setConstraints(backButton, 0, 4);

        // Radio buttons and toggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();

        radioOption_1 = new RadioButton("Nuova scoperta");
        radioOption_2 = new RadioButton("Risultati in archivio");

        radioOption_1.setToggleGroup(toggleGroup);
        radioOption_2.setToggleGroup(toggleGroup);

        radioOption_1.setSelected(true);

        GridPane.setMargin(radioOption_1, new Insets(0,0,0,0));
        GridPane.setMargin(radioOption_2, new Insets(50,0,0,0));

        GridPane.setConstraints(radioOption_1, 0, 0);
        GridPane.setConstraints(radioOption_2, 0, 0);

        // Labels
        Label titleLabel = new Label("EP MINER");
        titleLabel.setId("titleLabel");
        GridPane.setMargin(titleLabel, new Insets(0,0,50,0));

        GridPane.setConstraints(titleLabel, 0, 0);

        Label minSupLabel = new Label("minimo supporto");
        Label minGrowLabel = new Label("minimo grow rate");

        GridPane.setMargin(minSupLabel, new Insets(0,0,0,0));
        GridPane.setMargin(minGrowLabel, new Insets(60,0,0,0));

        GridPane.setConstraints(minSupLabel, 0, 1);
        GridPane.setConstraints(minGrowLabel, 0, 1);

        Label targetLabel = new Label("Tabella target");
        Label backgroundLabel = new Label("Tabella background");

        GridPane.setMargin(targetLabel, new Insets(0,0,0,0));
        GridPane.setMargin(backgroundLabel, new Insets(120,0,0,0));

        GridPane.setHalignment(targetLabel, HPos.CENTER);
        GridPane.setValignment(targetLabel, VPos.CENTER);

        GridPane.setHalignment(backgroundLabel, HPos.CENTER);
        GridPane.setValignment(backgroundLabel, VPos.CENTER);

        GridPane.setConstraints(targetLabel, 0, 2);
        GridPane.setConstraints(backgroundLabel, 0, 2);

        // Textfield
        minSupTextField = new TextField("");
        minGrowTextField = new TextField("");

        GridPane.setMargin(minSupTextField, new Insets(0,0,0,100));
        GridPane.setMargin(minGrowTextField, new Insets(60,0,0,100));

        GridPane.setConstraints(minSupTextField, 0, 1);
        GridPane.setConstraints(minGrowTextField, 0, 1);

        minSupTextField.setPrefSize(50, 20);
        minGrowTextField.setPrefSize(50, 20);

        minSupTextField.setMaxSize(100, 20);
        minGrowTextField.setMaxSize(100, 20);

        minSupTextField.setAlignment(Pos.CENTER);
        minGrowTextField.setAlignment(Pos.CENTER);

        targetTextField = new TextField("");
        backgroundTextField = new TextField("");

        //targetTextField.setMaxSize(350, 20);
        //backgroundTextField.setMaxSize(350, 20);

        targetTextField.setAlignment(Pos.CENTER);
        backgroundTextField.setAlignment(Pos.CENTER);

        GridPane.setHalignment(targetTextField, HPos.CENTER);
        GridPane.setValignment(targetTextField, VPos.CENTER);

        GridPane.setHalignment(backgroundTextField, HPos.CENTER);
        GridPane.setValignment(backgroundTextField, VPos.CENTER);

        GridPane.setMargin(targetTextField, new Insets(50,0,0,0));
        GridPane.setMargin(backgroundTextField, new Insets(170,0,0,0));

        GridPane.setConstraints(targetTextField, 0, 2);
        GridPane.setConstraints(backgroundTextField, 0, 2);

        gridPane.getChildren().addAll(radioOption_1, radioOption_2, minSupLabel, minGrowLabel,
                minSupTextField, minGrowTextField, targetLabel, backgroundLabel,
                targetTextField, backgroundTextField, queryButton, titleLabel, frequentPatternText, emergingPatternText, backButton);

        // Constraints
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        gridPane.getColumnConstraints().add(column1);

        /*RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.NEVER);
        gridPane.getRowConstraints().add(row1);
        */

        Scene scene = new Scene(gridPane, 800, 800);
        //String css = this.getClass().getResource("application.css").toExternalForm();
        //scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.setTitle("MAP Project");
        stage.show();
        stage.setMaximized(true);
    }

    /**
     * Metodo che controlla che tutti i campi non siano vuoti e il formato dei dati inserito sia corretto.
     * @return Booleano che indica se tutti i campi sono stati compilati in modo corretto o meno
     */
    private boolean check() {
        double number;

        try{
            number = Double.parseDouble(minSupTextField.getText());
            if(number <= 0 || number > 1) {
                alertError("Attenzione, il valore minimo di supporto deve essere compreso nell'intervallo (0, 1]");
                return false;
            }
            number = Double.parseDouble(minGrowTextField.getText());
            if(number < 1) {
                alertError("Attenzione, il valore minimo di grow rate deve essere >= 1");
                return false;
            }
        }catch (NumberFormatException e) {
            alertError("Attenzione, assicurati che il formato dei valori sia il seguente:" +
                    "\n[numero].[numero] se il numero e' decimale" +
                    "\n[numero] se il numero e' intero");
            return false;
        }

        if(targetTextField.getText().isEmpty() || backgroundTextField.getText().isEmpty()) {
            alertError("Attenzione, compilare i campi delle tabelle");
            return false;
        }

        return true;
    }

    private void alertError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setContentText(text);
        alert.show();
    }

    /**
     * Metodo che restituisce il TextField del minimo supporto.
     * @return TextField del minimo supporto
     */
    public TextField getMinSupTextField() {
        return minSupTextField;
    }

    /**
     * Metodo che restituisce il TextField del minimo grow rate.
     * @return TextField del minimo supporto
     */
    public TextField getMinGrowTextField() {
        return minGrowTextField;
    }

    /**
     * Metodo che restituisce il TextField della tabella target.
     * @return TextField della tabella target
     */
    public TextField getTargetTextField() {
        return targetTextField;
    }

    /**
     * Metodo che restituisce il TextField della tabella di background.
     * @return TextField della tabella di background
     */
    public TextField getBackgroundTextField() {
        return backgroundTextField;
    }

    /**
     * Metodo che restituisce il RadioButton corrispondente alla scelta Nuova Scoperta.
     * @return RadioButton della scelta Nuova Scoperta
     */
    public RadioButton getRadioOption_1() {
        return radioOption_1;
    }

    /**
     * Metodo che restituisce la TextArea relativa ai pattern frequenti.
     * @return TextArea dei pattern frequenti
     */
    public TextArea getFrequentPatternText() {
        return frequentPatternText;
    }

    /**
     * Metodo che restituisce la TextArea relativa ai pattern emergenti.
     * @return TextArea dei pattern emergenti
     */
    public TextArea getEmergingPatternText() {
        return emergingPatternText;
    }
}
