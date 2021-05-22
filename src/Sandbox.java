import rubIQs.*;
import java.util.*;
import static rubIQs.RubiksCube.colors.*;
import static rubIQs.RubiksCube.direction.*;

public class Sandbox {
    static public void main(String[] args) {
        RubiksCube r = new RubiksCube();
        r.printSides();
        r.scramble(10);
        r.printSides();
    }
}
