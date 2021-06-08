package app.feed.excel.export;

import feed.excel.ExcelReaderProvider;
import feed.excel.export.ExcelColumnDescriptor;
import feed.excel.export.ExcelContentResolver;
import feed.excel.export.ExcelFeedDescriptor;
import feed.excel.export.ExcelMarshaller;
import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.common.utils.CloseableIterator;
import org.apache.poi.util.DefaultTempFileCreationStrategy;
import org.apache.poi.util.TempFile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExcelMarshallerTest {
    private final ExcelReaderProvider excelReaderProvider = new ExcelReaderProvider();

    private File file;

    @TempDir
    public File anotherTempDir;

    @BeforeEach
    public void init() {
        File dir = new File(anotherTempDir, "somepath");
        dir.mkdir();
        TempFile.setTempFileCreationStrategy(new DefaultTempFileCreationStrategy(dir));
        file = new File(dir, "ExcelMarshallerTest.xlsx");
    }

    @Test
    public void marshallSheet_marshall_some_sheet() throws IOException {
        String sheetName = "some sheet";
        String stringColumnName = "some string column";
        String moneyColumnName = "some money column";
        ExcelContentResolver<String> excelContentResolver = new ExcelContentResolver<>() {
            @NonNull
            @Override
            public ExcelFeedDescriptor buildDescriptor() {
                ExcelFeedDescriptor excelFeedDescriptor = new ExcelFeedDescriptor();
                excelFeedDescriptor.addColumn(stringColumnName, ExcelColumnDescriptor.CellType.STRING);
                excelFeedDescriptor.addColumn(moneyColumnName, ExcelColumnDescriptor.CellType.FLOAT2);
                return excelFeedDescriptor;
            }

            @NonNull
            @Override
            public List<Map<String, Object>> resolveRow(@NonNull String item) {
                return Collections.singletonList(ImmutableMap.of(stringColumnName, "string_" + item, moneyColumnName, 1.23D));
            }
        };
        String id1 = "id 1";
        String id2 = "id 2";

        try (ExcelMarshaller excelMarshaller = new ExcelMarshaller(file)) {
            excelMarshaller.marshallSheet(sheetName, excelContentResolver, Arrays.asList(id1, id2).iterator());
            excelMarshaller.finalizeWritings();
        }

        try (CloseableIterator<List<String>> contentIterator = excelReaderProvider.buildReaderIterator(file, Locale.ENGLISH, sheetName)) {
            assertEquals(Arrays.asList(stringColumnName, moneyColumnName), contentIterator.next());
            assertEquals(Arrays.asList("string_id 1", "1.23"), contentIterator.next());
            assertEquals(Arrays.asList("string_id 2", "1.23"), contentIterator.next());
            assertFalse(contentIterator.hasNext());
        }
    }

    @Test
    public void marshallSheet_marshalling_of_sheet_threw_exception_check_we_can_marshall_this_sheet_again() throws IOException {
        String sheetName = "some sheet";
        String stringColumnName = "some string column";
        String moneyColumnName = "some money column";
        AtomicBoolean thrown = new AtomicBoolean(false);
        ExcelContentResolver<String> excelContentResolver = new ExcelContentResolver<String>() {
            @NonNull
            @Override
            public ExcelFeedDescriptor buildDescriptor() {
                ExcelFeedDescriptor excelFeedDescriptor = new ExcelFeedDescriptor();
                excelFeedDescriptor.addColumn(stringColumnName, ExcelColumnDescriptor.CellType.STRING);
                excelFeedDescriptor.addColumn(moneyColumnName, ExcelColumnDescriptor.CellType.FLOAT2);
                return excelFeedDescriptor;
            }

            @NonNull
            @Override
            public List<Map<String, Object>> resolveRow(@NonNull String item) {
                if (!thrown.get()) {
                    thrown.set(true);
                    throw new IllegalStateException("some exception");
                }
                return Collections.singletonList(ImmutableMap.of(stringColumnName, "string_" + item, moneyColumnName, 1.23D));
            }
        };
        String id1 = "id 1";
        String id2 = "id 2";
        String id3 = "id 3";
        String id4 = "id 4";

        try (OutputStream outputStream = new FileOutputStream(file);
             ExcelMarshaller excelMarshaller = new ExcelMarshaller(file)) {
            Assertions.assertThatThrownBy(() ->
                    excelMarshaller.marshallSheet(sheetName, excelContentResolver, Arrays.asList(id1, id2).iterator()))
                    .isExactlyInstanceOf(IllegalStateException.class);
            excelMarshaller.marshallSheet(sheetName, excelContentResolver, Arrays.asList(id3, id4).iterator());
            excelMarshaller.finalizeWritings();
        }

        try (CloseableIterator<List<String>> contentIterator = excelReaderProvider.buildReaderIterator(file, Locale.ENGLISH, sheetName)) {
            assertEquals(Arrays.asList(stringColumnName, moneyColumnName), contentIterator.next());
            assertEquals(Arrays.asList("string_id 3", "1.23"), contentIterator.next());
            assertEquals(Arrays.asList("string_id 4", "1.23"), contentIterator.next());
            assertFalse(contentIterator.hasNext());
        }
    }

    @Test
    public void marshallSheet_when_descriptor_contains_float_column_then_it_is_rendered_correctly() throws IOException {
        String sheetName = "some sheet";
        String stringColumnName = "some string column";
        String moneyColumnName = "some money column";
        String bigDecimalColumnName = "big decimal column name";
        String longString = RandomStringUtils.random(250, true, true);
        ExcelContentResolver<String> excelContentResolver = new ExcelContentResolver<String>() {
            @NonNull
            @Override
            public ExcelFeedDescriptor buildDescriptor() {
                ExcelFeedDescriptor excelFeedDescriptor = new ExcelFeedDescriptor();
                excelFeedDescriptor.addColumn(stringColumnName, ExcelColumnDescriptor.CellType.STRING);
                excelFeedDescriptor.addColumn(moneyColumnName, ExcelColumnDescriptor.CellType.FLOAT2);
                excelFeedDescriptor.addColumn(bigDecimalColumnName, ExcelColumnDescriptor.CellType.FLOAT2);
                return excelFeedDescriptor;
            }

            @NonNull
            @Override
            public List<Map<String, Object>> resolveRow(@NonNull String item) {
                String stringValue;
                if ("id 4".equals(item)) {
                    stringValue = longString;
                } else {
                    stringValue = "string_" + item;
                }
                return Collections.singletonList(ImmutableMap.of(stringColumnName, stringValue, moneyColumnName,
                        1.23D, bigDecimalColumnName, BigDecimal.valueOf(1L)));
            }
        };

        String id3 = "id 3";
        String id4 = "id 4";

        try (ExcelMarshaller excelMarshaller = new ExcelMarshaller(file)) {
            excelMarshaller.marshallSheet(sheetName, excelContentResolver, Arrays.asList(id3, id4).iterator());
            excelMarshaller.finalizeWritings();
        }

        try (CloseableIterator<List<String>> contentIterator = excelReaderProvider.buildReaderIterator(file, Locale.ENGLISH, sheetName)) {
            assertEquals(Arrays.asList(stringColumnName, moneyColumnName, bigDecimalColumnName), contentIterator.next(),"column headers are included to result");
            assertEquals(Arrays.asList("string_id 3", "1.23", "1"), contentIterator.next(),"string value is formatted correctly");
            assertEquals(Arrays.asList(longString, "1.23", "1"), contentIterator.next(), "long value is formatted correctly");
            assertFalse(contentIterator.hasNext(), "iterator should contain single record");
        }
    }

}
