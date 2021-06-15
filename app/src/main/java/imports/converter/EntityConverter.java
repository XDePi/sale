package imports.converter;

import imports.ReportMessage;

import java.util.List;

/**
 * A EntityConverter interface represents builder mechanism  for Delta  object of imported  entity. (<tt>CommonImportDeltaPojo</tt> )
 *
 * Each end-point implementation provides building of some part of the built Delta.
 * Collection of end-point implementations provides full conversion of input data to Delta object
 * @param <DataRow> input data type that must be transformed to Delta. For example : JSON , xml object  structure or simple List of String
 * @param <Delta> type of target Delta object. For example : <tt>PosImportDelta</tt> <tt>ProductImportDeltaPojo</tt>
 */
public interface EntityConverter<DataRow, Delta> {

    List<ReportMessage> convert(DataRow dataRow, Delta delta);
}
