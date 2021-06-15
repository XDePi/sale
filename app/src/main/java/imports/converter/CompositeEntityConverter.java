package imports.converter;

import imports.ReportMessage;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CompositeEntityConverter<DataRow, Delta> implements EntityConverter<DataRow, Delta> {

    private final List<EntityConverter<DataRow, Delta>> converters;

    @Override
    public List<ReportMessage> convert(DataRow dataRow, Delta delta) {
        List<ReportMessage> result = new ArrayList<>();
        for (EntityConverter<DataRow, Delta> converter : converters) {
            result.addAll(converter.convert(dataRow, delta));
        }
        return result;
    }
}
