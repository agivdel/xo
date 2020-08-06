package Agivdel.XO.Interface;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel panel;
    private final int IMAGE_SIZE = 200;
    private final int COLUMNS = 3;
    private final int ROWS = 3;

    public MainFrame() {
        setImages();
        initPanel();
        initFrame();
    }

    private void initPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Box box : Box.values())
                    g.drawImage((Image)box.image,box.ordinal()*IMAGE_SIZE, 0, this);
            }
        };
        panel.setPreferredSize(new Dimension(COLUMNS*IMAGE_SIZE, ROWS*IMAGE_SIZE));
        add(panel);
    }

    private void initFrame() {
        setTitle("Крестики-нолики");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
//        setLocationRelativeTo(null);//если центрировать окно здесь (до pack()), то центрируется левый верхний угол окна
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    private void setImages() {
        for (Box box : Box.values()) {
            box.image = getImage(box.name().toLowerCase());//у каждого объекта класса Box в поле image помещаем соответствующее изображение
        }
    }

    private Image getImage(String name) {
        String fileName = "/xo_image/" + name + ".png";//слеш перед именем папки обязательно!
        ImageIcon icon = new ImageIcon(getClass().getResource(fileName));//метод getResource ищет файл с именем fileName в корне (корневой папке) ресурсов
        return icon.getImage();
    }


}
