import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String []args){
        try {
            ServerSocket connectionSocket = new ServerSocket(8080);
            Socket clientSocket = connectionSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String countString = in.readLine();
            System.out.println(countString);
            if(countString!=null){
                SmokingObject object = new SmokingObject();
                new DealerThread(object, out).start();
                int count = Integer.parseInt(countString);
                for (int i = 0; i < count; i++) {
                    new SmokerThread(object, out).start();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }


    }

}
