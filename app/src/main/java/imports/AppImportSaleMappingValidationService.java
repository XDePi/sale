package imports;

import imports.bean.ImportSaleColumnMappingBean;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public interface AppImportSaleMappingValidationService {

    /**
     * Initially validate bean
     *
     * @param messageSource - messages strings source
     * @param bean          - bean to validate
     * @return validation result
     */
    @NonNull
    ValidateBeanResult validateBean(@NonNull ReportMessageSource messageSource,
                                    @NonNull ImportSaleColumnMappingBean bean);

    /**
     * Validate mappings before saving
     *
     * @param messageSource    - messages strings source
     * @param mappingsToSave   - Mappings collected for saving
     * @return validation messages
     */
    @NonNull
    List<ReportMessage> validateMappingToSave(@NonNull ReportMessageSource messageSource,
                                              @NonNull Map<Long, ImportSaleColumnMappingBean> mappingsToSave);
}
