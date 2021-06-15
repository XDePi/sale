package imports.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@ToString(callSuper = true)
public class ImportSaleColumnMappingBean extends BaseImportMappingBean {

    private Long saleId;
    private Date date;
    private String customerName;
    private BigDecimal amount;

    public ImportSaleColumnMappingBean(int rowNumber) {
        super(rowNumber);
    }
}
