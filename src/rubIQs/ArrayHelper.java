package rubIQs;

import rubIQs.RubiksCube.Color;

public class ArrayHelper {

    /**
     * Reverses an array of sides
     * @param array - The array of cube sides to be reversed
     * @return - The reversed array
     */
    public static Color[][][] reverse(Color[][][] array) {
        Color[][][] reversed = new Color[array.length][][];
        for (int i=0; i<array.length; i++) {
            reversed[array.length-1-i] = array[i];
        }
        return reversed;
    }

    public static Color[] reverseKeepFirst(Color[] array) {
        Color[] newArr = new Color[array.length];
        newArr[0] = array[0];
        for (int i=0; i<array.length - 1; i++) {
            newArr[array.length-1-i] = array[i+1];
        }
        return newArr;
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
     * Rotates an array clockwise by 90 degrees
     * NOTE: This does not rotate the cube, but only how it is displayed
     * @param side - The 3x3 representation of the side to be rotated
     */
    public static RubiksCube.Color[][] rotateArray(RubiksCube.Color[][] side) {
        RubiksCube.Color[][] thisSide = side.clone();
        int n = thisSide.length;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                RubiksCube.Color temp = thisSide[i][j];
                thisSide[i][j] = thisSide[j][i];
                thisSide[j][i] = temp;
            }
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n / 2; ++j) {
                RubiksCube.Color temp = thisSide[i][j];
                thisSide[i][j] = thisSide[i][n - j - 1];
                thisSide[i][n - j - 1] = temp;
            }
        }

        return thisSide;
    }

    /**
     *
     * @param c - the element being indexed
     * @param array - The array to search
     * @return - The index of c in the array. -1 if array does not contain c
     */
    public static int indexOf(Color c, Color[] array) {
        for (int i=0; i<array.length; i++) {
            if (array[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
