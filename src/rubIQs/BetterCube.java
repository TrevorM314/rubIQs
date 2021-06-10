package rubIQs;

import java.util.Arrays;
import java.util.Random;

import rubIQs.ArrayHelper;
import static rubIQs.BetterCube.Color.*;
import static rubIQs.BetterCube.Direction.*;

public class BetterCube {
    /**
     *
     */
    private Color[][][] cube = new Color[6][3][3]; //Represented as cube[side][row][col]
    private final int cubeSize = 3;

    public enum Color {
        WHITE(0, 'W'),
        BLUE(1, 'B'),
        RED(2, 'R'),
        YELLOW(3, 'Y'),
        GREEN(4, 'G'),
        ORANGE(5, 'O');

        private final int index;
        private final char C;

        Color (int index, char C) {
            this.index = index;
            this.C = C;
        }

        public int asInt() {
            return this.index;
        }
        public int asChar() {
            return this.C;
        }
    }

    public enum Direction {
        CLOCKWISE,
        COUNTER_CLOCKWISE
    }

    private class RelativeSides {
        public Color[][] top, right, left, bottom, back;
        RelativeSides(Color front) {
            int topInt = (front.asInt() + 1 + 3*(front.asInt()%2)) % 6;
            this.top = cube[ topInt ];
            this.bottom = cube[ (topInt + 3) % 6 ];
            int leftInt = (front.asInt() + 5) % 6;
            this.left = cube[ leftInt ];
            this.right = cube[ (leftInt + 3) % 6 ];
            this.back = cube[ (front.asInt() + 3) % 6 ];
        }
    }

    public BetterCube() {
        /* Initialize to a new, Solved rubik's Cube */
        for (Color color : Color.values()) {
            Color[][] side = new Color[3][3];
            for (int row=0; row<3; row++) {
                Arrays.fill(side[row], color);
            }
            cube[color.asInt()] = side;
        }
    }

    public void useTestCube() {
        /* Log
        0: GREEN CLOCKWISE
        1: WHITE COUNTER_CLOCKWISE
        2: WHITE COUNTER_CLOCKWISE
        3: BLUE CLOCKWISE
        4: BLUE CLOCKWISE
        5: YELLOW CLOCKWISE
        6: ORANGE COUNTER_CLOCKWISE
        7: BLUE COUNTER_CLOCKWISE
        8: GREEN CLOCKWISE
        9: YELLOW COUNTER_CLOCKWISE
        10: ORANGE COUNTER_CLOCKWISE
        11: RED CLOCKWISE
        */

        cube[WHITE.asInt()] = new Color[][] {{ORANGE, RED, ORANGE}, {ORANGE, WHITE, BLUE}, {WHITE, GREEN, YELLOW}};
        cube[BLUE.asInt()] = new Color[][] {{GREEN, GREEN, RED}, {WHITE, BLUE, RED}, {GREEN, WHITE, RED}};
        cube[RED.asInt()] = new Color[][] {{BLUE, YELLOW, WHITE}, {ORANGE, RED, BLUE}, {YELLOW, WHITE, BLUE}};
        cube[YELLOW.asInt()] = new Color[][] {{WHITE, BLUE, YELLOW}, {BLUE, YELLOW, RED}, {RED, RED, RED}};
        cube[GREEN.asInt()] = new Color[][] {{GREEN, WHITE, ORANGE}, {GREEN, GREEN, ORANGE}, {GREEN, ORANGE, ORANGE}};
        cube[ORANGE.asInt()] = new Color[][] {{YELLOW, YELLOW, BLUE}, {GREEN, ORANGE, YELLOW}, {BLUE, YELLOW, WHITE}};
    }

    /**
     *
     * @return cubeClone - a copy of the cube representation
     */
    public Color[][][] getCube() {
        Color[][][] cubeClone = new Color[6][3][3];
        for (Color side: Color.values()) {
            for (int row=0; row<3; row++) {
                System.arraycopy(cube[side.asInt()][row], 0, cubeClone[side.asInt()][row], 0, 3);
            }
        }
        return cubeClone;
    }

    public boolean isSolved() {
        for (Color color : Color.values()) {
            for (int row=0; row<3; row++) {
                for (int col=0; col<3; col++) {
                    if (cube[color.asInt()][row][col] != color) return false;
                }
            }
        }
        return true;
    }

