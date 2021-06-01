import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.awt.Color;
import rubIQs.RubiksCube;
import rubIQs.RubiksCube.colors;

public class CubeVisualizer extends JPanel{
    RubiksCube cube;
    colors topSide;
    colors frontSide;

    //Cube Size & Location
    private final double zMod = .75;
    private final int squareSize = 50; //Best to use even numbers
    private int zSquareSize = (int) Math.ceil(squareSize * zMod);
    private final int gapSpace = 2;
    private final int zGapSpace = (int) Math.ceil(gapSpace * zMod);;
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
        this.topSide = colors.BLUE;
        this.frontSide = colors.WHITE;
    }

    public void paint(Graphics og) {
        Graphics2D g = (Graphics2D) og;

        drawCube(frontSide, topSide, g);
    }

    public void topToFront() {
        colors leftSide = this.findLeftSide(frontSide, topSide);
        colors newTop = this.findLeftSide(leftSide, topSide);
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

    private void drawCube(colors frontColor, colors topColor, Graphics2D g) {
        drawFront(frontColor, g);
        drawTop(topColor, g);
        drawLeft(frontColor, topColor, g);
    }

    private void drawFront(colors frontColor, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(frontTopLeft-gapSpace, frontTopLeft-gapSpace, squareSize*3 + gapSpace*4, squareSize*3 + gapSpace*4);
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(cube.getCube()[frontColor.asInt()][row][col].asAwtColor());
                g.fillRect(frontTopLeft + squareSize*col + gapSpace*col, frontTopLeft  + squareSize*row + gapSpace*row, squareSize, squareSize);
            }
        }
    }

    private void drawTop(colors topColor, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(
                new int[] {topTopLeft-gapSpace, topTopLeft + cubeLength-gapSpace, frontTopLeft + cubeLength-gapSpace, frontTopLeft-gapSpace},
                new int[] {topTopLeft-zGapOffset, topTopLeft-gapSpace, frontTopLeft-gapSpace, frontTopLeft-zGapOffset},
                4
        );
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(cube.getCube()[topColor.asInt()][row][col].asAwtColor());
                int squareTopLeftX = topTopLeft + gapSpace+squareSize*col + gapSpace*col + zSquareOffset*row + zGapOffset*row;
                int squareTopLeftY = topTopLeft + zSquareOffset*row + zGapOffset*row;
                int[] xPoints = {squareTopLeftX, squareTopLeftX+squareSize, squareTopLeftX+zSquareOffset+squareSize, squareTopLeftX+zSquareOffset};
                int[] yPoints = {squareTopLeftY, squareTopLeftY, squareTopLeftY+zSquareOffset, squareTopLeftY+zSquareOffset};
                g.fillPolygon(xPoints, yPoints, 4);
            }
        }
    }

    private void drawLeft(colors frontColor, colors topColor, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(
                new int[] {topTopLeft-gapSpace, frontTopLeft-gapSpace, frontTopLeft-gapSpace, topTopLeft-gapSpace},
                new int[] {topTopLeft-zGapOffset, frontTopLeft-gapSpace, frontTopLeft+cubeLength-gapSpace, topTopLeft-zGapOffset+cubeLength-gapSpace},
                4
        );
        colors leftColor = findLeftSide(frontColor, topColor);
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(cube.getCube()[leftColor.asInt()][row][col].asAwtColor());
                int colMult = 2-col;
                int squareTopLeftX = topTopLeft + zSquareOffset*row + zGapOffset*row;
                int squareTopLeftY = topTopLeft+ zSquareOffset*row + zGapOffset*row + squareSize*colMult + gapSpace*colMult;
                int[] xPoints = {squareTopLeftX, squareTopLeftX+zSquareOffset, squareTopLeftX+zSquareOffset, squareTopLeftX};
                int[] yPoints = {squareTopLeftY, squareTopLeftY+zSquareOffset, squareTopLeftY+zSquareOffset+squareSize, squareTopLeftY+squareSize};
                g.fillPolygon(xPoints, yPoints, 4);
            }
        }
    }

    private colors findLeftSide(colors frontColor, colors topColor) {
        colors[] counterClockwiseOrder;
        switch (frontColor) {
            default:
            case WHITE:
                counterClockwiseOrder = new colors[] {colors.BLUE, colors.ORANGE, colors.GREEN, colors.RED};
                break;
            case YELLOW:
                counterClockwiseOrder = new colors[] {colors.BLUE, colors.RED, colors.GREEN, colors.ORANGE};
                break;
            case BLUE:
                counterClockwiseOrder = new colors[] {colors.YELLOW, colors.ORANGE, colors.WHITE, colors.RED};
                break;
            case RED:
                counterClockwiseOrder = new colors[] {colors.YELLOW, colors.BLUE, colors.WHITE, colors.GREEN};
                break;
            case GREEN:
                counterClockwiseOrder = new colors[] {colors.YELLOW, colors.RED, colors.WHITE, colors.ORANGE};
                break;
            case ORANGE:
                counterClockwiseOrder = new colors[] {colors.YELLOW, colors.GREEN, colors.WHITE, colors.BLUE};
                break;
        }

        return counterClockwiseOrder[(indexOf(counterClockwiseOrder, topColor) + 1) % 4];
    }

    private colors findRightSide(colors frontColor, colors topColor) {
        colors rightSide = frontColor;
        colors initialFront = frontColor;
        while (findLeftSide(rightSide, topColor) != initialFront) {
            rightSide = findLeftSide(rightSide, topColor);
        }
        return rightSide;
    }

    private int indexOf(colors[] array, colors query) {
        for (int i=0; i<array.length; i++) {
            if (array[i] == query) {
                return i;
            }
        }
        return -1;
    }
}
