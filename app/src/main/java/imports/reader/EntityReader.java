package imports.reader;

import imports.ReportMessage;

import java.util.List;

/**
 * A EntityReader interface represents mechanism for extracting particular data  from  external data with DataRow type
 * <p>
 * It is used with <tt>BaseEntityConverter</tt>.
 *
 * @param <DataRow> input data type that must be transformed to Delta. For example : JSON , xml object  structure or simple List of String
 * @param <Entity>  type of structure for delivery data from  EntityReader to  Converter
 */
public interface EntityReader<DataRow, Entity> {

    Entity read(DataRow dataRow, List<ReportMessage> messages);
}
