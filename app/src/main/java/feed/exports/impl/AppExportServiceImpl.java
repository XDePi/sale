package feed.exports.impl;

import com.depi.sale.repository.SaleRepository;
import feed.excel.export.AbstractMarshaller;
import feed.excel.export.ExcelMarshaller;
import feed.excel.export.IdentitySaleIterator;
import feed.exports.AppExportService;
import feed.exports.sale.ExportSaleMappingContentResolver;
import feed.exports.sale.impl.ExportSaleMappingContentResolverImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class AppExportServiceImpl implements AppExportService {

    @Autowired
    SaleRepository saleRepository;

    private final ExportSaleMappingContentResolver saleMappingContentResolver;

    public AppExportServiceImpl(ExportSaleMappingContentResolver saleMappingContentResolver) {
        this.saleMappingContentResolver = saleMappingContentResolver;
    }

    protected AbstractMarshaller getMarshaller() throws IOException {
        return new ExcelMarshaller();
    }

    @Override
    public File exportMappings() throws IOException {
        File tempFile;
        try (AbstractMarshaller marshaller = getMarshaller()) {
            IdentitySaleIterator saleIterator = new IdentitySaleIterator
                    (saleRepository::findByIdGreaterThan, saleRepository::countByIdGreaterThan);
            marshaller.marshallSheet(ExportSaleMappingContentResolverImpl.TAB_NAME, saleMappingContentResolver,
                    saleIterator);
            marshaller.finalizeWritings();
            tempFile = marshaller.getFile();
        }
        return tempFile;
    }

}

