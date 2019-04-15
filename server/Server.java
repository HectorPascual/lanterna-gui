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
              if(s != users.get(name)){
                s.write(name + " has join the chat");
              }
            }
            break;
          }
          client.write("Nick already taken. Introduce another name");
          // it should not change the gui
        }

        Thread t1 = new Thread(new Runnable(){
          public void run(){
            String line;
            while(true){
              if((line = users.get(name).read()) != null){
                for(String s : users.keySet()){
                  if(s != name) users.get(s).write(name + ": " + line);
                }
              } else {
                String modifyUsers = "$userlist,";
                for(String s : users.keySet()){
                  if(s != name){
                    modifyUsers = modifyUsers + s + ",";
                    users.get(s).write(name + " has left the chat");
                  }
                }
                users.remove(name);
                for(MySocket s : users.values()){
                  System.out.println(modifyUsers);
                  s.write(modifyUsers);
                }
                break;
              }
            }
          }
        });
        t1.start();
      }
    }
  }
}
