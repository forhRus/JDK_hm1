import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private final String JTF_SOCET = "8189";
    private final String JTF_PWD = "12345678";
    JTextArea textArea;
    JTextField jtfIp, jtfSocet, jtfHost, jtfSend;
    JPasswordField jtfPwd;
    JButton btnLogin, btnSend;
    StringBuilder sbTextArea;


    public ClientGUI() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocation(WINDOW_POS_X, WINDOW_POS_Y);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle(TITLE);
        setResizable(false);

        textArea = new JTextArea();
        textArea.setEditable(false); //запретить редактировать с клавиатуры


        add(createTopPanel(), BorderLayout.NORTH);
        add(textArea);
        add(createBotPanel(), BorderLayout.SOUTH);
        
        setVisible(true); // делаем видимым
    }

    private Component createBotPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        JTextField jtfSend = new JTextField();
        btnSend = new JButton(BTN_SEND);
        btnSend.setEnabled(false);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String send = jtfSend.getText();
                jtfSend.setText("");
                textArea.append(send+"\n");
            }
        });
        panel.add(jtfSend);
        panel.add(btnSend, BorderLayout.EAST);

        return panel;
    }

    /**
     * Создаёт верхнюю панель
     * @return
     */
    private Component createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3));
        JTextField jtfIp = new JTextField();
        jtfIp.setText(JTF_IP);
        JTextField jtfSocet = new JTextField();
        jtfSocet.setText(JTF_SOCET);
        panel.add(jtfIp);
        panel.add(jtfSocet);

        // вставка пустой ячейки
        panel.add(new JPanel(new GridLayout()));

        JTextField jtfHost = new JTextField();
        jtfHost.setText(JTF_HOST);
        JTextField jtfPwd = new JPasswordField();
        jtfPwd.setText(JTF_PWD);
        btnLogin = new JButton(BTN_LOGIN);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSend.setEnabled(true);
            }
        });
        panel.add(jtfHost);
        panel.add(jtfPwd);
        panel.add(btnLogin);

        return panel;
    }


}
