import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtils {

    public static void printToLog(PrintWriter out, String message) {
        LocalDateTime dateTime = LocalDateTime.now();
        String logDatePattern = "dd.MM.yyyy HH:mm:ss";
        DateTimeFormatter logDateFormatter = DateTimeFormatter.ofPattern(logDatePattern);
        out.println(logDateFormatter.format(dateTime) + ": " + message);
        out.flush();
    }

    private static final Object lock = new Object();

    public static synchronized void printWithLock(PrintWriter log, String message) {
        LogUtils.printToLog(log, message);
    }
}
