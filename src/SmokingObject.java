import java.util.concurrent.locks.ReentrantLock;

public class SmokingObject {
    private int type = 0;
    private final ReentrantLock lock = new ReentrantLock();
    public void acquire(){
        lock.lock();

    }
    public int getType(){
        return type;
    }
    public void setType (int type){
        this.type = type;
    }
    public void release(){
        lock.unlock();
    }
}
