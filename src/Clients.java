import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Clients extends JFrame {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    private ServerWindow serverWindow;

    private boolean connected;
    private String name;


    JTextArea log;
    JTextField IPAddress, Port, fLogin = new JTextField("Ivan ivanovich"), Message;
    JPasswordField passwordField;
    JButton jButtonLogin, jButtonSend;
    JPanel jPanel;

    public Clients(ServerWindow serverWindow) {
        this.serverWindow = serverWindow;

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat clients");
        setLocation(serverWindow.getX(), serverWindow.getY());
        createPanel();
        setVisible(true);
    }

    private void connectToServer() {
        if (serverWindow.connectClients(this)) {
            appendLog("Вы подключились! \n");
            jPanel.setVisible(false);
            connected = true;
            name = fLogin.getText();
            String log = serverWindow.getLog();
            if (log != null) {
                appendLog(log);

            }
        }

    }

    public void disconnectFromServer() {
        if (connected)
            jPanel.setVisible(true);
            connected = false;
            serverWindow.disconnectClients(this);
            appendLog("Вы отключены от сервера");
    }

    public void message() {
        if (connected) {
            String text = Message.getText();
            if (!text.isEmpty()) {
                serverWindow.message(name + ": " + text);
                Message.setText("");
            }
        } else {
            appendLog("Нет подключения к серверу");
        }
    }
    public void answer(String text) {
        appendLog(text);
    }

    private void appendLog(String text) {
        log.append(text + "\n");
    }

    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);

    }

    private Component createHeaderPanel() {
        jPanel = new JPanel(new GridLayout(2, 3));
        IPAddress = new JTextField("12.0.1.1");
        Port = new JTextField("3422");
        fLogin = new JTextField("Petr Petrovich");
        passwordField = new JPasswordField("223311");
        jButtonLogin = new JButton("Login");
        jButtonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        jPanel.add(IPAddress);
        jPanel.add(Port);
        jPanel.add(new JPanel());
        jPanel.add(fLogin);
        jPanel.add(passwordField);
        jPanel.add(jButtonLogin);

        return jPanel;
    }

    private Component createLog() {
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        Message = new JTextField();
        Message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    message();
                }
            }
        });
        jButtonSend = new JButton("send");
        jButtonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        panel.add(Message);
        panel.add(jButtonSend, BorderLayout.EAST);
        return panel;


    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSED) {
            disconnectFromServer();
        }
        super.processWindowEvent(e);
    }
}
