package feed.exports.sale.impl;

import com.depi.sale.entity.Sale;
import feed.SaleColumnKey;
import feed.excel.export.ExcelColumnDescriptor;
import feed.excel.export.ExcelFeedDescriptor;
import feed.exports.sale.ExportMerchantProductKeyResolver;
import feed.exports.sale.ExportSaleMappingContentResolver;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExportSaleMappingContentResolverImpl implements ExportSaleMappingContentResolver {
    public static final String TAB_NAME = "Sales";

    @NonNull
    @Override
    public ExcelFeedDescriptor buildDescriptor() {
        ExcelFeedDescriptor result = new ExcelFeedDescriptor();
        result.addColumn(SaleColumnKey.SALE_ID.getName(), ExcelColumnDescriptor.CellType.LONG, true);
        result.addColumn(SaleColumnKey.DATE.getName(), ExcelColumnDescriptor.CellType.STRING);
        result.addColumn(SaleColumnKey.CUSTOMER_NAME.getName(), ExcelColumnDescriptor.CellType.STRING);
        result.addColumn(SaleColumnKey.AMOUNT.getName(), ExcelColumnDescriptor.CellType.FLOAT2);
        return result;
    }

    @Override
    @NonNull
    public List<Map<String, Object>> resolveRow(@NonNull Sale item) {
        Map<String, Object> rowContent = new HashMap<>();
        rowContent.put(SaleColumnKey.SALE_ID.getName(), item.getId());
        rowContent.put(SaleColumnKey.DATE.getName(), item.getDate());
        rowContent.put(SaleColumnKey.CUSTOMER_NAME.getName(), item.getCustomerName());
        rowContent.put(SaleColumnKey.AMOUNT.getName(), item.getAmount());
        return Collections.singletonList(rowContent);
    }
}
