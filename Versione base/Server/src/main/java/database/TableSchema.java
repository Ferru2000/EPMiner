package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Questa classe gestisce lo schema di una tabella nel database relazionale.
 */
public class TableSchema {
    private Connection connection;

    /**
     * Metodo che gestisce il mapping tra Java-SQL.
     *
     * @param tableName  Nome della tabella nel database
     * @param connection Gestore della connessione al database
     * @throws SQLException Se vi e' una eccezione di tipo SQL
     */
    public TableSchema(String tableName, Connection connection) throws SQLException {
        this.connection = connection;
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
        //http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");

        DatabaseMetaData meta = connection.getMetaData();
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {
            if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
                tableSchema.add(new Column(
                        res.getString("COLUMN_NAME"),
                        mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
                );
        }
        res.close();
    }

	/**
	 * Questa classe gestisce una generica colonna nella tabella.
	 */
	public class Column {
        private String name;
        private String type;

		/**
		 * Costruttore che assegna i valori passati come parametro agli attributi della classe.
		 * @param name Nome della colonna
		 * @param type Tipo della colonna
		 */
		Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

		/**
		 * Metodo che restituisce il nome della colonna.
		 * @return Il nome della colonna
		 */
		public String getColumnName() {
            return name;
        }

		/**
		 * Metodo che verifica se il tipo della colonna è numerica.
		 * @return Booleano che indica se la colonna è di tipo numerica
		 */
		public boolean isNumber() {
            return type.equals("number");
        }

		/**
		 * Metodo che restituisce come stringa l'oggetto Column, concatenando il nome e il tipo
		 * @return Stringa che rappresenta l'oggetto Column
		 */
		public String toString() {
            return name + ":" + type;
        }
    }

    List<Column> tableSchema = new ArrayList<Column>();

	/**
	 * Metodo che restituisce il numero di colonne della tabella.
	 * @return Il numero di colonne della tabella
	 */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

	/**
	 * Metodo che restituisce la colonna indicata dall'indice.
	 * @param index Indice della colonna da restituire
	 * @return Colonna indicata dall'indice
	 */
	public Column getColumn(int index) {
        return tableSchema.get(index);
    }

}

		     


