package imports.impl;

import imports.ImportMappingReport;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BaseImportMappingService {

    protected ImportMappingReport prepareExceptionReport(Exception e) {
        ImportMappingReport result = new ImportMappingReport();
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        result.setStackTrace(stringWriter.toString());
        return result;
    }
}
