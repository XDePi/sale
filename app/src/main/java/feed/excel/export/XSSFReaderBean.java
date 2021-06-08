package feed.excel.export;

import exception.InvalidFormatFileException;
import exception.SheetNotFoundFileException;
import feed.excel.SheetRowContentHandler;
import feed.excel.StructuredElementFragmentEventReader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.springframework.util.xml.StaxUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
public class XSSFReaderBean {

    private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newFactory();

    private final OPCPackage pkg;
    private final List<StructuredElementFragmentEventReader> xmlReaders;
    private final Transformer transformer;
    private final SheetRowContentHandler contentHandler;
    private final XSSFSheetXMLHandler handler;
    private int openSheetCount;
    private int selectedSheetIndex;

    static {
        try {
            TRANSFORMER_FACTORY.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            XML_INPUT_FACTORY.setXMLResolver(new DummyExternalEntityXMLResolver());
            XML_INPUT_FACTORY.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            XML_INPUT_FACTORY.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            XML_INPUT_FACTORY.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        } catch (TransformerConfigurationException ex) {
            log.error("failed to create transformer factory", ex);
            throw new IllegalStateException(ex);
        }
    }

    public XSSFReaderBean(OPCPackage pkg, Locale sourceLocale, @NonNull String tabName) throws IOException, OpenXML4JException, XMLStreamException, TransformerConfigurationException, SAXException {
        this.pkg = pkg;
        xmlReaders = new ArrayList<>();
        XSSFReader reader = new XSSFReader(pkg);
        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) reader.getSheetsData();
        int sheetIndex = 0;
        boolean sheetFound = false;
        while (sheets.hasNext()) {
            XMLEventReader eventReader = XML_INPUT_FACTORY.createXMLEventReader(sheets.next());
//            fist sheet with given name is selected
            if (!sheetFound && tabName.equalsIgnoreCase(sheets.getSheetName())) {
                selectedSheetIndex = sheetIndex;
                sheetFound = true;
            }
            sheetIndex++;
            xmlReaders.add(new StructuredElementFragmentEventReader(eventReader, "/worksheet/sheetData/row"));
        }
        if (!sheetFound && sheetIndex > 1) { // when sheet number > 1 and sheet is not found by name
            throw new SheetNotFoundFileException();
        }
        openSheetCount = xmlReaders.size();
        transformer = TRANSFORMER_FACTORY.newTransformer();
        contentHandler = new SheetRowContentHandler();
        handler = new XSSFSheetXMLHandler(reader.getStylesTable(), new ReadOnlySharedStringsTable(pkg), contentHandler,
                new CustomDataFormatter(sourceLocale), false);
    }

    public int getSelectedSheetIndex() {
        return selectedSheetIndex;
    }

    public List<String> readDataRow(int sheetNumber) {
        StructuredElementFragmentEventReader xmlReader = xmlReaders.get(sheetNumber);
        if (xmlReader == null) {
            return null;
        }
        try {
            xmlReader.markFragmentProcessed();
            if (!xmlReader.moveCursorToNextFragment()) {
                return null;
            }
            xmlReader.markStartFragment();
            contentHandler.clear();
            transformer.transform(new SAXSource(StaxUtils.createXMLReader(xmlReader), new InputSource()), new SAXResult(handler));
            List<String> result = contentHandler.getContent();
            contentHandler.clear();
            return result;
        } catch (TransformerException | XMLStreamException ex) {
            log.error("failed to read file row", ex);
            throw new InvalidFormatFileException(ex);
        }
    }

    public void close(int sheetNumber) throws XMLStreamException {
        StructuredElementFragmentEventReader xmlReader = xmlReaders.get(sheetNumber);
        if (xmlReader != null) {
            xmlReader.close();
        }
        if (--openSheetCount == 0) {
            pkg.revert(); //yeah, it's supposed to be revert(), not close()
        }
    }

    private static class DummyExternalEntityXMLResolver implements XMLResolver {
        public static final byte[] EMPTY_BYTES = new byte[0];

        @Override
        public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace) throws XMLStreamException {
            return new ByteArrayInputStream(EMPTY_BYTES);
        }
    }

    private static class CustomDataFormatter extends DataFormatter {
        public CustomDataFormatter(Locale locale) {
            super(locale);
        }

        @Override
        public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing) {
            if (DateUtil.isADateFormat(formatIndex, formatString)) {
                return super.formatRawCellContents(value, formatIndex, formatString, use1904Windowing);
            }
            return super.formatRawCellContents(value, formatIndex, "@", use1904Windowing);
        }
    }
}