    /**
     * Scrambles a rubiks cube
     * @param iterations - How many moves should the scrambler perform on the cube (default 30)
     */
    public void scramble(int iterations) {
        Color previousSide = null;
        Direction previousDir = null;
        for (int i=0; i<iterations; i++) {
            Color side = Color.values()[new Random().nextInt(6)];
            Direction dir = Direction.values()[new Random().nextInt(2)];
            //Make sure that new move doesn't just undo previous move
            while (previousSide != null && side == previousSide && dir != previousDir) {
                side = Color.values()[new Random().nextInt(6)];
                dir = Direction.values()[new Random().nextInt(2)];
            }
            this.rotate(side, dir);
            previousSide = side;
            previousDir = dir;
        }
    }

    public void scramble() {
        this.scramble(30);
    }

    public void traceScramble(int iterations) {
        Color previousSide = null;
        Direction previousDir = null;
        for (int i=0; i<iterations; i++) {
            Color side = Color.values()[new Random().nextInt(6)];
            Direction dir = Direction.values()[new Random().nextInt(2)];
            //Make sure that new move doesn't just undo previous move
            while (previousSide != null && side == previousSide && dir != previousDir) {
                side = Color.values()[new Random().nextInt(6)];
                dir = Direction.values()[new Random().nextInt(2)];
            }
            this.rotate(side, dir);
            System.out.printf("%d: %s %s\n", i, side, dir);
            previousSide = side;
            previousDir = dir;
        }
    }

    /**
     * Rotates a side of the rubiks cube - both the face and the adjacent squares on other sides.
     * @param side - The side being rotated
     * @param dir - The direction of rotation (CLOCKWISE or COUNTER_CLOCKWISE
     */
    public void rotate(Color side, Direction dir) {
        RelativeSides sides = new RelativeSides(side);

        rotateFace(side, dir);
        for (int i=0; i<3; i++) {
            if (side.asInt()%2 == 0 /* Even */) {
                Color temp = sides.top[i][0];
                if (dir == CLOCKWISE) {
                    sides.top[i][0] = sides.left[2][i];
                    sides.left[2][i] = sides.bottom[2 - i][2];
                    sides.bottom[2 - i][2] = sides.right[2][i];
                    sides.right[2][i] = temp;
                }
                else {
                    sides.top[i][0] = sides.right[2][i];
                    sides.right[2][i] = sides.bottom[2 - i][2];
                    sides.bottom[2 - i][2] = sides.left[2][i];
                    sides.left[2][i] = temp;
                }
            }
            else /* Odd */ {
                Color temp = sides.top[2-i][2];
                if (dir == CLOCKWISE) {
                    sides.top[2-i][2] = sides.left[0][2-i];
                    sides.left[0][2-i] = sides.bottom[i][0];
                    sides.bottom[i][0] = sides.right[0][2-i];
                    sides.right[0][2-i] = temp;
                }
                else {
                    sides.top[2-i][2] = sides.right[0][2-i];
                    sides.right[0][2-i] = sides.bottom[i][0];
                    sides.bottom[i][0] = sides.left[0][2-i];
                    sides.left[0][2-i] = temp;
                }
            }
        }
    }


