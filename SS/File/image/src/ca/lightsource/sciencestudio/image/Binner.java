/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Binner class.
 *     
 */
package ca.lightsource.sciencestudio.image;

import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.Calibration;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;



/**
 *
 * Binning utility is based on the Averaging Reducer by Wayne Rasband (wsr at nih.gov)
 * The original ImageJ plugin is available <a href="http://rsbweb.nih.gov/ij/plugins/reducer.html">http://rsbweb.nih.gov/ij/plugins/reducer.html</a>
 *
 * @author Dong Liu
 *
 */
public class Binner {



	private int xshrink=4, yshrink=4; // the default value set to be 4


	private double product = xshrink*xshrink;



	int[] pixel = new int[3];
    int[] sum = new int[3];
    int samples;

    public Binner(int shrink) {
		this(shrink, shrink);
	}

    public Binner(int xshrink, int yshrink) {
		this.xshrink = xshrink;
		this.yshrink = yshrink;
		this.product = xshrink * yshrink;
	}

    public int getXshrink() {
		return xshrink;
	}


	public int getYshrink() {
		return yshrink;
	}

	public void setShrink(int xshrink, int yshrink) {
		this.xshrink = xshrink;
		this.yshrink = yshrink;
		this.product = xshrink * yshrink;
	}

	public double getProduct() {
		return product;
	}



    public ImagePlus shrink(ImagePlus imp) {
        ImageStack stack1 = imp.getStack();
        ImageStack stack2 = new ImageStack(imp.getWidth()/xshrink,imp.getHeight()/yshrink);
        int n = stack1.getSize();
        for (int i=1; i<=n; i++) {
//            IJ.showStatus(i+"/"+n);
//            IJ.showProgress(i, n);
            ImageProcessor ip2 = shrink(stack1.getProcessor(i));
            stack2.addSlice(null, ip2);
        }
        ImagePlus imp2 = new ImagePlus("Reduced "+imp.getShortTitle(), stack2);
        imp2.setCalibration(imp.getCalibration());
        Calibration cal2 = imp2.getCalibration();
        cal2.pixelWidth *= xshrink;
        cal2.pixelHeight *= yshrink;
        return imp2;
    }

    public ImageProcessor shrink(ImageProcessor ip) {
        if (ip instanceof FloatProcessor)
                    return shrinkFloat(ip);
        samples = ip instanceof ColorProcessor?3:1;
        int w = ip.getWidth()/xshrink;
        int h = ip.getHeight()/yshrink;
        ImageProcessor ip2 = ip.createProcessor(w, h);
        for (int y=0; y<h; y++)
            for (int x=0; x<w; x++)
                ip2.putPixel(x, y, getAverage(ip, x, y));
       return ip2;
    }

    int[] getAverage(ImageProcessor ip, int x, int y) {
         for (int i=0; i<samples; i++)
            sum[i] = 0;
         for (int y2=0; y2<yshrink; y2++) {
            for (int x2=0;  x2<xshrink; x2++) {
                pixel = ip.getPixel(x*xshrink+x2, y*yshrink+y2, pixel);
                for (int i=0; i<samples; i++)
                     sum[i] += pixel[i];
             }
        }
        for (int i=0; i<samples; i++)
            sum[i] = (int)(sum[i]/product+0.5);
       return sum;
    }

    /*boolean showDialog() {
        GenericDialog gd = new GenericDialog("Image Shrink");
        gd.addNumericField("X Shrink Factor:", xshrink, 0);
        gd.addNumericField("Y Shrink Factor:", yshrink, 0);
        gd.showDialog();
        if (gd.wasCanceled())
            return false;
        xshrink = (int) gd.getNextNumber();
        yshrink = (int) gd.getNextNumber();
        product = xshrink*yshrink;
        return true;
    }*/

   ImageProcessor shrinkFloat(ImageProcessor ip) {
        int w = ip.getWidth()/xshrink;
        int h = ip.getHeight()/yshrink;
        ImageProcessor ip2 = ip.createProcessor(w, h);
        for (int y=0; y<h; y++)
            for (int x=0; x<w; x++)
                ip2.putPixelValue(x, y, getFloatAverage(ip, x, y));
        return ip2;
    }

    float getFloatAverage(ImageProcessor ip, int x, int y) {
        double sum = 0.0;
        for (int y2=0; y2<yshrink; y2++)
            for (int x2=0;  x2<xshrink; x2++)
                sum += ip.getPixelValue(x*xshrink+x2, y*yshrink+y2);
        return (float)(sum/product);
    }

}
