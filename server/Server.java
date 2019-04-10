import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class Server{

  public static void main(String[] args) {
    MyServerSocket server = MyServerSocket.init(1414);

    final Map<String,MySocket> users = new ConcurrentHashMap<String,MySocket>();

    while(true){
      MySocket client = new MySocket(server.accept());
      final String name;
      if(client != null){
        String username;
        while(true){
          username = client.read();
          if(!users.containsKey(username)){
            name = username;
            users.put(name,client);

            //Update userlist each time a user joins succesfully
            String userlist = "$userlist,";
            for(String s : users.keySet()){
              userlist = userlist + s + ",";
            }
            for(MySocket s : users.values()){
              s.write(userlist);
            }

            break;
          }
          client.write("Nick already taken");
        }

        Thread t1 = new Thread(new Runnable(){
          public void run(){
            String line;
            while(true){
              if((line = users.get(name).read()) != null){
                for(String s : users.keySet()){               //maybe a method because we use it more than once
                  if(s != name) users.get(s).write(name + ": " + line);
                }
              }
            }
          }
        });
        t1.start();
      }
    }
  }
}
