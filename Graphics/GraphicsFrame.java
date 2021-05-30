import javax.swing.*;
import rubIQs.RubiksCube;

public class GraphicsFrame  extends JFrame{
    GraphicsPanel rubiksVisualizer;

    GraphicsFrame(RubiksCube r) {
        rubiksVisualizer = new GraphicsPanel(r);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Rubiks Solver");
        this.add(rubiksVisualizer);
        this.pack();
        this.setVisible(true);
    }
}
