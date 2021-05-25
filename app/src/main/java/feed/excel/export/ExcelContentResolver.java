package feed.excel.export;

import lombok.NonNull;

import java.util.List;
import java.util.Map;

public interface ExcelContentResolver<ObjectType> {

    /**
     * Create descriptor of exported excel file
     *
     * @return collection of columns descriptors
     */
    @NonNull
    ExcelFeedDescriptor buildDescriptor();

    /**
     * Resolve content of exported row for specified product identifier  <b/>
     *
     * @param item product id
     * @return List of rows to marshall, every row is a map with exported values
     */
    @NonNull
    List<Map<String, Object>> resolveRow(@NonNull ObjectType item);
}
