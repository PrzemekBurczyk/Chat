import java.net.*;
import java.io.*;

public class ClientThread extends Thread{
    private Socket socket = null;
    private Client client = null;
    private DataInputStream in = null;
    private boolean run = true;

    public ClientThread(Client client, Socket socket) {
        try{
            this.client = client;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            start();
        } catch(IOException e){
            client.viewMessage("Error creating ClientThread: " + e.getMessage());
            client.halt();
        }
    }

    public void run(){
        while(run == true){
            try{
                client.handle(in.readUTF());
            } catch (IOException e){
                client.viewMessage("Listening from server error: " + e.getMessage());
                run = false;
                client.halt();
            }
        }
    }
}

