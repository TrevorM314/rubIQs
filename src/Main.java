import java.awt.Canvas;

import rubIQs.*;
import static rubIQs.RubiksCube.colors.*;

public class Main extends Canvas {
    static public void main(String[] args) {
        System.out.println("Launching");

        RubiksCube cube = new RubiksCube();
        GraphicsFrame rubiksVisualizer = new GraphicsFrame(cube);
        cube.rotate(WHITE, RubiksCube.direction.CLOCKWISE);
    }
}
