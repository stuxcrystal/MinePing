package net.stuxcrystal.minehack.mineping;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.apache.commons.lang3.StringUtils;

/**
 * For
 * @author StuxCrystal
 *
 */
public class LogFormatter extends Formatter {
    private final SimpleDateFormat date;

    public LogFormatter() {
       date = new SimpleDateFormat("[HH:mm:ss]");
    }

    /**
     * Default Logging Format:<br>{@code [HH:mm:ss] [Logger] [Level] Message}
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        Throwable ex = record.getThrown();

        builder.append(date.format(record.getMillis()));
        builder.append(" [").append(record.getLoggerName()).append("] ");
        builder.append(formatLogEnd(record.getLevel().getName(), 6));
        builder.append(formatMessage(record));
        builder.append('\n');

        if (ex != null) {
            StringWriter writer = new StringWriter();
            ex.printStackTrace(new PrintWriter(writer));
            builder.append(writer);
        }

        return builder.toString();
    }

    public String formatLogEnd(String str, int length) {
    	return StringUtils.rightPad("[" + str + "]", length + 3);
    }

}