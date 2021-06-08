package feed.excel;

import exception.InvalidFormatFileException;
import feed.excel.export.XSSFReaderBean;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.CloseableIterator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class ExcelReaderProvider {

    public CloseableIterator<List<String>> buildReaderIterator(@NonNull File file, @NonNull Locale locale, @NonNull String tabName) {
        XSSFReaderBean xssfReaderBean = getXssfReaderBean(file, locale, tabName);
        return new XSSFIterator(xssfReaderBean);
    }

    @NonNull
    XSSFReaderBean getXssfReaderBean(@NonNull File file, @NonNull Locale sourceLocale, @NonNull String tabName) {
        OPCPackage pkg;
        try {
            pkg = OPCPackage.open(file);
        } catch (InvalidFormatException | InvalidOperationException | IllegalStateException | NotOfficeXmlFileException ex) {
            log.error("failed to open file", ex);
            throw new InvalidFormatFileException(ex);
        }

        XSSFReaderBean reader;
        try {
            reader = new XSSFReaderBean(pkg, sourceLocale, tabName);
        } catch (IOException | OpenXML4JException | XMLStreamException | TransformerConfigurationException | SAXException ex) {
            log.error("failed to process file", ex);
            pkg.revert();
            throw new InvalidFormatFileException(ex);
        }
        return reader;
    }
}
