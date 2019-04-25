import java.io.*;
import java.net.*;

public class MySocket{
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    public MySocket(Socket s){
        socket = s;
    }

    public MySocket(String ip, int port){
        try{
            socket = new Socket(ip, port);
        }catch(IOException ex){}
    }


  /*@Override
  public void connect(SocketAddress addr){
    try{
      super.connect(addr);
    }catch(IOException ex){}
  }*/

    public void close(){
        try{
            socket.close();
        }catch(IOException ex){
        }
    }

    public void shutdownInput(){
        try{
            socket.shutdownInput();
        }catch(IOException ex){
        }
    }

    public void write(String line){
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch(IOException ex){
        }
        out.println(line);
    }

    public String read(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String tmp = "";
            String line = "";
            boolean b = true;

            while((line = in.readLine()) != null && !line.equals("[-1")){
                if(line == null) return null;
                if(b) tmp = line;
                else tmp = tmp + '\n' + line;
                b = false;
            }

           // System.out.println(tmp);
            return tmp;
            /*String line = in.readLine();
            System.out.println(line);
            return line;*/

        }catch(IOException ex){
        }
        return null;
    }

}