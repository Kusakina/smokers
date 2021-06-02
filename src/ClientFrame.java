import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientFrame extends JFrame {
    private JTextArea console = new JTextArea();
    private Socket clientSocket;
    private BufferedWriter out;
    private boolean sent = false;
    private Receiver receiver;
    public ClientFrame(){
        initFrame();
        setVisible(true);
        try {
            clientSocket = new Socket(InetAddress.getByName("localhost"), 8080);
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            receiver = new Receiver(this);
            receiver.execute();
        }catch(IOException e){
            e.printStackTrace();
//            print(e.getMessage()+"\n");
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
//        for (int i = 0; i < 10; i++) {
//            print("test");
//        }
    }
    public BufferedReader getSocketReader() throws IOException{
        return new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    public void print(String string){
        console.append(string);
    }

    public void initFrame(){
        this.setSize(640, 480);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel mainePanel = new JPanel(new BorderLayout());
        mainePanel.add(new JScrollPane(console), BorderLayout.CENTER);
        JPanel numberPanel = new JPanel(new FlowLayout());
        JTextField textField = new JTextField();
        textField.setColumns(20);
        JButton button = new JButton("Send number");
        button.addActionListener(actionEvent -> {
            try (FileOutputStream fileOutputStream = new FileOutputStream("log.txt");
                 FileChannel fileChannel = fileOutputStream.getChannel();
                 PrintWriter out2 = new PrintWriter(fileOutputStream)) {
                if (!sent) {
                    String value = textField.getText();
                    try {
                        int n = Integer.parseInt(value);
                        if(n<3 || n>10){
                            throw new IllegalArgumentException("Number must be in range [3, 10]\n");
                        }
                        print("Sending "+ value+"\n");
                        out.write(value + "\n");
                        out.flush();
                        while (true) {
                            try (FileLock lock = fileChannel.tryLock()) {
                                if (null == lock) continue;

                                LocalDateTime dateTime = LocalDateTime.now();
                                String logDatePattern = "dd.MM.yyyy HH:mm:ss";
                                DateTimeFormatter logDateFormatter = DateTimeFormatter.ofPattern(logDatePattern);
                                out2.print(logDateFormatter.format(dateTime) + ": " + value +" smokers were created");
                                out2.flush();
                                break;
                            }
                        }
                        sent = true;

                    } catch (NumberFormatException e) {
                        print("Invalid value format. Enter only digits\n");
                    } catch (Exception e) {
                        print(e.getMessage()+"\n");
                    }
                } else print("Number already sent.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        numberPanel.add(textField);
        numberPanel.add(button);
        mainePanel.add(numberPanel, BorderLayout.NORTH);
        this.add(mainePanel);
    }

    public static void main(String[] args){
        new ClientFrame();
    }

}
