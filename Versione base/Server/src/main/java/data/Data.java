package data;

import database.*;
import database.TableData.TupleData;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Questa classe gestisce la tabella su cui lavorare.
 */
public class Data {

    private Object data[][];

    private int numberOfExamples;

    private List<Attribute> attributeSet = new LinkedList<Attribute>();

    /**
     * Costruttore che inizializza la tabella con i dati presente nel database.
     * @param tableName Nome della tabella nel database
     * @throws DatabaseConnectionException Se la connessione al database è fallita
     * @throws SQLException Se vi è un errore SQl (come l'inesistenza della tabella)
     * @throws NoValueException Se non vi sono risultati
     */
    public Data(String tableName) throws DatabaseConnectionException, SQLException, NoValueException {
        // open db connection
        DbAccess db = new DbAccess();
        db.initConnection();
        TableSchema tSchema;
        try {
            tSchema = new TableSchema(tableName, db.getConnection());
            TableData tableData = new TableData(db.getConnection());
            List<TupleData> transSet = tableData.getTransazioni(tableName);

            data = new Object[transSet.size()][tSchema.getNumberOfAttributes()];
            for (int i = 0; i < transSet.size(); i++)
                for (int j = 0; j < tSchema.getNumberOfAttributes(); j++) {
                    data[i][j] = transSet.get(i).tuple.get(j);
                }

            numberOfExamples = transSet.size();

            for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
                if (tSchema.getColumn(i).isNumber())
                    attributeSet.add(
                            new ContinuousAttribute(
                                    tSchema.getColumn(i).getColumnName(),
                                    i,
                                    ((Float) (tableData.getAggregateColumnValue(tableName, tSchema.getColumn(i), QUERY_TYPE.MIN))).floatValue(),
                                    ((Float) (tableData.getAggregateColumnValue(tableName, tSchema.getColumn(i), QUERY_TYPE.MAX))).floatValue()
                            )
                    );
                else {
                    // avvalora values con i valori distinti della colonna oridinati in maniera crescente
                    List<Object> valueList = tableData.getDistinctColumnValues(tableName, tSchema.getColumn(i));
                    String values[] = new String[valueList.size()];
                    Iterator it = valueList.iterator();
                    int ct = 0;
                    while (it.hasNext()) {
                        values[ct] = (String) it.next();
                        ct++;
                    }
                    attributeSet.add(new DiscreteAttribute(tSchema.getColumn(i).getColumnName(), i, values));
                }
            }
        } finally {
            db.closeConnection();
        }

    }

    /**
     * Metodo che restituisce il numero di tuple della tabella.
     * @return Numero di tuple della tabella
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /**
     * Metodo che restituisce il numero di attributi della tabella.
     * @return Numero di attribuiti della tabella
     */
    public int getNumberOfAttributes() {
        return attributeSet.size();
    }

    /**
     * Metodo che restituisce il valore presenta nella riga exampleIndex nella colonna attributeIndex
     * @param exampleIndex Indice della riga
     * @param attributeIndex Indice della colonna
     * @return Valore presente nella tabella nella riga exampleIndex nella colonna attributeIndex
     */
    public Object getAttributeValue(int exampleIndex, int attributeIndex) {
        return data[exampleIndex][attributeSet.get(attributeIndex).getIndex()];
    }

    /**
     * Metodo che restituisce l'attributo della tabella presente in posizione index
     * @param index Indice dell'attributo da restituire
     * @return Attributo presente in posizione index
     */
    public Attribute getAttribute(int index) {
        return attributeSet.get(index);
    }

    /**
     * Metodo che restituisce le tuple della tabella concatenate in una stringa.
     * @return Stringa che contiene le tuple della tabella
     */
    public String toString() {
        String value = "";
        for (int i = 0; i < numberOfExamples; i++) {
            value += (i + 1) + ":";
            for (int j = 0; j < attributeSet.size() - 1; j++)
                value += data[i][j] + ",";

            value += data[i][attributeSet.size() - 1] + "\n";
        }
        return value;
    }
}
