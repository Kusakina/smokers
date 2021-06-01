import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

public class SmokerThread extends Thread {
    private final int necessaryObjectType;
    private SmokingObject object;
    private static int count = 1;
    private final int id;
    BufferedWriter out;
    public static final int SMOKING_TIME =4000;
    public static final int SLEEP_TIME =6000;
    Random random =new Random();
    Typerecoder typeRecorder = new Typerecoder();
    public SmokerThread(SmokingObject object, BufferedWriter out){

        this.object =object;
        this.out = out;
        if(count<=3){
            necessaryObjectType = count;
        } else{

            necessaryObjectType = random.nextInt(3)+1;
        }
        id = count++;
        System.out.println("Smoker"+id+" created he has " + typeRecorder.getSmokerThings(necessaryObjectType));
        try {
            this.out.write("Smoker" + id + " created, he has " + typeRecorder.getSmokerThings(necessaryObjectType) + "\n");
            this.out.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while (true){
            object.acquire();
            if(object.getType()==necessaryObjectType){
                object.setType(0);
                try {
                    out.write("Smoker "+id + " Oyyy, I need it, because I have "+ typeRecorder.getSmokerThings(necessaryObjectType) + "\n");
                    out.flush();
                    out.write("Smoker "+id + " is smoking\n");
                    out.flush();
                    System.out.println("Smoker "+id + " is smoking");
                    sleep(SMOKING_TIME);
                    out.write("Smoker "+id + " stopped smoking\n");
                    out.flush();
                    object.release();
                    sleep(SLEEP_TIME);
                    //out.write("Smoker "+id + " waiting for "+typeRecorder.getDealerThings(necessaryObjectType)+"\n");
                    //out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    object.release();
                    break;
                }
            }else {
                object.release();
            }
        }

    }
}
