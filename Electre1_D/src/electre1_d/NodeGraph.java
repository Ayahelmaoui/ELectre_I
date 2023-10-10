
package electre1_d;

import javax.swing.*;
import java.awt.*;

public class NodeGraph extends JFrame {

    private int circleCount;
    private int spacing;
    private int[][] connections;

    public NodeGraph(int circleCount, int spacing, int[][] connections) {
        this.circleCount = circleCount;
        this.spacing = spacing;
        this.connections = connections;
        setTitle("Node Graph");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Set the color and thickness of the circles and arcs
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Calculate the circle radius based on the circle count, spacing, and frame dimensions
        int minDimension = Math.min(getWidth(), getHeight());
        int radius = minDimension / (circleCount * 2 + (circleCount - 1) * spacing);
        int circleSize = radius * 2;

        // Draw the specified number of circles with spacing
        for (int i = 0; i < circleCount; i++) {
            int x = centerX + (i - circleCount / 2) * (circleSize + spacing);
            int y = centerY;
            g.drawOval(x - radius, y - radius, circleSize, circleSize);
            g.drawString(String.valueOf(i + 1), x, y);
        }

        // Draw arcs between the connected circles
        for (int i = 0; i < connections.length; i++) {
            int source = connections[i][0];
            int target = connections[i][1];
            
            // Calculate the coordinates of the source and target circles
            int startX = centerX + (source - circleCount / 2) * (circleSize + spacing);
            int startY = centerY;
            int endX = centerX + (target - circleCount / 2) * (circleSize + spacing);
            int endY = centerY;
            
            // Calculate the control point coordinates for a curved arc
            int controlX = (startX + endX) / 2;
            int controlY = (startY + endY) / 2 - (radius + spacing) * 2;
            
            // Draw a curved arc between the source and target circles

            g.drawLine(startX + radius, startY, controlX, controlY);
            g.drawLine(endX + radius, endY, controlX, controlY);
        }
    }

}
