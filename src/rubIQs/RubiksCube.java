package rubIQs;

import java.util.*;
import static rubIQs.RubiksCube.colors.*;
import static rubIQs.RubiksCube.direction.*;

public class RubiksCube {
    /**
     * a 3d array representing the cube. The first index corresponds to the side,
     * which can be accessed by the enumeration. The first side is white, which is
     * the front side, then blue is top, red is right, green is bottom, orange is
     * left, and yellow is the back. The side's color is determined by the center
     * piece, which never changes location.
     * The next 2 indexes are row and column.
     * For the yellow and white sides, side[0][0] is the top left corner when looking
     * at the side with green at the bottom. For all other sides, side[0][0] is the
     * top left corner when looking at it with white at the bottom.
     */
    private colors[][][] cube = new colors[6][3][3]; //Represented as cube[side][row][col]
    private final int cubeSize = 3;

    public enum colors {
        WHITE(0, 'W'),
        BLUE(1, 'B'),
        RED(2, 'R'),
        GREEN(3, 'G'),
        ORANGE(4, 'O'),
        YELLOW(5, 'Y');

        private final int index;
        private final char C;

        colors (int index, char C) {
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

    public enum direction {
        CLOCKWISE,
        COUNTER_CLOCKWISE
    }

    public RubiksCube() {
        /*Initialize to a new, Solved rubik's Cube*/
        for (colors color : colors.values()) {
            colors[][] side = new colors[3][3];
            for (int row=0; row<3; row++) {
                Arrays.fill(side[row], color);
            }
            cube[color.asInt()] = side;
        }

        /*Test Cube*/
        /*
        cube = new colors[][][] {
                {{colors.ORANGE, colors.BLUE, colors.RED}, {colors.ORANGE, colors.WHITE, colors.WHITE}, {colors.BLUE, colors.WHITE, colors.ORANGE}},
                {{colors.GREEN, colors.YELLOW, colors.ORANGE}, {colors.GREEN, colors.BLUE, colors.BLUE}, {colors.GREEN, colors.RED, colors.YELLOW}},
                {{colors.WHITE, colors.WHITE, colors.ORANGE}, {colors.YELLOW, colors.RED, colors.WHITE}, {colors.GREEN, colors.BLUE, colors.YELLOW}},
                {{colors.YELLOW, colors.GREEN, colors.BLUE}, {colors.RED, colors.GREEN, colors.YELLOW}, {colors.BLUE, colors.GREEN, colors.RED}},
                {{colors.RED, colors.GREEN, colors.WHITE}, {colors.RED, colors.ORANGE, colors.YELLOW}, {colors.WHITE, colors.BLUE, colors.WHITE}},
                {{colors.BLUE, colors.ORANGE, colors.RED}, {colors.ORANGE, colors.YELLOW, colors.ORANGE}, {colors.GREEN, colors.RED, colors.YELLOW}}
        };
        */
    }

    /**
     * Scrambles a rubiks cube
     * @param iterations - How many moves should the scrambler perform on the cube (default 30)
     */
    public void scramble(int iterations) {
        colors previousSide = null;
        direction previousDir = null;
        for (int i=0; i<iterations; i++) {
            colors side = colors.values()[new Random().nextInt(6)];
            direction dir = direction.values()[new Random().nextInt(2)];
            //Make sure that new move doesn't just undo previous move
            while (previousSide != null && side == previousSide && dir != previousDir) {
                side = colors.values()[new Random().nextInt(6)];
                dir = direction.values()[new Random().nextInt(2)];
            }
            this.rotate(side, dir);
            previousSide = side;
            previousDir = dir;
        }
    }

    public void scramble() {
        this.scramble(30);
    }

    /**
     * Rotates a side of the rubiks cube - both the face and the adjacent squares on other sides.
     * @param side - The side being rotated
     * @param dir - The direction of rotation (CLOCKWISE or COUNTER_CLOCKWISE
     */
    public void rotate(colors side, direction dir) {
        this.rotateFace(side, dir);

        colors[] nextChange, newTemp;

        int adjSide;
        if (side==WHITE) {
            if (dir == direction.CLOCKWISE) nextChange = cube[ORANGE.asInt()][2].clone();
            else nextChange = cube[BLUE.asInt()][2].clone();
            //Iterate through each side in blue, red, green, orange
            for (int i = 1; i < 5; i++) {
                if (dir == direction.CLOCKWISE) adjSide = i;
                else adjSide = 5 - i;
                newTemp = cube[adjSide][2].clone();
                cube[adjSide][2] = nextChange.clone();
                nextChange = newTemp.clone();
            }
        }
        else if (side == YELLOW) {
            if (dir == direction.CLOCKWISE) nextChange = cube[BLUE.asInt()][0].clone();
            else nextChange = cube[ORANGE.asInt()][0].clone();
            for (int i = 1/*BLUE*/; i < 5; i++) {
                if (dir == direction.CLOCKWISE) adjSide = 5-i;
                else adjSide = i;
                newTemp = cube[adjSide][0].clone();
                cube[adjSide][0] = nextChange.clone();
                nextChange = newTemp.clone();
            }
        }
        else if (side == BLUE) {
            if (dir == CLOCKWISE) {
                for (int i=0; i<3; i++) {
                    colors temp = cube[YELLOW.asInt()][0][i];
                    cube[YELLOW.asInt()][0][i] = cube[ORANGE.asInt()][i][2];
                    cube[ORANGE.asInt()][i][2] = cube[WHITE.asInt()][0][i];
                    cube[WHITE.asInt()][0][i] = cube[RED.asInt()][2-i][0];
                    cube[RED.asInt()][2-i][0] = temp;
                }
            }
            else {
                for (int i=0; i<3; i++) {
                    colors temp = cube[YELLOW.asInt()][0][i];
                    cube[YELLOW.asInt()][0][i] = cube[RED.asInt()][2-i][0];
                    cube[RED.asInt()][2-i][0] = cube[WHITE.asInt()][0][i];
                    cube[WHITE.asInt()][0][i] = cube[ORANGE.asInt()][i][2];
                    cube[ORANGE.asInt()][i][2] = temp;
                }
            }
        }
        else if (side == RED) {
            if (dir == CLOCKWISE) {
                for (int i=0; i<3; i++) {
                    colors temp = cube[YELLOW.asInt()][i][0];
                    cube[YELLOW.asInt()][i][0] = cube[BLUE.asInt()][2-i][2];
                    cube[BLUE.asInt()][2-i][2] = cube[WHITE.asInt()][2-i][2];
                    cube[WHITE.asInt()][2-i][2] = cube[GREEN.asInt()][i][0];
                    cube[GREEN.asInt()][i][0] = temp;
                }
            }
            else {
                for (int i=0; i<3; i++) {
                    colors temp = cube[YELLOW.asInt()][i][0];
                    cube[YELLOW.asInt()][i][0] = cube[GREEN.asInt()][i][0];
                    cube[GREEN.asInt()][i][0] = cube[WHITE.asInt()][2-i][2];
                    cube[WHITE.asInt()][2-i][2] = cube[BLUE.asInt()][2-i][2];
                    cube[BLUE.asInt()][2-i][2] = temp;
                }
            }
        }
        else if (side == GREEN) {
            if (dir == CLOCKWISE) {
                for (int i=0; i<3; i++) {
                    colors temp = cube[YELLOW.asInt()][2][i];
                    cube[YELLOW.asInt()][2][i] = cube[RED.asInt()][2-i][2];
                    cube[RED.asInt()][2-i][2] = cube[WHITE.asInt()][2][i];
                    cube[WHITE.asInt()][2][i] = cube[ORANGE.asInt()][i][0];
                    cube[ORANGE.asInt()][i][0] = temp;
                }
            }
            else {
                for (int i=0; i<3; i++) {
                    colors temp = cube[YELLOW.asInt()][2][i];
                    cube[YELLOW.asInt()][2][i] = cube[ORANGE.asInt()][i][0];
                    cube[ORANGE.asInt()][i][0] = cube[WHITE.asInt()][2][i];
                    cube[WHITE.asInt()][2][i] = cube[RED.asInt()][2-i][2];
                    cube[RED.asInt()][2-i][2] = temp;
                }
            }
        }
        else if (side == ORANGE) {
            if (dir == CLOCKWISE) {
                for (int i=0; i<3; i++) {
                    colors temp = cube[YELLOW.asInt()][2-i][2];
                    cube[YELLOW.asInt()][2-i][2] = cube[GREEN.asInt()][2-i][2];
                    cube[GREEN.asInt()][2-i][2] = cube[WHITE.asInt()][i][0];
                    cube[WHITE.asInt()][i][0] = cube[BLUE.asInt()][i][0];
                    cube[BLUE.asInt()][i][0] = temp;
                }
            }
            else {
                for (int i=0; i<3; i++) {
                    colors temp = cube[YELLOW.asInt()][2-i][2];
                    cube[YELLOW.asInt()][2-i][2] = cube[BLUE.asInt()][i][0];
                    cube[BLUE.asInt()][i][0] = cube[WHITE.asInt()][i][0];
                    cube[WHITE.asInt()][i][0] = cube[GREEN.asInt()][2-i][2];
                    cube[GREEN.asInt()][2-i][2] = temp;
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
    private void rotateFace(colors side, direction dir) {
        colors[][] thisSide = cube[side.asInt()];
        int n = thisSide.length;

        if (dir == direction.CLOCKWISE) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < i; ++j) {
                    colors temp = thisSide[i][j];
                    thisSide[i][j] = thisSide[j][i];
                    thisSide[j][i] = temp;
                }
            }
        }
        else /*dir == COUNTER_CLOCKWISE*/ {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - i; j++) {
                    colors temp = thisSide[i][j];
                    thisSide[i][j] = thisSide[n - 1 - j][n - 1 - i];
                    thisSide[n - 1 - j][n - 1 - i] = temp;
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n / 2; ++j) {
                colors temp = thisSide[i][j];
                thisSide[i][j] = thisSide[i][n - j - 1];
                thisSide[i][n - j - 1] = temp;
            }
        }
    }

    /**
     * This atrocious, unoptimized code prints out a human readable cube format.
     * NOTE: The second display shows the sides as if you were looking through
     * the cube at them, not as if you were looking directly at the side
     */
    public void printSides() {
        char[] colorInitials = {'W', 'B', 'R', 'G', 'O', 'Y'};

        System.out.print("      -------------          -------------\n");
        System.out.printf("     / %C / %C / %C /|         /| %C | %C | %C |\n",
                cube[BLUE.asInt()][0][0].asChar(),
                cube[BLUE.asInt()][0][1].asChar(),
                cube[BLUE.asInt()][0][2].asChar(),
                cube[YELLOW.asInt()][0][2].asChar(),
                cube[YELLOW.asInt()][0][1].asChar(),
                cube[YELLOW.asInt()][0][0].asChar()
        );
        System.out.printf("    /-----------/%C/        /%C|---+---+---|\n",
                cube[RED.asInt()][0][0].asChar(),
                cube[ORANGE.asInt()][0][2].asChar()
                );
        System.out.printf("   / %C / %C / %C /|/|       /|/| %C | %C | %C |\n",
                cube[BLUE.asInt()][1][0].asChar(),
                cube[BLUE.asInt()][1][1].asChar(),
                cube[BLUE.asInt()][1][2].asChar(),
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
                cube[BLUE.asInt()][2][0].asChar(),
                cube[BLUE.asInt()][2][1].asChar(),
                cube[BLUE.asInt()][2][2].asChar(),
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
                cube[GREEN.asInt()][0][2].asChar(),
                cube[GREEN.asInt()][0][1].asChar(),
                cube[GREEN.asInt()][0][0].asChar()
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
                cube[GREEN.asInt()][1][2].asChar(),
                cube[GREEN.asInt()][1][1].asChar(),
                cube[GREEN.asInt()][1][0].asChar()
        );
        System.out.printf("|---+---+---|%C/        /%C/---/---/---/\n",
                cube[RED.asInt()][2][2].asChar(),
                cube[ORANGE.asInt()][2][0].asChar()
        );
        System.out.printf("| %C | %C | %C |/         |/ %C / %C / %C /\n",
                cube[WHITE.asInt()][2][0].asChar(),
                cube[WHITE.asInt()][2][1].asChar(),
                cube[WHITE.asInt()][2][2].asChar(),
                cube[GREEN.asInt()][2][2].asChar(),
                cube[GREEN.asInt()][2][1].asChar(),
                cube[GREEN.asInt()][2][0].asChar()
        );
        System.out.print("-------------          -------------\n");
    }
}