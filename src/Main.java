import java.awt.Canvas;

import rubIQs.*;
import static rubIQs.RubiksCube.colors.*;

public class Main extends Canvas {
    static public void main(String[] args) {
        System.out.println("Launching");

        BetterCube cube = new BetterCube();
        cube.traceScramble(5);
        GraphicsFrame rubiksVisualizer = new GraphicsFrame(cube);
        cube.printSides();
    }
}
