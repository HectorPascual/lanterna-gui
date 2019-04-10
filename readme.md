### LANTERNA GUI for the Kotlin and Java chat

## How to


In order to compile and run the code it must be done with the following commands :

1. A server instance must be started before running the client
```
cd server
javac Server.java MyServerSocket.java MySocket.java
java Server
```

2. Compile the client and then run it

```
cd src
javac -cp ".:../libs/lanterna-3.0.1.jar" FirstGUI.java Client.java MySocket.java KeyStrokeListener.java
java -cp ".:../libs/lanterna-3.0.1.jar" Client

```
