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
            if (countString != null) {

                try (var fos = new FileOutputStream("log.txt", true);
                     var log = new PrintWriter(fos)) {
                    int count = Integer.parseInt(countString);
                    LogUtils.printToLog(log,count + " smokers were created");

                    SmokingObject object = new SmokingObject();
                    new DealerThread(object, out, log).start();
                    for (int i = 0; i < count; i++) {
                        new SmokerThread(object, out, log, i + 1).start();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
