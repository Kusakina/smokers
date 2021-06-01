import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

public class DealerThread extends Thread{
    SmokingObject object;
    BufferedWriter out;
    Random random = new Random();
    public void run(){
        while (true){
            Typerecoder typeRecorder = new Typerecoder();
            object.acquire();
            if(object.getType()==0){
                object.setType(random.nextInt(3)+1);
                try {
                    out.write("New Object was given! On table "+ typeRecorder.getDealerThings(object.getType())+"\n");
                    out.flush();
                    System.out.println("New Object was given! On table "+typeRecorder.getDealerThings(object.getType()));
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
            if(object.getType()!=0){
                object.setType(0);
            }
            object.release();


        }

    }
    public DealerThread(SmokingObject object, BufferedWriter out){
        this.object =object;
        this.out = out;
    }
}
