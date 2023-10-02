import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ServerWindow extends JFrame {
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 400;
    private final int WINDOW_POS_X = 800;
    private final int WINDOW_POS_Y = 300;
    private final String TITLE = "Chat server";
    private final String BTN_START = "Start";
    private final String BTN_STOP = "Stop";
    private final String SERVER_STARTED = "Server is started";
    private final String SERVER_STOPPED = "Server is stopped";
    private final String SERVER_INACTIVE = "Server is inactive";
    private final String SERVER_ISRUNNING = "Server is already running";
    private final String PATH_HOSTS_DIR = "./src/hosts";
    private final String CHAT_FILE = "Chat.txt";
    private final String LOGS_FILE = "logs.txt";

    private boolean serverState;
    JTextArea jtaLogServer;

    public ServerWindow() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE); //завершать программу при закрытие окна сервера

        setLocation(WINDOW_POS_X, WINDOW_POS_Y); // позиция окна
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // размер окна
        setTitle(TITLE); // Заголовок окна
        setResizable(false); //Запретить менять размеры окна
        serverState = false;

        jtaLogServer = new JTextArea();
        jtaLogServer.setEditable(false); //запретить редактировать с клавиатуры
        add(jtaLogServer); // текстовое поле

        add(createStartStopPanel(), BorderLayout.SOUTH); // панель с кнопками start и stop
        setVisible(true); // делаем видимым
    }

    /**
     * Создание южной панели
     *
     * @return
     */
    private Component createStartStopPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        JButton btnStart = new JButton(BTN_START);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverState) {
                    sendTo(SERVER_ISRUNNING); // сервер уже включён
                } else {
                    serverState = true; // включить сервер
                    sendTo(SERVER_STARTED);
                }
            }
        });
        JButton btnStop = new JButton(BTN_STOP);
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverState) {
                    serverState = false; // отключить сервер
                    sendTo(SERVER_STOPPED);
                }
            }
        });
        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }

    /**
     * Проверка активности сервера
     *
     * @return
     */
    public boolean isServerState() {
        return serverState;
    }

    /**
     * Получает из клиента host и ip формирует строку для логирования.
     *
     * @param client
     * @return
     */
    public boolean connectToServer(ClientGUI client) {
        if (serverState) {
            String s = String.format("host:%s, ip:%s is connected",
                    client.getHost(), client.getIp());
            String msg = addTimeMsg(s);
            writeLog(msg, client.getHost());
            jtaLogServer.append(msg);
        }
        return serverState;
    }

    /**
     * Геттер неактивного статуса для невозможности залогинится при отключенном сервере
     *
     * @return
     */
    public String getSERVER_INACTIVE() {
        return SERVER_INACTIVE;
    }

    /**
     * Запись в лог файл клиента о его активности
     *
     * @param msg
     * @param host
     */
    private void writeLog(String msg, String host) {
        File logs = new File(checkHostDir(host), LOGS_FILE);
        try (FileWriter fw = new FileWriter(logs, true);) {
            fw.write(msg);
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    /**
     * вывод в лог сервера сообщения с добавлением времени
     *
     * @param msg
     */
    private void sendTo(String msg) {
        jtaLogServer.append(addTimeMsg(msg));
    }

    /**
     * Добавить время к строке
     *
     * @param s
     * @return
     */
    private String addTimeMsg(String s) {
        LocalTime time = LocalTime.now();
        String t = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String msg = String.format("%s: %s\n", t, s);
        return msg;
    }

    /**
     * Проверяет наличие дириктории у хоста и создаёт её, если она отстустствует
     *
     * @param host имя директории
     * @return директория хоста
     */
    private File checkHostDir(String host) {
        File hostDir = new File(String.format("%s/%s", PATH_HOSTS_DIR, host));
        if (!hostDir.exists()) {
            hostDir.mkdirs();
        }
        return hostDir;
    }

    /**
     * Записывает чат в файл. Если дириктории у хоста нет, то создаёт её.
     *
     * @param host
     * @param msg
     */
    public void writeChat(String host, String msg) {
        File chat = new File(checkHostDir(host), CHAT_FILE);
        try (FileWriter fw = new FileWriter(chat, true);) {
            fw.write(msg);
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    public String loadChat(String host) {
        File hostDir = checkHostDir(host);
        File chat = new File(hostDir, CHAT_FILE);
        if (chat.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(chat))) {
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
                return sb.toString();
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }
        return "";
    }

}
