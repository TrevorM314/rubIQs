import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.awt.Color;

import rubIQs.ArrayHelper;
import rubIQs.BetterCube;

public class CubeVisualizer extends JPanel{
    BetterCube cube;
    BetterCube.Color topSide;
    BetterCube.Color frontSide;

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

    CubeVisualizer(BetterCube cube) {
        this.setPreferredSize(new Dimension(500, 500));
        this.cube = cube;
        this.topSide = BetterCube.Color.BLUE;
        this.frontSide = BetterCube.Color.WHITE;
    }

    public void paint(Graphics og) {
        Graphics2D g = (Graphics2D) og;

        drawCube(frontSide, topSide, g);
    }

    public void topToFront() {
        BetterCube.Color leftSide = this.findLeftSide(frontSide, topSide);
        BetterCube.Color newTop = this.findLeftSide(leftSide, topSide);
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

    private void drawCube(BetterCube.Color frontColor, BetterCube.Color topColor, Graphics2D g) {
        drawFront(frontColor, topColor, g);
        drawTop(topColor, g);
        drawLeft(frontColor, topColor, g);
    }

    private void drawFront(BetterCube.Color frontColor, BetterCube.Color topColor, Graphics2D g) {
        BetterCube.Color[][] visualFront = ArrayHelper.deepCopy(cube.getCube()[frontColor.asInt()]);
        BetterCube.Color[] adjacentSides = calculateAdjacentSides(frontColor); //Have to make function still
        int numRotations = 0;


        g.setColor(Color.BLACK);
        g.fillRect(frontTopLeft-gapSpace, frontTopLeft-gapSpace, squareSize*3 + gapSpace*4, squareSize*3 + gapSpace*4);
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(toAwtColor(visualFront[row][col]));
                g.fillRect(frontTopLeft + squareSize*col + gapSpace*col, frontTopLeft  + squareSize*row + gapSpace*row, squareSize, squareSize);
            }
        }
    }

    private void drawTop(BetterCube.Color topColor, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(
                new int[] {topTopLeft-gapSpace, topTopLeft + cubeLength-gapSpace, frontTopLeft + cubeLength-gapSpace, frontTopLeft-gapSpace},
                new int[] {topTopLeft-zGapOffset, topTopLeft-gapSpace, frontTopLeft-gapSpace, frontTopLeft-zGapOffset},
                4
        );
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(toAwtColor(cube.getCube()[topColor.asInt()][row][col]));
                int squareTopLeftX = topTopLeft + gapSpace+squareSize*col + gapSpace*col + zSquareOffset*row + zGapOffset*row;
                int squareTopLeftY = topTopLeft + zSquareOffset*row + zGapOffset*row;
                int[] xPoints = {squareTopLeftX, squareTopLeftX+squareSize, squareTopLeftX+zSquareOffset+squareSize, squareTopLeftX+zSquareOffset};
                int[] yPoints = {squareTopLeftY, squareTopLeftY, squareTopLeftY+zSquareOffset, squareTopLeftY+zSquareOffset};
                g.fillPolygon(xPoints, yPoints, 4);
            }
        }
    }

    private void drawLeft(BetterCube.Color frontColor, BetterCube.Color topColor, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(
                new int[] {topTopLeft-gapSpace, frontTopLeft-gapSpace, frontTopLeft-gapSpace, topTopLeft-gapSpace},
                new int[] {topTopLeft-zGapOffset, frontTopLeft-gapSpace, frontTopLeft+cubeLength-gapSpace, topTopLeft-zGapOffset+cubeLength-gapSpace},
                4
        );
        BetterCube.Color leftColor = findLeftSide(frontColor, topColor);
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(toAwtColor(cube.getCube()[leftColor.asInt()][row][col]));
                int colMult = 2-col;
                int squareTopLeftX = topTopLeft + zSquareOffset*row + zGapOffset*row;
                int squareTopLeftY = topTopLeft+ zSquareOffset*row + zGapOffset*row + squareSize*colMult + gapSpace*colMult;
                int[] xPoints = {squareTopLeftX, squareTopLeftX+zSquareOffset, squareTopLeftX+zSquareOffset, squareTopLeftX};
                int[] yPoints = {squareTopLeftY, squareTopLeftY+zSquareOffset, squareTopLeftY+zSquareOffset+squareSize, squareTopLeftY+squareSize};
                g.fillPolygon(xPoints, yPoints, 4);
            }
        }
    }

    private BetterCube.Color findLeftSide(BetterCube.Color frontColor, BetterCube.Color topColor) {
        BetterCube.Color[] counterClockwiseOrder;
        switch (frontColor) {
            default:
            case WHITE:
                counterClockwiseOrder = new BetterCube.Color[] {BetterCube.Color.BLUE, BetterCube.Color.ORANGE, BetterCube.Color.GREEN, BetterCube.Color.RED};
                break;
            case YELLOW:
                counterClockwiseOrder = new BetterCube.Color[] {BetterCube.Color.BLUE, BetterCube.Color.RED, BetterCube.Color.GREEN, BetterCube.Color.ORANGE};
                break;
            case BLUE:
                counterClockwiseOrder = new BetterCube.Color[] {BetterCube.Color.YELLOW, BetterCube.Color.ORANGE, BetterCube.Color.WHITE, BetterCube.Color.RED};
                break;
            case RED:
                counterClockwiseOrder = new BetterCube.Color[] {BetterCube.Color.YELLOW, BetterCube.Color.BLUE, BetterCube.Color.WHITE, BetterCube.Color.GREEN};
                break;
            case GREEN:
                counterClockwiseOrder = new BetterCube.Color[] {BetterCube.Color.YELLOW, BetterCube.Color.RED, BetterCube.Color.WHITE, BetterCube.Color.ORANGE};
                break;
            case ORANGE:
                counterClockwiseOrder = new BetterCube.Color[] {BetterCube.Color.YELLOW, BetterCube.Color.GREEN, BetterCube.Color.WHITE, BetterCube.Color.BLUE};
                break;
        }

        return counterClockwiseOrder[(indexOf(counterClockwiseOrder, topColor) + 1) % 4];
    }

    private BetterCube.Color findRightSide(BetterCube.Color frontColor, BetterCube.Color topColor) {
        BetterCube.Color rightSide = frontColor;
        while (findLeftSide(rightSide, topColor) != frontColor) {
            rightSide = findLeftSide(rightSide, topColor);
        }
        return rightSide;
    }

    private int indexOf(BetterCube.Color[] array, BetterCube.Color query) {
        for (int i=0; i<array.length; i++) {
            if (array[i] == query) {
                return i;
            }
        }
        return -1;
    }

    private Color toAwtColor(BetterCube.Color c) {
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
