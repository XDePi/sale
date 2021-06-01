package feed.exports;


import java.io.File;
import java.io.IOException;

public interface AppExportService {
    /**
     * export all mappings to file
     * @return export file
     * @throws IOException exception
     */
    File exportMappings() throws IOException;
}
