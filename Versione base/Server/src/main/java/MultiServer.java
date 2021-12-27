import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Questa classe rappresenta il server e gestisce le nuove connessioni con i client.
 */
public class MultiServer {

    private static final int PORT = 8080;

    /**
     * Costruttore del MultiServer.
     * @throws IOException Se vi e' una eccezione di input/output
     */
    private MultiServer() throws IOException{
        run();
    }

    /**
     * Metodo che gestisce le nuove connessioni creando un nuovo thread.
     * @throws IOException Se vi e' una eccezione di input/output
     */
    private void run() throws IOException{
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server avviato");
        try {
            while(true) {
                Socket socket = s.accept();
                System.out.println("Connessione di socket " + socket);
                System.out.println("Nuovo client connesso");
                try {
                    new ServerOneClient(socket);
                } catch(IOException e) {
                    socket.close();
                }
            }
        } finally {
            s.close();
        }

    }

    /**
     * Metodo di avvio del server.
     * @throws IOException Se vi e' una eccezione di input/output
     */
    public static void main(String[] args) throws IOException {
         new MultiServer();
    }

}
