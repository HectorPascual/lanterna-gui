import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class Server{

  public static void main(String[] args) {
    MyServerSocket server = MyServerSocket.init(1414);

    final Map<String,MySocket> users = new ConcurrentHashMap<String,MySocket>();

    while(true){
      MySocket client = new MySocket(server.accept());

      //final String username;
      if(client != null){
        Thread t1 = new Thread(new Runnable(){ // Everything inside a thread in order to make each user input independent
          public void run(){

            // Name request part
            String username;
            while(true){
              username = client.read();
              if(!users.containsKey(username)){
                users.put(username,client);

                //Update userlist each time a user joins succesfully
                String userlist = "$userlist,";
                for(String s : users.keySet()){
                  userlist = userlist + s + ",";
                }
                for(MySocket s : users.values()){
                  s.write(userlist);
                  if(s != users.get(username)){
                    s.write(username + " has join the chat");
                  }
                }
                break;
              }
              client.write("Nick already taken. Introduce another username");
              // it should not change the gui
            }

            // Continuous reading
            String line;
            while(true){
              if((line = users.get(username).read()) != null){
                for(String s : users.keySet()){
                  if(s != username) users.get(s).write(username + ": " + line);
                }
              } else {
                String modifyUsers = "$userlist,";
                for(String s : users.keySet()){
                  if(s != username){
                    modifyUsers = modifyUsers + s + ",";
                    users.get(s).write(username + " has left the chat");
                  }
                }
                users.remove(username);
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
