package app.feed.excel.exports.impl;

import com.depi.sale.entity.Sale;
import com.depi.sale.repository.SaleRepository;
import feed.excel.export.AbstractMarshaller;
import feed.excel.export.ExcelContentResolver;
import feed.exports.AppExportService;
import feed.exports.impl.AppExportServiceImpl;
import feed.exports.sale.ExportSaleMappingContentResolver;
import feed.exports.sale.impl.ExportSaleMappingContentResolverImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppExportServiceImplTest {

    @Mock
    private ExportSaleMappingContentResolver saleMappingContentResolver;
    @Mock
    private SaleRepository saleRepository;

    private AppExportService service;

    private final TestMarshaller marshaller = new TestMarshaller();

    @BeforeEach
    void init() {
        service = new AppExportServiceImpl(saleMappingContentResolver, saleRepository) {
            @Override
            public AbstractMarshaller getMarshaller() {
                return marshaller;
            }
        };
    }

    @Test
    void exportSaleMappingContentResolverImpl_calls_export_entity_service() throws IOException {
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setDate(new Date());
        sale.setCustomerName("TESTED");
        sale.setAmount(BigDecimal.TEN);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        Page<Sale> salesPage = new PageImpl<>(sales, pageable, sales.size());

        when(saleRepository.findByIdGreaterThan(0L, pageable)).thenReturn(salesPage);
        service.exportMappings();

        Iterator<Sale> saleIterator = marshaller.getRenderedSheets().get(ExportSaleMappingContentResolverImpl.TAB_NAME);

        assertEquals(sales.get(0), saleIterator.next(), "Sale tab filled");
    }

    private static class TestMarshaller extends AbstractMarshaller {
        private final Map<String, Iterator> renderedSheets = new HashMap<>(4);

        @Override
        public <ObjectType> void marshallSheet(String sheetName, ExcelContentResolver<ObjectType> excelContentResolver, Iterator<ObjectType> iterator) {
            renderedSheets.put(sheetName, iterator);
        }

        @Override
        public void finalizeWritings() {
        }

        @Override
        public File getFile() {
            return null;
        }

        @Override
        public void close() {
        }

        Map<String, Iterator> getRenderedSheets() {
            return renderedSheets;
        }
    }
}
