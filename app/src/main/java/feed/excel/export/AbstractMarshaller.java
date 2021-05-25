package feed.excel.export;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public abstract class AbstractMarshaller implements Closeable {

    public abstract <ObjectType> void marshallSheet(String sheetName, ExcelContentResolver<ObjectType> excelContentResolver,
                                                    Iterator<ObjectType> iterator) throws IOException;

    public abstract void finalizeWritings() throws IOException;

    public abstract File getFile();
}
