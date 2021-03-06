import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.awt.Color;

import rubIQs.ArrayHelper;
import rubIQs.RubiksCube;

public class CubeVisualizer extends JPanel{
    RubiksCube cube;
    RubiksCube.Color topSide;
    RubiksCube.Color frontSide;

    //Cube Size & Location
    private final double zMod = .75;
    private final int squareSize = 50; //Best to use even numbers
    private final int zSquareSize = (int) Math.ceil(squareSize * zMod);
    private final int gapSpace = 2;
    private final int zGapSpace = (int) Math.ceil(gapSpace * zMod);
    private final int frontTopLeft = (int) (250-(squareSize*1.5)-gapSpace);
    private final int cubeLength = gapSpace * 4 + squareSize * 3;

    int zEdge = zGapSpace*4 + zSquareSize*3;
    int zSquareOffset = (int)( Math.sqrt(Math.pow(zSquareSize, 2) / 2) );
    int zGapOffset = (int)( Math.sqrt(Math.pow(zGapSpace, 2) / 2) );
    int offset = (int)( Math.sqrt(Math.pow(zEdge-zGapSpace, 2) / 2) );
    int topTopLeft = frontTopLeft - offset;

    CubeVisualizer(RubiksCube cube) {
        this.setPreferredSize(new Dimension(500, 500));
        this.cube = cube;
        this.topSide = RubiksCube.Color.BLUE;
        this.frontSide = RubiksCube.Color.WHITE;
    }

    public void paint(Graphics og) {
        Graphics2D g = (Graphics2D) og;

        drawCube(frontSide, topSide, g);
    }

    public RubiksCube.Color getFrontSide() {
        return this.frontSide;
    }

    /**
     *
     * @param side - The side whose edges are being calculated
     * @return - an array of colors, starting with the sides top adjacent side, ending with left
     */
    private RubiksCube.Color[] calculateAdjacentSides(RubiksCube.Color side) {
        RubiksCube.Color[] adjacentSides = new RubiksCube.Color[4];
        RubiksCube.Color[] colors = RubiksCube.Color.values();
        int sideNum = side.asInt();

        adjacentSides[0] = colors[ (sideNum + 1 + (3 * (sideNum%2))) % 6 ]; //Top
        adjacentSides[3] = colors[ (sideNum + 5) % 6 ]; //Left
        adjacentSides[1] = colors[ (adjacentSides[3].asInt() + 3) % 6 ]; //Right
        adjacentSides[2] = colors[ (adjacentSides[0].asInt() + 3) % 6 ]; //Bottom
        return adjacentSides;
    }

    public void topToFront() {
        RubiksCube.Color leftSide = this.findLeftSide(frontSide, topSide);
        RubiksCube.Color newTop = this.findLeftSide(leftSide, topSide);
        this.frontSide = topSide;
        this.topSide = newTop;
    }

    public void leftToFront() {
        this.frontSide = findLeftSide(frontSide, topSide);
    }

    public void rightToFront() {
        this.frontSide = findRightSide(frontSide, topSide);
    }

    public void bottomToFront() {
        for (int i=0; i<3; i++) {
            this.topToFront();
        }
    }

    private void drawCube(RubiksCube.Color frontColor, RubiksCube.Color topColor, Graphics2D g) {
        drawFront(frontColor, topColor, g);
        drawTop(frontColor, topColor, g);
        drawLeft(frontColor, topColor, g);
    }

