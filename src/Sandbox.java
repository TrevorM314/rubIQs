import rubIQs.*;
import java.util.*;
import static rubIQs.RubiksCube.colors.*;
import static rubIQs.RubiksCube.direction.*;

public class Sandbox {
    static public void main(String[] args) {
        RubiksCube r = new RubiksCube();
        r.printSides();
        rubIQs.RubiksCube.colors[][][] myCube = r.getCube();
        myCube[WHITE.asInt()][0][0] = GREEN;
        r.printSides();
    }
}
