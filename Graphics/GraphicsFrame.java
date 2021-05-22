import javax.swing.*;

public class GraphicsFrame  extends JFrame{
    GraphicsPanel rubiksVisualizer;

    GraphicsFrame() {
        rubiksVisualizer = new GraphicsPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Rubiks Solver");
        this.add(rubiksVisualizer);
        this.pack();
        this.setVisible(true);
    }
}
