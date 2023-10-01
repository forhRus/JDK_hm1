import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerWindow extends JFrame {
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 400;
    private final int WINDOW_POS_X = 800;
    private final int WINDOW_POS_Y = 300;
    private  final String TITLE = "Chat server";
    private  final String BTN_START = "Start";
    private  final String BTN_STOP = "Stop";
    JTextArea textArea;
    StringBuilder sbTextArea;


    public ServerWindow() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE); //завершать программу при закрытие окна сервера

        setLocation(WINDOW_POS_X, WINDOW_POS_Y); // позиция окна
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // размер окна
        setTitle(TITLE); // Заголовок окна
        setResizable(false); //Запретить менять размеры окна

        textArea = new JTextArea();
        textArea.setEditable(false); //запретить редактировать с клавиатуры
        add(textArea); // текстовое поле

        add(createStartStopPanel(), BorderLayout.SOUTH); // панель с кнопками start и stop
        setVisible(true); // делаем видимым
    }



    private Component createStartStopPanel(){
        JPanel panel = new JPanel(new GridLayout(1, 2));
        JButton btnStart = new JButton(BTN_START);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("append");
            }
        });
        JButton btnStop = new JButton(BTN_STOP);
        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }


}
