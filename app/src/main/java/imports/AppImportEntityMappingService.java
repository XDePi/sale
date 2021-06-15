package imports;

import lombok.NonNull;

import java.io.File;
import java.util.Locale;

public interface AppImportEntityMappingService {

    /**
     * import mapping from file
     *
     * @param file        file
     * @param fileLocale  file locale
     * @return import report
     */
    @NonNull
    ImportMappingReport importMapping(@NonNull File file, @NonNull Locale fileLocale);


}

