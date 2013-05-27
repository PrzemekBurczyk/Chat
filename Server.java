import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread{
    private ServerSocket server = null;
    private boolean run = true;
    private LinkedList<ServerThread> clients = new LinkedList<ServerThread>();

    public Server(int port) {
        System.out.println("Binding to port " + port + " ...");
        try{
            server = new ServerSocket(port);
        } catch (IOException e){
            System.out.println("Cannot bind to port " + port + ": " + e.getMessage());
        }
    }

    public void run(){
        System.out.println("Server started ...");
        while(run == true){
            try{
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
                System.out.println(clients.toString());
            } catch (IOException e){
                System.out.println("Server accept error: " + e.getMessage());
                run = false;
            }
        }
    }

    private void addThread(Socket socket) {
        ServerThread serverThread = new ServerThread(this, socket);
        clients.add(new ServerThread(this, socket));
        serverThread.start();
        System.out.println("Client accepted: " + socket);
    }

    public synchronized void handle(String input, int id) {
        for(ServerThread ss : clients){
            if(ss.getID() != id){
                ss.send(input);  
            }
        }
    }   

    public synchronized void remove(ServerThread st){
        System.out.println(st.getID() + " Removing client thread");
        try{
            st.close();
        } catch (IOException e){
            System.out.println(st.getID() + " Error closing thread: " + e.getMessage());
        }
        for(ServerThread ss : clients){
            if(ss.getID() == st.getID()){
                clients.remove(ss);
                return;
            }
        }
    }

    public static void main(String[] args){
        Server myServer = new Server(8080);
        myServer.start();
    }
}
