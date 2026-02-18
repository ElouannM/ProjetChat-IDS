import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ClientVue extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton quitButton;

    LocalTime time;
    DateTimeFormatter formatter;
    String stringTime;

    ChatClientController control;


    public ClientVue(ChatClientController control){
        this.control = control;
        control.setVue(this);

        time = LocalTime.now();
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        stringTime = "[" + time.format(formatter) + "] ";

        init();
        addListeners();
        setTitle(control.getServerName()+" - " + control.getUserName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);
    }



    private void init(){
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createTitledBorder("Messages"));


        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        sendButton = new JButton("Envoyer");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.WHITE);
        
        quitButton = new JButton("Quitter");
        quitButton.setFont(new Font("Arial", Font.BOLD, 14));
        quitButton.setBackground(new Color(220, 20, 60));
        quitButton.setForeground(Color.WHITE);


        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.add(sendButton);
        buttonPanel.add(quitButton);
        
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        add(chatScrollPane, BorderLayout.CENTER);
        
        add(inputPanel, BorderLayout.SOUTH);
    }


    private void addListeners(){
        sendButton.addActionListener(e -> sendMessage());
        quitButton.addActionListener(e->quitChat());

        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                quitChat();
            }
        });
        messageField.addActionListener(e -> sendMessage());

    }



    public void sendMessage(){
        String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                control.sendMessage(message);
                messageField.setText("");
            }
    }

    public void quitChat(){
        control.leaveChat();
        System.exit(0);
    }

    public void afficherMessage(String message,String from){
        SwingUtilities.invokeLater(() -> {
            String prefix = from;
            if(from.equals(control.getUserName())){
                prefix = "Vous";
            }
            
            chatArea.append(getCurrentTime() + prefix + ": " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void joinChat(String name){
        SwingUtilities.invokeLater(() -> {
            String mess = getCurrentTime() +name + " has joined the chat\n";
            chatArea.append(mess);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void leaveChat(String name){
        SwingUtilities.invokeLater(() -> {
                String mess = getCurrentTime() + name + " has left the chat\n";
                chatArea.append(mess);
                chatArea.setCaretPosition(chatArea.getDocument().getLength());
            });
    }


    public void serverShutdown(){
        SwingUtilities.invokeLater(() -> {
        messageField.setEnabled(false);
        sendButton.setEnabled(false);
        JOptionPane.showMessageDialog(this, "Server is shutting down", "Warning", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    });
    }



    private String getCurrentTime(){
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return "[" + time.format(formatter) + "] ";
    }
}