    /**
     * Helper function to rotate the current side, (not the adjacent rows though)
     * Credit: Rahul Verma - https://www.geeksforgeeks.org/rotate-a-matrix-by-90-degree-in-clockwise-direction-without-using-any-extra-space/
     * @param side - The side being rotated
     * @param dir - The direction of rotation: CLOCKWISE or COUNTER_CLOCKWISE
     */
    private void rotateFace(Color side, Direction dir) {
        Color[][] thisSide = cube[side.asInt()];
        int n = thisSide.length;

        if (dir == BetterCube.Direction.CLOCKWISE) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < i; ++j) {
                    Color temp = thisSide[i][j];
                    thisSide[i][j] = thisSide[j][i];
                    thisSide[j][i] = temp;
                }
            }
        }
        else /*dir == COUNTER_CLOCKWISE*/ {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - i; j++) {
                    Color temp = thisSide[i][j];
                    thisSide[i][j] = thisSide[n - 1 - j][n - 1 - i];
                    thisSide[n - 1 - j][n - 1 - i] = temp;
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n / 2; ++j) {
                Color temp = thisSide[i][j];
                thisSide[i][j] = thisSide[i][n - j - 1];
                thisSide[i][n - j - 1] = temp;
            }
        }
    }


    /**
     * Prints out a human readable cube format.
     * NOTE: The second display shows the sides as if you were looking through
     * the cube at them, not as if you were looking directly at the side
     */
    public void printSides() {
        char[] colorInitials = {'W', 'B', 'R', 'G', 'O', 'Y'};

        System.out.print("      -------------          -------------\n");
        System.out.printf("     / %C / %C / %C /|         /| %C | %C | %C |\n",
                cube[BLUE.asInt()][0][2].asChar(),
                cube[BLUE.asInt()][1][2].asChar(),
                cube[BLUE.asInt()][2][2].asChar(),
                cube[YELLOW.asInt()][0][2].asChar(),
                cube[YELLOW.asInt()][0][1].asChar(),
                cube[YELLOW.asInt()][0][0].asChar()
        );
        System.out.printf("    /-----------/%C/        /%C|---+---+---|\n",
                cube[RED.asInt()][0][0].asChar(),
                cube[ORANGE.asInt()][0][2].asChar()
        );
        System.out.printf("   / %C / %C / %C /|/|       /|/| %C | %C | %C |\n",
                cube[BLUE.asInt()][0][1].asChar(),
                cube[BLUE.asInt()][1][1].asChar(),
                cube[BLUE.asInt()][2][1].asChar(),
                cube[YELLOW.asInt()][1][2].asChar(),
                cube[YELLOW.asInt()][1][1].asChar(),
                cube[YELLOW.asInt()][1][0].asChar()
        );
        System.out.printf("  /-----------/%C/%C/      /%C/%C|---+---+---|\n",
                cube[RED.asInt()][1][0].asChar(),
                cube[RED.asInt()][0][1].asChar(),
                cube[ORANGE.asInt()][1][2].asChar(),
                cube[ORANGE.asInt()][0][1].asChar()
        );
        System.out.printf(" / %C / %C / %C /|/|/|     /|/|/| %C | %C | %C |\n",
                cube[BLUE.asInt()][0][0].asChar(),
                cube[BLUE.asInt()][1][0].asChar(),
                cube[BLUE.asInt()][2][0].asChar(),
                cube[YELLOW.asInt()][2][2].asChar(),
                cube[YELLOW.asInt()][2][1].asChar(),
                cube[YELLOW.asInt()][2][0].asChar()
        );
        System.out.printf("|-----------|%C/%C/%C/    /%C/%C/%C|-----------|\n",
                cube[RED.asInt()][2][0].asChar(),
                cube[RED.asInt()][1][1].asChar(),
                cube[RED.asInt()][0][2].asChar(),
                cube[ORANGE.asInt()][2][2].asChar(),
                cube[ORANGE.asInt()][1][1].asChar(),
                cube[ORANGE.asInt()][0][0].asChar()
        );
        System.out.printf("| %C | %C | %C |/|/|/     |/|/|/ %C / %C / %C /\n",
                cube[WHITE.asInt()][0][0].asChar(),
                cube[WHITE.asInt()][0][1].asChar(),
                cube[WHITE.asInt()][0][2].asChar(),
                cube[GREEN.asInt()][0][0].asChar(),
                cube[GREEN.asInt()][1][0].asChar(),
                cube[GREEN.asInt()][2][0].asChar()
        );
        System.out.printf("|---+---+---|%C/%C/      /%C/%C/---/---/---/\n",
                cube[RED.asInt()][2][1].asChar(),
                cube[RED.asInt()][1][2].asChar(),
                cube[ORANGE.asInt()][2][1].asChar(),
                cube[ORANGE.asInt()][1][0].asChar()
        );
        System.out.printf("| %C | %C | %C |/|/       |/|/ %C / %C / %C /\n",
                cube[WHITE.asInt()][1][0].asChar(),
                cube[WHITE.asInt()][1][1].asChar(),
                cube[WHITE.asInt()][1][2].asChar(),
                cube[GREEN.asInt()][0][1].asChar(),
                cube[GREEN.asInt()][1][1].asChar(),
                cube[GREEN.asInt()][2][1].asChar()
        );
        System.out.printf("|---+---+---|%C/        /%C/---/---/---/\n",
                cube[RED.asInt()][2][2].asChar(),
                cube[ORANGE.asInt()][2][0].asChar()
        );
        System.out.printf("| %C | %C | %C |/         |/ %C / %C / %C /\n",
                cube[WHITE.asInt()][2][0].asChar(),
                cube[WHITE.asInt()][2][1].asChar(),
                cube[WHITE.asInt()][2][2].asChar(),
                cube[GREEN.asInt()][0][2].asChar(),
                cube[GREEN.asInt()][1][2].asChar(),
                cube[GREEN.asInt()][2][2].asChar()
        );
        System.out.print("-------------          -------------\n");
    }
}