    private void drawFront(RubiksCube.Color frontColor, RubiksCube.Color topColor, Graphics2D g) {
        RubiksCube.Color[][] visualFront = ArrayHelper.deepCopy(cube.getCube()[frontColor.asInt()]);
        RubiksCube.Color[] adjacentSides = calculateAdjacentSides(frontColor);
        adjacentSides = ArrayHelper.reverseKeepFirst(adjacentSides);
        int numRotations = 0; // Number of clockwise rotations to properly display the front side
        while (adjacentSides[numRotations] != topColor) {
            numRotations++;
        }

        for (int i=0; i<numRotations; i++) {
            ArrayHelper.rotateArray(visualFront);
        }

        g.setColor(Color.BLACK);
        g.fillRect(frontTopLeft-gapSpace, frontTopLeft-gapSpace, squareSize*3 + gapSpace*4, squareSize*3 + gapSpace*4);
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(toAwtColor(visualFront[row][col]));
                g.fillRect(frontTopLeft + squareSize*col + gapSpace*col, frontTopLeft  + squareSize*row + gapSpace*row, squareSize, squareSize);
            }
        }
    }

    private void drawTop(RubiksCube.Color frontColor, RubiksCube.Color topColor, Graphics2D g) {
        RubiksCube.Color[][] visualFront = ArrayHelper.deepCopy(cube.getCube()[topColor.asInt()]);
        RubiksCube.Color[] adjacentSides = calculateAdjacentSides(topColor);
        adjacentSides = ArrayHelper.reverseKeepFirst(adjacentSides); //Now iterating counter clockwise through adjacents
        int numRotations = 0; // Number of clockwise rotations to properly display the front side
        while (adjacentSides[(2+numRotations) % 4] != frontColor) {
            numRotations++;
        }

        for (int i=0; i<numRotations; i++) {
            ArrayHelper.rotateArray(visualFront);
        }

        g.setColor(Color.BLACK);
        g.fillPolygon(
                new int[] {topTopLeft-gapSpace, topTopLeft + cubeLength-gapSpace, frontTopLeft + cubeLength-gapSpace, frontTopLeft-gapSpace},
                new int[] {topTopLeft-zGapOffset, topTopLeft-gapSpace, frontTopLeft-gapSpace, frontTopLeft-zGapOffset},
                4
        );
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(toAwtColor(visualFront[row][col]));
                int squareTopLeftX = topTopLeft + gapSpace+squareSize*col + gapSpace*col + zSquareOffset*row + zGapOffset*row;
                int squareTopLeftY = topTopLeft + zSquareOffset*row + zGapOffset*row;
                int[] xPoints = {squareTopLeftX, squareTopLeftX+squareSize, squareTopLeftX+zSquareOffset+squareSize, squareTopLeftX+zSquareOffset};
                int[] yPoints = {squareTopLeftY, squareTopLeftY, squareTopLeftY+zSquareOffset, squareTopLeftY+zSquareOffset};
                g.fillPolygon(xPoints, yPoints, 4);
            }
        }
    }

    private void drawLeft(RubiksCube.Color frontColor, RubiksCube.Color topColor, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(
                new int[] {topTopLeft-gapSpace, frontTopLeft-gapSpace, frontTopLeft-gapSpace, topTopLeft-gapSpace},
                new int[] {topTopLeft-zGapOffset, frontTopLeft-gapSpace, frontTopLeft+cubeLength-gapSpace, topTopLeft-zGapOffset+cubeLength-gapSpace},
                4
        );
        RubiksCube.Color leftColor = findLeftSide(frontColor, topColor);

        RubiksCube.Color[][] visualLeft = ArrayHelper.deepCopy(cube.getCube()[leftColor.asInt()]);
        RubiksCube.Color[] adjacentSides = calculateAdjacentSides(leftColor);
        adjacentSides = ArrayHelper.reverseKeepFirst(adjacentSides);
        int numRotations = 0; // Number of clockwise rotations to properly display the front side
        while (adjacentSides[numRotations] != topColor) {
            numRotations++;
        }

        for (int i=0; i<numRotations; i++) {
            ArrayHelper.rotateArray(visualLeft);
        }

        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(toAwtColor(visualLeft[row][col]));

                int squareTopLeftX = topTopLeft + zSquareOffset*col + zGapOffset*col;
                int squareTopLeftY = topTopLeft+ zSquareOffset*col + zGapOffset*col + squareSize*row + gapSpace*row;
                int[] xPoints = {squareTopLeftX, squareTopLeftX+zSquareOffset, squareTopLeftX+zSquareOffset, squareTopLeftX};
                int[] yPoints = {squareTopLeftY, squareTopLeftY+zSquareOffset, squareTopLeftY+zSquareOffset+squareSize, squareTopLeftY+squareSize};
                g.fillPolygon(xPoints, yPoints, 4);
            }
        }
    }

    private RubiksCube.Color findLeftSide(RubiksCube.Color frontColor, RubiksCube.Color topColor) {
        RubiksCube.Color[] adjacentSides = calculateAdjacentSides(frontColor);
        int topIdx = ArrayHelper.indexOf(topColor, adjacentSides);
        int leftIdx = topIdx-1;
        if (leftIdx < 0) leftIdx += 4;
        return adjacentSides[leftIdx];
    }

    private RubiksCube.Color findRightSide(RubiksCube.Color frontColor, RubiksCube.Color topColor) {
        RubiksCube.Color rightSide = frontColor;
        while (findLeftSide(rightSide, topColor) != frontColor) {
            rightSide = findLeftSide(rightSide, topColor);
        }
        return rightSide;
    }

    private Color toAwtColor(RubiksCube.Color c) {
        Color awtColor;
        switch(c.asChar()) {
            case 'W':
                awtColor = Color.WHITE;
                break;
            case 'B':
                awtColor = Color.BLUE;
                break;
            case 'R':
                awtColor = Color.RED;
                break;
            case 'G':
                awtColor = Color.GREEN;
                break;
            case 'O':
                awtColor = new Color(255, 150, 0);
                break;
            case 'Y':
                awtColor = Color.YELLOW;
                break;
            default:
                awtColor = null;
        }
        return awtColor;
    }
}
