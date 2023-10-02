import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientGUI extends JFrame {
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 400;
    private final int WINDOW_POS_X = 900;
    private final int WINDOW_POS_Y = 400;
    private final String TITLE = "Chat client";
    private final String BTN_LOGIN = "login";
    private final String BTN_SEND = "send";
    private final String JTF_IP = "127.0.0.1";
    private final String JTF_HOST = "Ivan Ivanovich";
    private final String JTF_SOCKET = "8189";
    private final String JTF_PWD = "12345678";
    private final String CONNECTED = "You are connected to server";

    private JTextArea jtaChat;
    private JTextField jtfIp, jtfSocket, jtfHost, jtfSend;
    private JPasswordField jtfPwd;
    private JButton btnLogin, btnSend;
    private ServerWindow server;
    private String host, ip, socket;
    private boolean isLogined;


    public ClientGUI(ServerWindow serverWindow) throws HeadlessException {
        server = serverWindow;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocation(WINDOW_POS_X, WINDOW_POS_Y);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle(TITLE);
        setResizable(false);

        jtaChat = new JTextArea();
        jtaChat.setEditable(false); //запретить редактировать с клавиатуры

        add(createTopPanel(), BorderLayout.NORTH);
        add(jtaChat);
        add(createBotPanel(), BorderLayout.SOUTH);

        setVisible(true); // делаем видимым
    }

    private Component createBotPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField jtfSend = new JTextField();
        btnSend = new JButton(BTN_SEND);
        btnSend.setEnabled(false); // кнопка неактивна

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.isServerState()) { // проверка активности сервера
                    String msg = jtfSend.getText();
                    if (msg.length() == 0) {
                        return;
                    }
                    msg = addTimeMsg(jtfSend.getText()); // добавление времени к
                    jtfSend.setText("");  // удаление содержимого в текстовом поле
                    server.writeChat(host, msg);
                    jtaChat.append(msg); // отправка в чат сообщение из текстового поля
                } else {
                    btnSend.setEnabled(false); // отключение кнопки
                    jtaChat.append(addTimeMsg(server.getSERVER_INACTIVE()));
                }
            }
        });

        // отправка по интеру
        jtfSend.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSend.doClick(); // жмём send там все проверки
                }
            }
        });

        // панель с двумя компонентами
        panel.add(jtfSend);
        panel.add(btnSend, BorderLayout.EAST);

        return panel;
    }

    /**
     * Создаёт верхнюю панель
     *
     * @return
     */
    private Component createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3));
        JTextField jtfIp = new JTextField();
        jtfIp.setText(JTF_IP);
        JTextField jtfSocket = new JTextField();
        jtfSocket.setText(JTF_SOCKET);
        panel.add(jtfIp);
        panel.add(jtfSocket);

        // вставка пустой ячейки
        panel.add(new JPanel(new GridLayout()));

        JTextField jtfHost = new JTextField();
        jtfHost.setText(JTF_HOST);
        JTextField jtfPwd = new JPasswordField();
        jtfPwd.setText(JTF_PWD);
        isLogined = false;
        btnLogin = new JButton(BTN_LOGIN);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.isServerState()) {
                    if (!isLogined) { // если залогинились, не делать этого повторно.
                        isLogined = true;
                        host = jtfHost.getText();
                        ip = jtfIp.getText();
                        jtaChat.setText("");
                        jtaChat.append(server.loadChat(host)); // выгружаем чат с сервера
                        server.connectToServer(ClientGUI.this); // отдаём серверу клиента для логирования
                        jtaChat.append(addTimeMsg(CONNECTED));
                        btnSend.setEnabled(true); // активация кнопки отправить
                    }
                } else {
                    isLogined = false;
                    jtaChat.append(addTimeMsg(server.getSERVER_INACTIVE()));
                }
            }
        });
        panel.add(jtfHost);
        panel.add(jtfPwd);
        panel.add(btnLogin);

        return panel;
    }

    // геттеры для сервера
    public String getIp() {
        return ip;
    }

    public String getHost() {
        return host;
    }

    // время для сообщений
    private String addTimeMsg(String s) {
        LocalTime time = LocalTime.now();
        String t = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String msg = String.format("%s: %s\n", t, s);
        return msg;
    }
}
