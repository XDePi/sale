package feed.exports;


import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;

public interface AppExportService {
    /**
     * export all mappings to file
     * @return export file
     * @throws IOException exception
     */
    File exportMappings(Long from, Pageable pageable) throws IOException;
}
