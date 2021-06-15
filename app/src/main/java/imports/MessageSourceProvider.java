package imports;

import imports.ReportMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageSourceProvider {

    public ReportMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Messages");
        return new ReportMessageSource(messageSource, Locale.ENGLISH);
    }
}
