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
                  s.write(userlist + '\n' + "[-1");
                  if(s != users.get(username)){
                    System.out.println("Sending " + username + "has join the chat to" + s.toString());
                    s.write(username + " has joined the chat" + '\n' + "[-1");
                  }
                }
                break;
              }
              client.write("Nick already taken. Introduce another username" + '\n' + "[-1");
              // it should not change the gui
            }

            // Continuous reading
            String line;
            while(true){
              if((line = users.get(username).read()) != null){
                for(String s : users.keySet()){
                  if(s != username) users.get(s).write(username + ": " + line + '\n' + "[-1");
                }
              } else {
                String modifyUsers = "$userlist,";
                for(String s : users.keySet()){
                  if(s != username){
                    modifyUsers = modifyUsers + s + ",";
                    users.get(s).write(username + " has left the chat" +'\n' + "[-1");
                  }
                }
                users.remove(username);
                for(MySocket s : users.values()){
                  System.out.println(modifyUsers);
                  s.write(modifyUsers + '\n' + "[-1");
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
