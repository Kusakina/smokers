import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class Receiver extends SwingWorker<String, String> {
    BufferedReader socketReader;
    ClientFrame parent;
    public Receiver( ClientFrame parent){
        this.parent = parent;
        try {
            socketReader = parent.getSocketReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected String doInBackground() throws Exception {
        String newString;
        while(true){
            newString = socketReader.readLine();
            System.out.println(newString);;
            if(newString!=null){
                publish(newString);
            }
        }
    }

    @Override
    protected void process(List<String> chunks) {
        for(String string: chunks){
            //System.out.println(string);
            parent.print(string+"\n");
        }
    }

    public void setSocketReader(BufferedReader socketReader){
        this.socketReader = socketReader;
    }
}
