package feed.excel;

import org.springframework.batch.item.xml.stax.DefaultFragmentEventReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class StructuredElementFragmentEventReader extends DefaultFragmentEventReader{

    private final Set<String> counterNodes = new HashSet<>();
    private final LinkedList<String> pathList = new LinkedList<>();

    public StructuredElementFragmentEventReader(XMLEventReader wrappedEventReader, String... counterNodes) {
        super(wrappedEventReader);
        this.counterNodes.addAll(Arrays.asList(counterNodes));
        pathList.add(""); //always contains the empty root
    }

    public boolean moveCursorToNextFragment() throws XMLStreamException {
        while (peek() != null) {
            XMLEvent event = peek();
            if (event.isStartElement()) {
                String last = pathList.getLast();
                last += "/" + event.asStartElement().getName().getLocalPart();
                if (counterNodes.contains(last)) {
                    return true;
                }
                pathList.addLast(last);
                nextEvent();
            } else if (event.isEndElement()) {
                pathList.removeLast();
                nextEvent();
            } else {
                nextEvent();
            }
        }
        return false;
    }
}
