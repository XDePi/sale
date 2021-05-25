package feed.exports;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
