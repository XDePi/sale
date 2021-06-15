package imports.analyzer;

import imports.converter.EntityConverter;
import lombok.NonNull;
import java.util.List;

public interface ColumnAnalyzer <DataRow, Delta> {

    @NonNull List<EntityConverter<DataRow, Delta>> analyze(@NonNull List<String> columnNames);
}
