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
    private static int count = 1;
    private final int id;
    BufferedWriter out;
    public static final int SMOKING_TIME = 4000;
    public static final int SLEEP_TIME = 6000;
    Random random = new Random();
    Typerecoder typeRecorder = new Typerecoder();

    public SmokerThread(SmokingObject object, BufferedWriter out) {

        this.object = object;
        this.out = out;
        if (count <= 3) {
            necessaryObjectType = count;
        } else {

            necessaryObjectType = random.nextInt(3) + 1;
        }
        id = count++;
        System.out.println("Smoker" + id + " created he has " + typeRecorder.getSmokerThings(necessaryObjectType));
        try (FileOutputStream fileOutputStream = new FileOutputStream("log.txt");
             FileChannel fileChannel = fileOutputStream.getChannel();
             PrintWriter out2 = new PrintWriter(fileOutputStream)) {
            try {
                this.out.write("Smoker" + id + " created, he has " + typeRecorder.getSmokerThings(necessaryObjectType) + "\n");
                this.out.flush();
                while (true) {
                    try (FileLock lock = fileChannel.tryLock()) {
                        if (null == lock) continue;

                        LocalDateTime dateTime = LocalDateTime.now();
                        String logDatePattern = "dd.MM.yyyy HH:mm:ss";
                        DateTimeFormatter logDateFormatter = DateTimeFormatter.ofPattern(logDatePattern);
                        out2.print(logDateFormatter.format(dateTime) + ": " + "New Object was given! On table " + typeRecorder.getDealerThings(object.getType()) + "\n");
                        out2.flush();
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            object.acquire();
            try (FileOutputStream fileOutputStream = new FileOutputStream("log.txt");
                 FileChannel fileChannel = fileOutputStream.getChannel();
                 PrintWriter out2 = new PrintWriter(fileOutputStream)) {
                if (object.getType() == necessaryObjectType) {
                    object.setType(0);
                    try {
                        out.write("Smoker " + id + " Oyyy, I need it, because I have " + typeRecorder.getSmokerThings(necessaryObjectType) + "\n");
                        out.flush();
                        out.write("Smoker " + id + " is smoking\n");
                        out.flush();

                        while (true) {
                            try (FileLock lock = fileChannel.tryLock()) {
                                if (null == lock) continue;

                                LocalDateTime dateTime = LocalDateTime.now();
                                String logDatePattern = "dd.MM.yyyy HH:mm:ss";
                                DateTimeFormatter logDateFormatter = DateTimeFormatter.ofPattern(logDatePattern);
                                out2.print(logDateFormatter.format(dateTime) + ": " + "Smoker " + id +" with"+typeRecorder.getSmokerThings(necessaryObjectType) + " is smoking\n");
                                out2.flush();
                                break;
                            }
                        }

                        System.out.println("Smoker " + id + " is smoking");
                        sleep(SMOKING_TIME);
                        out.write("Smoker " + id + " stopped smoking\n");
                        out.flush();

                        while (true) {
                            try (FileLock lock = fileChannel.tryLock()) {
                                if (null == lock) continue;

                                LocalDateTime dateTime = LocalDateTime.now();
                                String logDatePattern = "dd.MM.yyyy HH:mm:ss";
                                DateTimeFormatter logDateFormatter = DateTimeFormatter.ofPattern(logDatePattern);
                                out2.print(logDateFormatter.format(dateTime) + ": " + "Smoker " + id + " stopped smoking\n");
                                out2.flush();
                                break;
                            }
                        }

                        object.release();
                        sleep(SLEEP_TIME);

                        while (true) {
                            try (FileLock lock = fileChannel.tryLock()) {
                                if (null == lock) continue;

                                LocalDateTime dateTime = LocalDateTime.now();
                                String logDatePattern = "dd.MM.yyyy HH:mm:ss";
                                DateTimeFormatter logDateFormatter = DateTimeFormatter.ofPattern(logDatePattern);
                                out2.print(logDateFormatter.format(dateTime) + ": " + "Smoker " + id + " is sleeping \n");
                                out2.flush();
                                break;
                            }
                        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

