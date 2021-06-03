import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SmokerThread extends Thread {
    private final int necessaryObjectType;
    private SmokingObject object;
    private final int id;
    BufferedWriter out;
    public static final int SMOKING_TIME = 4000;
    public static final int SLEEP_TIME = 6000;
    Random random = new Random();
    TypeRecoder typeRecorder = new TypeRecoder();
    PrintWriter log;

    public SmokerThread(SmokingObject object, BufferedWriter out, PrintWriter log, int id) {
        this.log = log;

        this.object = object;
        this.out = out;
        if (id <= 3) {
            necessaryObjectType = id;
        } else {
            necessaryObjectType = random.nextInt(3) + 1;
        }
        this.id = id;
    }

    public void run() {
        try (var fos = new FileOutputStream("log.txt", true);
             var log = new PrintWriter(fos)) {
            String createMessage = "Smoker " + id + " created he has " + typeRecorder.getSmokerThings(necessaryObjectType);
            System.out.println(createMessage);
            LogUtils.printWithLock(log, createMessage);
            try {
                this.out.write(createMessage + "\n");
                this.out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                object.acquire();
                if (object.getType() == necessaryObjectType) {
                    object.setType(0);
                    try {
                        out.write("Smoker " + id + " Oyyy, I need it, because I have " + typeRecorder.getSmokerThings(necessaryObjectType) + "\n");
                        out.flush();
                        out.write("Smoker " + id + " is smoking\n");
                        out.flush();

                        LogUtils.printWithLock(log, "Smoker " + id + " with " + typeRecorder.getSmokerThings(necessaryObjectType) + " is smoking");

                        System.out.println("Smoker " + id + " is smoking");
                        sleep(SMOKING_TIME);
                        out.write("Smoker " + id + " stopped smoking\n");
                        out.flush();

                        LogUtils.printWithLock(log, "Smoker " + id + " stopped smoking");

                        object.release();
                        sleep(SLEEP_TIME);

                        LogUtils.printWithLock(log, "Smoker " + id + " is sleeping");

                        //out.write("Smoker "+id + " waiting for "+typeRecorder.getDealerThings(necessaryObjectType)+"\n");
                        //out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        object.release();
                        break;
                    }
                } else {
                    object.release();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

