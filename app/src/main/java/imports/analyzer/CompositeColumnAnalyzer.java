package imports.analyzer;

import imports.converter.EntityConverter;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CompositeColumnAnalyzer<DataRow, Delta> implements ColumnAnalyzer<DataRow, Delta> {

    private final List<ColumnAnalyzer<DataRow, Delta>> analyzers;

    @NonNull
    @Override
    public List<EntityConverter<DataRow, Delta>> analyze(@NonNull List<String> columnNames) {
        List<EntityConverter<DataRow, Delta>> result = new ArrayList<>();
        for (ColumnAnalyzer<DataRow, Delta> analyzer : analyzers) {
            result.addAll(analyzer.analyze(columnNames));
        }
        return result;
    }
}

