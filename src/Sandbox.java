import rubIQs.*;
import java.util.*;
import static rubIQs.BetterCube.Color.*;
import static rubIQs.BetterCube.Direction.*;

public class Sandbox {
    static public void main(String[] args) {
        BetterCube r = new BetterCube();
        r.traceScramble(10);
        r.printSides();
    }
}
