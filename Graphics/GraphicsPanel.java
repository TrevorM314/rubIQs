import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.awt.Color;
import rubIQs.RubiksCube;
import rubIQs.RubiksCube.colors;

public class GraphicsPanel extends JPanel{
    RubiksCube cube;

    //Cube Size & Location
    private final double zMod = .75;
    private final int squareSize = 20; //Best to use even numbers
    private int zSquareSize = (int) Math.ceil(squareSize * zMod);
    private final int gapSpace = 2;
    private final int zGapSpace = (int) Math.ceil(gapSpace * zMod);;
    private final int frontTopLeft = (int) (250-(squareSize*1.5)-gapSpace);
    private final int cubeLength = gapSpace * 4 + squareSize * 3;

    int zEdge = zGapSpace*4 + zSquareSize*3;
    int zSquareOffset = (int)( Math.sqrt(Math.pow(zSquareSize, 2) / 2) );
    int zGapOffset = (int)( Math.sqrt(Math.pow(zGapSpace, 2) / 2) );
    int offset = (int)( Math.sqrt(Math.pow(zEdge-zGapSpace, 2) / 2) );
    int topTopLeft = frontTopLeft-gapSpace - offset;

    GraphicsPanel(RubiksCube cube) {
        this.setPreferredSize(new Dimension(500, 500));
        this.cube = cube;
    }

    public void paint(Graphics og) {
        Graphics2D g = (Graphics2D) og;

        drawCube(colors.WHITE, colors.BLUE, g);
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
                new int[] {topTopLeft-zGapOffset, topTopLeft + cubeLength-gapSpace, frontTopLeft + cubeLength-gapSpace, frontTopLeft-gapSpace},
                new int[] {topTopLeft-zGapOffset, topTopLeft-gapSpace, frontTopLeft-gapSpace, frontTopLeft-zGapOffset},
                4
        );
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                g.setColor(cube.getCube()[topColor.asInt()][row][col].asAwtColor());
                int squareTopLeftX = topTopLeft + squareSize*col + gapSpace*col + zSquareOffset*row + zGapOffset*row;
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
                new int[] {topTopLeft-zGapOffset, frontTopLeft-gapSpace, frontTopLeft-gapSpace, topTopLeft-zGapOffset},
                new int[] {topTopLeft-zGapOffset, frontTopLeft-gapSpace, frontTopLeft+cubeLength-gapSpace, topTopLeft-zGapOffset+cubeLength-gapSpace},
                4
        );
    }
}
