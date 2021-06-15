package imports;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidateBeanResult {

    private final List<ReportMessage> messages = new ArrayList<>();
    private boolean ignored = false;
}
