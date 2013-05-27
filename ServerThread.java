import java.net.*;
import java.io.*;

public class ServerThread extends Thread{
    private Server server = null;
    private Socket socket = null;
    private int id = -1;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private boolean run = true;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        id = socket.getPort();
        try{
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e){
            System.out.println("Error opening thread: " + e.getMessage());
        }
    }

    public void run(){
        System.out.println("Server Thread " + id + " running.");
        while(run == true){
            try {
                server.handle(in.readUTF(), id);
            } catch (IOException e){
                System.out.println(id + " Error reading: " + e.getMessage());
                server.remove(this);
                run = false;
            }
        } 
    }

    public int getID(){
        return id;
    }

    public void close() throws IOException{
        socket.close();
        in.close();
        out.close();
    }

    public void send(String message) {
        try{
            out.writeUTF(message);
            out.flush();    
        } catch (IOException e){
            System.out.println(id + " Error sending: " + e.getMessage());
            server.remove(this);
            run = false;
        }
    }
}
