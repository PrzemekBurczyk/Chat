import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame{
    private Socket socket = null;
    private DataOutputStream out = null;
    private ClientThread client = null;
    private String name = null;
    private JTextField textField;
    private JTextArea screenField;
    private JButton sendButton;

    public Client() {
        Font font = new Font("monospaced", Font.PLAIN, 16);

        textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField.setFont(font);
        textField.setPreferredSize(new Dimension(670, 30));
        SendListener sendListener = new SendListener();
        textField.addActionListener(sendListener);

        sendButton = new JButton("Send");
        sendButton.setFont(font);
        sendButton.addActionListener(sendListener);
        sendButton.setPreferredSize(new Dimension(100, 30));

        JPanel sendingPanel = new JPanel();
        sendingPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        sendingPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        sendingPanel.add(textField);
        sendingPanel.add(sendButton);

        screenField = new JTextArea();
        screenField.setFont(font);
        screenField.setEditable(false);
        screenField.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(screenField);
        scrollPane.setPreferredSize(new Dimension(780, 540));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        panel.add(scrollPane, BorderLayout.NORTH);
        panel.add(sendingPanel, BorderLayout.SOUTH);

        this.setContentPane(panel);
        this.setResizable(true);

        try{
            socket = new Socket("127.0.0.1", 8080);
            out = new DataOutputStream(socket.getOutputStream());
            client = new ClientThread(this, socket);
        } catch(UnknownHostException uhe){
            screenField.setText(screenField.getText() + "Unknown host: " + uhe.getMessage() + "\n");
            screenField.setCaretPosition(screenField.getDocument().getLength());
            halt();
            return;
        } catch(IOException ioe){
            screenField.setText(screenField.getText() + "Error creating client: " + ioe.getMessage() + "\n");
            screenField.setCaretPosition(screenField.getDocument().getLength());
            halt();
            return;
        }

        screenField.setText(screenField.getText() + "Podaj nick");
        screenField.setCaretPosition(screenField.getDocument().getLength());
    }

    class SendListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            send();
        }
    }

    public void send(){
        if(!textField.getText().equals("")){
            if(name == null){
                name = textField.getText();
                screenField.setText(screenField.getText() + "\n" + "Wybrany nick: " + name);
                screenField.setCaretPosition(screenField.getDocument().getLength());
            } else {
                try{
                    out.writeUTF(name + ": " + textField.getText());
                    out.flush();
                } catch (IOException e){
                    screenField.setText(screenField.getText() + "\n" + "Error sending message" + e.getMessage());
                    screenField.setCaretPosition(screenField.getDocument().getLength());
                    halt();
                    return;
                }
            }
        }
        textField.setText("");
    }

    public void viewMessage(String message){
        screenField.setText(screenField.getText() + "\n" + message);
        screenField.setCaretPosition(screenField.getDocument().getLength());
    }

    public void halt(){
        textField.setEditable(false);
        sendButton.setEnabled(false);
    }

    public void handle(String message){
        screenField.setText(screenField.getText() + "\n" + message);
        screenField.setCaretPosition(screenField.getDocument().getLength());
    }
}

