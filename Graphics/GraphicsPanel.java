import javax.swing.*;
import java.awt.*;

public class GraphicsPanel extends JPanel{
    GraphicsPanel() {
        this.setPreferredSize(new Dimension(500, 500));
    }

    public void paint(Graphics og) {
        Graphics2D g = (Graphics2D) og;

        drawCube(0, g);
    }

    private void drawCube(int frontColor, Graphics2D g) {
        g.drawRect(5,5,20,20);
    }
}
