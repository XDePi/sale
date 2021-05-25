package feed.exports.impl;

import com.depi.sale.entity.Sale;
import com.depi.sale.repository.SaleRepository;
import feed.excel.export.AbstractMarshaller;
import feed.excel.export.ExcelMarshaller;
import feed.exports.AppExportService;
import feed.exports.sale.ExportSaleMappingContentResolver;
import feed.exports.sale.impl.ExportSaleMappingContentResolverImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            List<Sale> sales = saleRepository.findAll();
            marshaller.marshallSheet(ExportSaleMappingContentResolverImpl.TAB_NAME, saleMappingContentResolver,
                    sales.iterator());
            marshaller.finalizeWritings();
            tempFile = marshaller.getFile();
        }
        return tempFile;
    }
}

