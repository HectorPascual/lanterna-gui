import java.io.*;
import java.net.*;

public class Client{

    public static void main(String[] args) {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final MySocket client = new MySocket("127.0.0.1",1414);

        FirstGUI gui = new FirstGUI(client);

        Thread guiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                gui.run();
            }
        });

        guiThread.start();

        /*Thread t1 = new Thread(new Runnable(){
            public void run() {
                try{
                    String line;
                    while((line = in.readLine()) != null){
                        client.write(line);
                    }
                } catch(IOException ex){
                }
                client.shutdownInput();
            }
        });
        t1.start();*/

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                String line;
                while((line = client.read()) != null){
                    gui.write(line);
                }
                client.close();
            }
        });
        t2.start();

    }
}