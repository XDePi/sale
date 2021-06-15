package imports.reader;

import imports.ReportMessageSource;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class BaseFlatReader<Entity> implements EntityReader<List<String>, Entity> {

    private final int index;
    protected final String columnName;
    protected final ReportMessageSource reportMessageSource;

    protected String readEntity(List<String> fields) {
        if (index < 0 || index >= fields.size())
            return null;
        String result = fields.get(index);
        return result == null ? null : result.trim();
    }
}
