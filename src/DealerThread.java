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
    PrintWriter log;
    Random random = new Random();

    public DealerThread(SmokingObject object, BufferedWriter out, PrintWriter log) {
        this.object = object;
        this.out = out;
        this.log = log;
    }

    public void run() {
        try (var fos = new FileOutputStream("log.txt", true);
             var log = new PrintWriter(fos)) {
            while (true) {
                TypeRecoder typeRecorder = new TypeRecoder();
                object.acquire();
                if (object.getType() == 0) {
                    object.setType(random.nextInt(3) + 1);
                    try {
                        out.write("New Object was given! On table " + typeRecorder.getDealerThings(object.getType()) + "\n");
                        out.flush();
                        LogUtils.printWithLock(log, "New Object was given! On table " + typeRecorder.getDealerThings(object.getType()));

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

}
