import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {

    private static final int PORT = 8080;

    private MultiServer() throws IOException{
        run();
    }

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

    public static void main(String[] args) throws IOException {
         new MultiServer();
    }

}
