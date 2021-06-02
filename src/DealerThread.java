import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DealerThread extends Thread {
    SmokingObject object;
    BufferedWriter out;
    Random random = new Random();

    public void run() {
        try (FileOutputStream fileOutputStream = new FileOutputStream("log.txt");
             FileChannel fileChannel = fileOutputStream.getChannel();
             PrintWriter out2 = new PrintWriter(fileOutputStream)) {


            while (true) {
                Typerecoder typeRecorder = new Typerecoder();
                object.acquire();
                if (object.getType() == 0) {
                    object.setType(random.nextInt(3) + 1);
                    try {
                        out.write("New Object was given! On table " + typeRecorder.getDealerThings(object.getType()) + "\n");
                        out.flush();
                        while (true) {
                            try (FileLock lock = fileChannel.tryLock()) {
                                if (null == lock) continue;

                                LocalDateTime dateTime = LocalDateTime.now();
                                String logDatePattern = "dd.MM.yyyy HH:mm:ss";
                                DateTimeFormatter logDateFormatter = DateTimeFormatter.ofPattern(logDatePattern);
                                out2.print(logDateFormatter.format(dateTime) + ": " + "New Object was given! On table " + typeRecorder.getDealerThings(object.getType()) + "\n");
                                break;
                            }
                        }

                        System.out.println("New Object was given! On table " + typeRecorder.getDealerThings(object.getType()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        object.release();
                        break;
                    }

                }
                object.release();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                object.acquire();
                if (object.getType() != 0) {
                    object.setType(0);
                }
                object.release();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DealerThread(SmokingObject object, BufferedWriter out) {
        this.object = object;
        this.out = out;
    }

}
