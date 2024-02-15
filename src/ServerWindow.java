import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class ServerWindow extends JFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private static final String LOG_PATH = "src/log.txt";
    List<Clients> clientsList;

    JButton jButtonStart, jButtonStop;
    JTextArea log;
    boolean work;


    public ServerWindow() {
        clientsList = new ArrayList<>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat");
        setLocationRelativeTo(null);

        createPanel();
        setVisible(true);

    }

    private void createPanel() {
        log = new JTextArea();
        add(log);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        jButtonStart = new JButton("Start");
        jButtonStop = new JButton("Stop");

        jButtonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (work) {
                    appendLog("Сервер запущен");
                }else {
                    work = true;
                    appendLog("Cервер запущен");
                }

            }
        });
        jButtonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!work) {
                    appendLog("Сервер остановлен");
                } else {
                    work = false;
                    while (!clientsList.isEmpty()) {
                        disconnectClients(clientsList.get(clientsList.size() - 1));
                    }
                    appendLog("Сервер остановлен");
                }

            }


        });
        panel.add(jButtonStart);
        panel.add(jButtonStop);
        return panel;
    }

    public String getLog() {
        return readLog();
    }

    public void disconnectClients(Clients clients) {
        clientsList.remove(clients);
        if (clients != null) {
            clients.disconnectFromServer();
        }
    }

    public boolean connectClients(Clients clients) {
        if (!work) {
            return false;
        }
        clientsList.add(clients);
        return true;

    }


    private void answerAll(String text) {
        for (Clients clients : clientsList) {
            clients.answer(text);

        }
    }

    private void seveInLog(String text) {
        try (FileWriter fileWriter = new FileWriter(LOG_PATH, true)) {
            fileWriter.write(text);
            fileWriter.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readLog() {
        StringBuilder sb = new StringBuilder();
        try (FileReader fileReader = new FileReader(LOG_PATH);) {
            int r;
            while ((r = fileReader.read()) != -1) {
                sb.append((char) r);
            }
            sb.delete(sb.length() - 1, sb.length());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void message(String text) {
        if (!work) {
            return;
        }
        appendLog(text);
        answerAll(text);
        seveInLog(text);
    }

    private void appendLog(String text) {
        log.append(text + "\n");
    }


}
