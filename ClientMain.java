import javax.swing.*;
import java.awt.*;

public class ClientMain extends JApplet{

    public ClientMain(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){
            e.printStackTrace();
        }
        JFrame frame = new Client();
        this.setContentPane(frame.getContentPane());
    }

    public void init(){
        this.setVisible(true);
        this.setSize(new Dimension(800, 600));
    }
}
