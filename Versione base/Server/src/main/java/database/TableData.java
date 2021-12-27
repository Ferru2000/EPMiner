package database;

import database.TableSchema.Column;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Questa classe gestisce le transizioni nella tabella.
 */
public class TableData {

    private Connection connection;

    /**
     * Costruttore che inizializza il gestore della connessione.
     * @param connection Gestore della connessione
     */
    public TableData(Connection connection) {
        this.connection = connection;
    }

    /**
     * Classe interna che gestisce le tuple della tabella.
     */
    public class TupleData {
        public List<Object> tuple = new ArrayList<Object>();

        /**
         * Metodo che restituisce in una stringa le tuple della tabella.
         * @return Stringa contenente le tuple della tabella
         */
        public String toString() {
            String value = "";
            Iterator<Object> it = tuple.iterator();
            while (it.hasNext())
                value += (it.next().toString() + " ");

            return value;
        }
    }

    /**
     * Metodo che restituisce lo schema della tabella e le sue tuple.
     * @param table Nome della tabella
     * @return Lista di transizioni della tabella
     * @throws SQLException Se vi e' una eccezione SQL
     */
    public List<TupleData> getTransazioni(String table) throws SQLException {
        LinkedList<TupleData> transSet = new LinkedList<TupleData>();
        Statement statement;
        TableSchema tSchema = new TableSchema(table, connection);


        String query = "select ";

        for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
            Column c = tSchema.getColumn(i);
            if (i > 0)
                query += ",";
            query += c.getColumnName();
        }
        if (tSchema.getNumberOfAttributes() == 0)
            throw new SQLException();
        query += (" FROM " + table);

        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            TupleData currentTuple = new TupleData();
            for (int i = 0; i < tSchema.getNumberOfAttributes(); i++)
                if (tSchema.getColumn(i).isNumber())
                    currentTuple.tuple.add(rs.getFloat(i + 1));
                else
                    currentTuple.tuple.add(rs.getString(i + 1));
            transSet.add(currentTuple);
        }
        rs.close();
        statement.close();


        return transSet;

    }

    /**
     * Metodo che esegue una query sql per estrarre i valori distinti ordinati di column e
     * popola un insieme da restituire.
     * @param table Nome della tabella
     * @param column Nome della colonna
     * @return Lista di valori distinti della colonna specificata
     * @throws SQLException Se vi e' una eccezione SQL
     */
    public List<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        LinkedList<Object> valueSet = new LinkedList<Object>();
        Statement statement;
        TableSchema tSchema = new TableSchema(table, connection);

        String query = "select distinct ";

        query += column.getColumnName();

        query += (" FROM " + table);

        query += (" ORDER BY " + column.getColumnName());


        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            if (column.isNumber())
                valueSet.add(rs.getFloat(1));
            else
                valueSet.add(rs.getString(1));

        }
        rs.close();
        statement.close();

        return valueSet;

    }

    /**
     * Metodo che esegue una query sql per estrarre il valore aggregato cercato nella colonna di
     * nome column della tabella di nome table.
     * @param table Nome della tabella
     * @param column Nome della colonna
     * @param aggregate Operatore SQL di aggregazione(MIN, MAX)
     * @return Insieme di valori distinti per l'attributo identificato dal nome della colonna
     * @throws SQLException Se vi e' una eccezione SQL
     * @throws NoValueException Se vi e' una eccezione NoValueException
     */
    public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws SQLException, NoValueException {
        Statement statement;
        TableSchema tSchema = new TableSchema(table, connection);
        Object value = null;
        String aggregateOp = "";

        String query = "select ";
        if (aggregate == QUERY_TYPE.MAX)
            aggregateOp += "max";
        else
            aggregateOp += "min";
        query += aggregateOp + "(" + column.getColumnName() + ") FROM " + table;


        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            if (column.isNumber())
                value = rs.getFloat(1);
            else
                value = rs.getString(1);

        }
        rs.close();
        statement.close();
        if (value == null)
            throw new NoValueException("No " + aggregateOp + " on " + column.getColumnName());

        return value;

    }
}
