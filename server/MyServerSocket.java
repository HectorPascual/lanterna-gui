import java.net.*;
import java.io.*;

public class MyServerSocket extends ServerSocket {

    private PrintWriter printout;
    private BufferedReader in;

    public static MyServerSocket init(int port) {
        try {
            return new MyServerSocket(port);
        } catch (IOException ex) {
            return null;
        }
    }

    public MyServerSocket(int port) throws IOException {
        super(port);
    }

    @Override
    public Socket accept() {
        try {
            return super.accept();
        } catch (IOException ex) {
        }
        return null;
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (IOException ex) {
        }
    }

}
