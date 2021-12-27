package database;

/**
 * Questa classe gestisce un'eccezione di tipo DatabaseConnectionException.
 */
public class DatabaseConnectionException extends Exception {
	DatabaseConnectionException(String msg){
		super(msg);
	}
}
