package rubIQs;

import rubIQs.BetterCube.Color;

public class ArrayHelper {

    public static Color[][][] reverse(Color[][][] array) {
        Color[][][] reversed = new Color[array.length][][];
        for (int i=0; i<array.length; i++) {
            reversed[array.length-1-i] = array[i];
        }
        return reversed;
    }

    /**
     * Deep Copies a 3x3 array of Colors
     * @param side the 3x3 side to be copied
     * @return a deep copy of the array
     */
    public static Color[][] deepCopy(Color[][] side) throws IllegalArgumentException {
        if (side.length != 3) throw new IllegalArgumentException("the argument must be a 3x3 array");
        Color[][] copy = new Color[3][3];
        for (int row=0; row<3; row++) {
            if (side[row].length != 3) throw new IllegalArgumentException("the argument must be a 3x3 array");
            System.arraycopy(side[row], 0, copy[row], 0, 3);
        }
        return copy;
    }

    /**
     * Rotates an array clockwise
     * NOTE: This does not rotate the cube, but only how it is displayed
     * @param side - The 3x3 representation of the side to be rotated
     */
    private static BetterCube.Color[][] rotateArray(BetterCube.Color[][] side) {
        BetterCube.Color[][] thisSide = side.clone();
        int n = thisSide.length;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                BetterCube.Color temp = thisSide[i][j];
                thisSide[i][j] = thisSide[j][i];
                thisSide[j][i] = temp;
            }
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n / 2; ++j) {
                BetterCube.Color temp = thisSide[i][j];
                thisSide[i][j] = thisSide[i][n - j - 1];
                thisSide[i][n - j - 1] = temp;
            }
        }

        return thisSide;
    }
}
