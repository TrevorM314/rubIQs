import javax.swing.*;
import rubIQs.RubiksCube;
import java.awt.*;
import java.awt.event.*;

public class GraphicsFrame extends JFrame implements ActionListener {
    CubeVisualizer rubiksVisualizer;
    Button topToFront, leftToFront, rightToFront, bottomToFront;

    GraphicsFrame(RubiksCube r) {
        rubiksVisualizer = new CubeVisualizer(r);
        rubiksVisualizer.setLayout(null);

        topToFront = new Button("^");
        topToFront.setBounds(220, 10, 20, 20);
        topToFront.addActionListener(this);
        rubiksVisualizer.add(topToFront);

        leftToFront = new Button("<");
        leftToFront.setBounds(0, 240, 20, 20);
        leftToFront.addActionListener(this);
        rubiksVisualizer.add(leftToFront);

        bottomToFront = new Button("v");
        bottomToFront.setBounds(240, 470, 20, 20);
        bottomToFront.addActionListener(this);
        rubiksVisualizer.add(bottomToFront);

        rightToFront = new Button(">");
        rightToFront.setBounds(470, 240, 20, 20);
        rightToFront.addActionListener(this);
        rubiksVisualizer.add(rightToFront);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Rubiks Solver");
        this.add(rubiksVisualizer);
        this.pack();
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == topToFront) {
            rubiksVisualizer.topToFront();
        }
        else if (source == leftToFront) {
            rubiksVisualizer.leftToFront();
        }
        else if (source == bottomToFront) {
            rubiksVisualizer.bottomToFront();
        }
        else if (source == rightToFront) {
            rubiksVisualizer.rightToFront();
        }
        this.paint(getGraphics());
    }
}
