package app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Questa classe avvia l'interfaccia grafica dell'utente per l'inserimento dei parametri
 * del server.
 */
public class Main extends Application{

    /**
     * Metodo che fa partire l'interfaccia grafica per l'inserimento dei parametri del server.
     * @param primaryStage Stage principale del programma
     * @throws Exception Eccezione generale
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        LoginScene.getLoginScene().buildLoginScene(primaryStage);

    }

    /**
     * Metodo di avvio del programma client.
     * @param args Stringa che contiene indirizzo e porta del server, se esso viene avviato
     *             tramite parametri.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
