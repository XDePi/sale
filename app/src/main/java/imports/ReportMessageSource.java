package imports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@AllArgsConstructor
@Data
public class ReportMessageSource {

    @Getter
    private MessageSource messageSource;
    @Getter
    private Locale locale;

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @return the resolved ReportMessage in error format
     * @throws NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    public ReportMessage getErrorMsg(String code) throws NoSuchMessageException {
        return getErrorMsg(code, new String[]{});
    }


    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @param args an array of arguments that will be filled in for params within
     *             the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *             or {@code null} if none.
     * @return the resolved ReportMessage in error format
     * @throws NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    public ReportMessage getErrorMsg(String code, Object[] args) throws NoSuchMessageException {
        return new ReportMessage(messageSource.getMessage(code, args, locale), ReportMessage.Level.ERROR);
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @param arg0 argument #0 that will be filled in for params within
     *             the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *             or {@code null} if none.
     * @return the resolved ReportMessage in error format
     * @throws NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    public ReportMessage getErrorMsg(String code, Object arg0) throws NoSuchMessageException {
        return new ReportMessage(messageSource.getMessage(code, new Object[]{arg0}, locale), ReportMessage.Level.ERROR);
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @param arg0 argument #0 that will be filled in for params within
     *             the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *             or {@code null} if none.
     * @param arg1 argument #1 that will be filled in for params within
     *             the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *             or {@code null} if none.
     * @return the resolved ReportMessage in error format
     * @throws NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    public ReportMessage getErrorMsg(String code, Object arg0, Object arg1) throws NoSuchMessageException {
        return new ReportMessage(messageSource.getMessage(code, new Object[]{arg0, arg1}, locale), ReportMessage.Level.ERROR);
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @return the resolved ReportMessage in warning format
     * @throws NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    public ReportMessage getWarnMsg(String code) throws NoSuchMessageException {
        return getWarnMsg(code, new String[]{});
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @param args an array of arguments that will be filled in for params within
     *             the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *             or {@code null} if none.
     * @return the resolved ReportMessage in warning format
     * @throws NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    public ReportMessage getWarnMsg(String code, Object[] args) throws NoSuchMessageException {
        return new ReportMessage(messageSource.getMessage(code, args, locale), ReportMessage.Level.WARNING);
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @param arg0 argument that will be filled in for param #0 within
     *             the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *             or {@code null} if none.
     * @return the resolved ReportMessage in warning format
     * @throws NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    public ReportMessage getWarnMsg(String code, Object arg0) {
        return new ReportMessage(messageSource.getMessage(code, new Object[]{arg0}, locale), ReportMessage.Level.WARNING);
    }

    public ReportMessage getWarnMsg(String code, Object arg0, Object arg1) {
        return getWarnMsg(code, new Object[]{arg0, arg1});
    }

    public ReportMessage getWarnMsg(String code, Object arg0, Object arg1, Object arg2) {
        return getWarnMsg(code, new Object[]{arg0, arg1, arg2});
    }

    public ReportMessage getWarnMsg(String code, Object arg0, Object arg1, Object arg2, Object arg3) {
        return getWarnMsg(code, new Object[]{arg0, arg1, arg2, arg3});
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @param args an array of arguments that will be filled in for params within
     *             the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *             or {@code null} if none.
     * @return the resolved ReportMessage in info format
     * @throws NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    public ReportMessage getInfoMsg(String code, Object[] args) throws NoSuchMessageException {
        return new ReportMessage(messageSource.getMessage(code, args, locale), ReportMessage.Level.INFO);
    }

    public ReportMessage getInfoMsg(String code) throws NoSuchMessageException {
        return new ReportMessage(messageSource.getMessage(code, new Object[]{}, locale), ReportMessage.Level.INFO);
    }

    public ReportMessage getInfoMsg(String code, Object arg0) throws NoSuchMessageException {
        return new ReportMessage(messageSource.getMessage(code, new Object[]{arg0}, locale), ReportMessage.Level.INFO);
    }

    public ReportMessage getInfoMsg(String code, Object arg0, Object arg1) throws NoSuchMessageException {
        return new ReportMessage(messageSource.getMessage(code, new Object[]{arg0, arg1}, locale), ReportMessage.Level.INFO);
    }

    public String getMsg(String code, Object[] args) {
        return messageSource.getMessage(code, args, locale);
    }

    public String getMsg(String code, Object arg0) {
        return messageSource.getMessage(code, new Object[]{arg0}, locale);
    }

    public String getMsg(String code) {
        return messageSource.getMessage(code, new Object[]{}, locale);
    }
}
