package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Questa classe gestisce l'accesso al DB per la lettura dei dati di training.
 */
public class DbAccess {

	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
	private final String DBMS = "jdbc:mysql";
	private final String SERVER = "localhost";
	private final int PORT = 3306;
	private final String DATABASE = "Map";
	private final String USER_ID = "Student";
	private final String PASSWORD = "map";

	private Connection conn;

	/**
	 * Metodo che inizializza una connessione al DB.
	 * @throws DatabaseConnectionException Se vi è una eccezione durante l'accesso al database
	 */
	public void initConnection() throws DatabaseConnectionException{
		String connectionString =  DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
				+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
		try {
			
				Class.forName(DRIVER_CLASS_NAME).newInstance();
			} 
		catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DatabaseConnectionException(e.toString());
			}
		catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new DatabaseConnectionException(e.toString());
			} 
		catch (ClassNotFoundException e) {
			System.out.println("Impossibile trovare il Driver: " + DRIVER_CLASS_NAME);
			throw new DatabaseConnectionException(e.toString());
		}
		
		try {
			conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
			
		} catch (SQLException e) {
			System.out.println("Impossibile connettersi al DB");
			e.printStackTrace();
			throw new DatabaseConnectionException(e.toString());
		}
		
	}

	/**
	 * Metodo che restitusice il gestore della connessione al database.
	 * @return Gestore della connessione
	 */
	public  Connection getConnection(){
		return conn;
	}

	/**
	 * Metodo che chiude la connessione con il database.
	 */
	public  void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Impossibile chiudere la connessione");
		}
	}

}
