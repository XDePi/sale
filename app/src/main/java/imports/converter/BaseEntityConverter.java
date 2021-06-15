package imports.converter;

import imports.ReportMessage;
import imports.ReportMessageSource;
import imports.reader.EntityReader;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A simple abstract implementation of <tt>EntityConverter</tt> which delegates
 * reading and parsing data to <tt>EntityReader</tt>
 * <p>
 * Each end-point implementation provides building of some part of the built Delta and don't depend of input data type.
 * <p> The abstract implementation suggests using only one  instant of reader.
 * If EntityReader works with errors or return null, then Delta will not be modified.
 * Example of Delta  : MappedContract or MappedProduct
 *
 * @param <DataRow> input data type that must be transformed to Delta. For example : JSON , xml object  structure or simple List of Strings
 * @param <Delta>   type of target Delta object. For example : <tt>PosImportDelta</tt> <tt>ProductImportDeltaPojo</tt>
 * @param <Entity>  type of structure for delivery data from  EntityReader to  Converter
 */
@RequiredArgsConstructor
public abstract class BaseEntityConverter<DataRow, Delta, Entity> implements EntityConverter<DataRow, Delta> {

    protected final ReportMessageSource reportMessageSource;
    private final EntityReader<DataRow, Entity> reader;

    @Override
    public List<ReportMessage> convert(DataRow dataRow, Delta delta) {
        List<ReportMessage> readMessages = new ArrayList<>();
        Entity entity = reader.read(dataRow, readMessages);

        List<ReportMessage> result = new ArrayList<>();

        if (ReportMessage.hasErrors(readMessages)) {
            result.add(buildReadErrorMessage(readMessages));
            handleBadValue(delta);
        } else {
            if (ReportMessage.hasWarnings(readMessages)) {
                result.add(buildReadWarningMessage(readMessages));
            }
            if (ReportMessage.hasInfos(readMessages)) {
                result.add(buildReadInfoMessage(readMessages));
            }
            if (entity == null) {
                result.addAll(writeNull(delta));
            } else {
                result.addAll(writeEntity(delta, entity));
            }
        }
        return result;
    }

    protected void handleBadValue(Delta delta) {
        // for overriding
    }

    protected Collection<ReportMessage> writeNull(Delta delta) {
        // for overriding
        return Collections.emptyList();
    }

    protected abstract Collection<ReportMessage> writeEntity(Delta delta, Entity entity);

    protected ReportMessage buildReadErrorMessage(List<ReportMessage> messages) {
        String combinedErrorMessages = ReportMessage.combineErrorMessages(messages);
        return reportMessageSource.getErrorMsg("converter.error.attribute.read.has.errors", new String[]{combinedErrorMessages});
    }

    protected ReportMessage buildReadWarningMessage(List<ReportMessage> messages) {
        String combinedErrorMessages = ReportMessage.combineWarningMessages(messages);
        return reportMessageSource.getWarnMsg("converter.waring.attribute.read.has.warings", new String[]{combinedErrorMessages});
    }

    protected ReportMessage buildReadInfoMessage(List<ReportMessage> messages) {
        String combinedErrorMessages = ReportMessage.combineInfoMessages(messages);
        return reportMessageSource.getInfoMsg("converter.info.attribute.read.has.infos", new String[]{combinedErrorMessages});
    }
}
