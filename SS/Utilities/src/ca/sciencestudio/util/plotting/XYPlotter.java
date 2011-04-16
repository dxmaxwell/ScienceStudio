/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     XYPlotter class.
 *     
 */
package ca.sciencestudio.util.plotting;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author medrand
 *
 */
public class XYPlotter extends Canvas {
	
	private static final long serialVersionUID = 1L;
	
	private final int width = 800;
	private final int height = 600;
	private final int offset1 = 50;
	private final int offset2 = 10;
	private final int xOrigin = 50;
	private final int yOrigin = 500;
	private final int axisWidth = width - 100;
	private final int axisHeight = height - 100;
	private double maxEnergy;
	private int[] data;
	private String xAxisLabel;
	private String yAxisLabel;
	private int xMax, xMin, yMax, yMin;
	private double xScale, yScale;
	
	
	private XYPlotter() {
		this.setBounds(0, 0, width, height);
		this.setBackground(Color.WHITE);
	}
	
	private XYPlotter(int[] data, double maxEnergy) {
		this();
		this.data = data;
		this.maxEnergy = maxEnergy;
	}
	
	public XYPlotter(int[] data, double maxEnergy, String xAxisLabel, String yAxisLabel) {
		this(data, maxEnergy);
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		initParameters();
	}
	
	public void paint(Graphics graphics) {
		graphics.setColor(Color.GREEN);

		int x0 = xOrigin;//the x-origin
		int y0 = yOrigin;//the y-origin
		int x1 = x0;
		int y1 = y0;

		//Now loop and plot the points
		for (int i = 0; i < xMax; i++) {

			x1 = xOrigin + (int) (i * xScale);
			y1 = yOrigin - (int) (data[i] * yScale);
			// draw the next line segment
			graphics.drawLine(x0, y0, x1, y1);

			//Save end point to use as start
			// point for next line segment.
			x0 = x1;
			y0 = y1;
		}//end while loop
		
		drawBothAxis(graphics);
		drawXTics(graphics);
		drawYTics(graphics);
		drawXAxisLabel(graphics);
		drawYAxisLabel(graphics);
	}
	
	public BufferedImage getImage() {
		Rectangle rec = this.getBounds();
		BufferedImage image = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, rec.width, rec.height);
		this.paint(graphics);
		return image;
	}
	
	private void initParameters() {
		xMax = data.length;
		xMin = 0;
		yMin = getMin(data);
		yMax = getMax(data);
		xScale = (axisWidth * 0.92) / (xMax - xMin);
		yScale = (axisHeight * 0.92) / (yMax - yMin);
	}
	
	private void drawBothAxis(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.drawLine(xOrigin, yOrigin, axisWidth, yOrigin);
		graphics.drawLine(xOrigin, yOrigin, xOrigin, yOrigin - axisHeight);
	}
	
	private void drawXAxisLabel(Graphics graphics) {
		FontMetrics fm = graphics.getFontMetrics();
		Rectangle2D xLblBox = fm.getStringBounds(xAxisLabel, graphics);
		graphics.drawString(xAxisLabel, width / 2 - (int) xLblBox.getWidth() / 2,
				(height - offset2 - 50) + (int) xLblBox.getHeight() / 2);
	}
	
	private void drawYAxisLabel(Graphics graphics) {
		FontMetrics fm = graphics.getFontMetrics();
		Graphics2D g2d = (Graphics2D) graphics;
		AffineTransform at = new AffineTransform();
		at.setToRotation(-Math.PI / 2.0, width / 2,
				height / 2);
		g2d.setTransform(at);
		Rectangle2D yLblBox = fm.getStringBounds(yAxisLabel, g2d);
		g2d.drawString(yAxisLabel, width / 2 - (int) yLblBox.getWidth() / 2,
				-offset1 - 42);
	}
	
	private void drawXTics(Graphics graphics) {
		int ticInterval;
		int divisor = 10;

		ticInterval = (xMax + (10 - (xMax % 10))) / divisor;
		//ticInterval = xMax / divisor;

		for (int i = 0; i <= (ticInterval * divisor); i += ticInterval) {
			int x1 = (int) (xOrigin + xScale * i);
			int y1 = yOrigin;
			int x2 = x1;
			int y2 = y1 + 5;

			//chk if we need to clamp this tick...
			if (y1 < (xOrigin - axisWidth)) {
				x1 = (int) (xOrigin - xScale * xMax);
				x2 = x1;
			}
			graphics.drawLine(x1, y1, x2, y2);
			//label it...
			String val = Integer.toString((int) (i * (maxEnergy / xMax)));
			FontMetrics fm = graphics.getFontMetrics();
			Rectangle2D valBox = fm.getStringBounds(val, graphics);
			graphics.drawString(val, x1 - (int) valBox.getWidth() / 2, y2 + 5
					+ (int) valBox.getHeight() / 2);

		}
	}
	
	private void drawYTics(Graphics graphics) {
		int ticInterval;
		int divisor = 10;
		//round max axis value up such that mod(val,10)==0...
		ticInterval = yMax / divisor;

		for (int i = 0; i <= (ticInterval * divisor); i += ticInterval) {
			int x1 = xOrigin;
			int y1 = (int) (yOrigin - yScale * i);
			int x2 = x1 - 5;
			int y2 = y1;

			//chk if we need to clamp this tick...
			if (y1 < (yOrigin - axisHeight)) {
				y1 = (int) (yOrigin - yScale * yMax);
				y2 = y1;
			}
			graphics.drawLine(x1, y1, x2, y2);
			String val = Integer.toString((int)i);
			FontMetrics fm = graphics.getFontMetrics();
			Rectangle2D valBox = fm.getStringBounds(val, graphics);
			graphics.drawString(val, x2 - 5 - (int) valBox.getWidth(), y2
					+ (int) valBox.getHeight() / 2);
		}
	}
	
	private int getMin(int[] data) {
		int min = 0;
		for(int i : data) {
			if(i < min)
				min = i;
		}
		return min;
	}
	
	private int getMax(int[] data) {
		int max = 100;
		for(int i : data) {
			if(i > max)
				max = i;
		}
		return max;
	}
}
