package imports;

import com.depi.sale.entity.Sale;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;

public interface ImportMappingSimpleReportBuilder {
    File buildReport(@NonNull Sale sale) throws IOException;
}
