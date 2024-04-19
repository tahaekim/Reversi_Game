package reversi;

import javax.swing.*;
import java.awt.*;

public class GUIViewBoardSquare extends JButton {
    private static final int SIZE = 50;
    private static final Color BORDER_COLOR = Color.BLACK;

    private int content;

    public GUIViewBoardSquare() {
        setPreferredSize(new Dimension(SIZE, SIZE));
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        setContentAreaFilled(false);
        setOpaque(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, SIZE, SIZE);

        if (content == 1) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval(5, 5, SIZE - 10, SIZE - 10);
        } else if (content == 2) {
            g2d.setColor(Color.BLACK);
            g2d.fillOval(5, 5, SIZE - 10, SIZE - 10);
        }

        g2d.dispose();
    }

    public void setContent(int content) {
        this.content = content;
        repaint();
    }
    
    public void setSelected(boolean selected) {
        if (selected) {
            setContentAreaFilled(true);
            if (content == 1) {
                setBackground(Color.WHITE);
            } else if (content == 2) {
                setBackground(Color.BLACK);
            }
        } else {
            setContentAreaFilled(false);
            setBackground(Color.GREEN);
        }
    }
}
