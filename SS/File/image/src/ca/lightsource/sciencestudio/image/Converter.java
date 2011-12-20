/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Converter class.
 *     
 */
package ca.lightsource.sciencestudio.image;

import ij.CompositeImage;
import ij.ImageJ;
import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.Opener;
import ij.io.TiffEncoder;
import ij.measure.Calibration;
import ij.process.ImageProcessor;

import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.jetty.util.log.Log;

import com.objectplanet.image.PngEncoder;

public class Converter {

	public static void convert(String source, String format, int binX, int binY, String lut, boolean reverse, String encoder, File dest)
			throws IOException {

		/* load image from source */

		long start = System.currentTimeMillis();
		Opener opener = new Opener();
		ImagePlus image = null;
		ImagePlus image2 = null;
		image = opener.openImage(source);

		if (image == null) {
			Log.warn(source + "cannot be opened. ");
			return;
		}
		if (binX > 1 || binY > 1) {
			Binner binner = new Binner(binX, binY);
			image2 = binner.shrink(image);
		} else {
			image2 = image;
		}

		// init lut
		IJLUT ijLut = new IJLUT(lut);
		
		
		// apply lut 
		if (ijLut.getLutName().equalsIgnoreCase(lut)) {
			ijLut.applyLut(image2);
		}
		
		// revert lut 
		if (reverse) {
			ijLut.invertLut(image2);
		}
		
		
		int outType = ImageType.getType(format);
		switch (outType) {
		case ImageType.TIFF:

			FileInfo fileInfo = image2.getFileInfo();
			if (fileInfo.nImages > 1) {
				if (fileInfo.nImages == 1) {
					System.err.println("This is not a stack");
					// return false;
				}
				if (fileInfo.pixels == null && image2.getStack().isVirtual()) {
					System.err.println("Save As Tiff Virtual stacks not supported.");
					// return false;
				}
				Object info = image2.getProperty("Info");
				if (info != null && (info instanceof String))
					fileInfo.info = (String) info;
				fileInfo.description = getDescriptionString(image2, fileInfo);
				fileInfo.sliceLabels = image2.getStack().getSliceLabels();

			} else {
				fileInfo.nImages = 1;
				Object info = image2.getProperty("Info");
				if (info != null && (info instanceof String))
					fileInfo.info = (String) info;
				Object label = image2.getProperty("Label");
				if (label != null && (label instanceof String)) {
					fileInfo.sliceLabels = new String[1];
					fileInfo.sliceLabels[0] = (String) label;
				}
				fileInfo.description = getDescriptionString(image2, fileInfo);
			}
			TiffEncoder tiffencoder = new TiffEncoder(fileInfo);
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dest)));
			tiffencoder.write(out);
			break;
		case ImageType.PNG:

			if (encoder.startsWith("o") || encoder.startsWith("O")) {
				// com.objectplanet.image.PngEncoder;
				PngEncoder pngencoder = new PngEncoder(PngEncoder.COLOR_INDEXED);
				pngencoder.encode(image2.getImage(), new FileOutputStream(dest));
			} else if (encoder.startsWith("j") || encoder.startsWith("J")) {
				ImageIO.write((RenderedImage) image2.getBufferedImage(), "png", dest);
			}
			break;

		default:
			Log.warn(format + " not supported. ");
			break;
		}

		/* clean up */
		image2.flush();
		image.flush();
		Log.info("Converting " + source + " to " + dest.getName() + " using " + encoder + " takes "
				+ (System.currentTimeMillis() - start) + " milliseconds.");

	}

	public static void convert(String source, String format, File dest) throws IOException {

		convert(source, format, 4, 4, "fire", false, "objectplanet", dest);

	}

	/**
	 * Returns a string containing information about the specified image.
	 * 
	 * @param fi
	 */
	public static String getDescriptionString(ImagePlus imp, FileInfo fi) {
		Calibration cal = imp.getCalibration();
		StringBuffer sb = new StringBuffer(100);
		sb.append("ImageJ=" + ImageJ.VERSION + "\n");
		if (fi.nImages > 1 && fi.fileType != FileInfo.RGB48)
			sb.append("images=" + fi.nImages + "\n");
		int channels = imp.getNChannels();
		if (channels > 1)
			sb.append("channels=" + channels + "\n");
		int slices = imp.getNSlices();
		if (slices > 1)
			sb.append("slices=" + slices + "\n");
		int frames = imp.getNFrames();
		if (frames > 1)
			sb.append("frames=" + frames + "\n");
		if (imp.isHyperStack())
			sb.append("hyperstack=true\n");
		if (imp.isComposite()) {
			String mode = ((CompositeImage) imp).getModeAsString();
			sb.append("mode=" + mode + "\n");
		}
		if (fi.unit != null)
			sb.append("unit=" + (fi.unit.equals("\u00B5m") ? "um" : fi.unit) + "\n");
		if (fi.valueUnit != null && fi.calibrationFunction != Calibration.CUSTOM) {
			sb.append("cf=" + fi.calibrationFunction + "\n");
			if (fi.coefficients != null) {
				for (int i = 0; i < fi.coefficients.length; i++)
					sb.append("c" + i + "=" + fi.coefficients[i] + "\n");
			}
			sb.append("vunit=" + fi.valueUnit + "\n");
			if (cal.zeroClip())
				sb.append("zeroclip=true\n");
		}

		// get stack z-spacing and fps
		if (cal.frameInterval != 0.0) {
			if ((int) cal.frameInterval == cal.frameInterval)
				sb.append("finterval=" + (int) cal.frameInterval + "\n");
			else
				sb.append("finterval=" + cal.frameInterval + "\n");
		}
		if (!cal.getTimeUnit().equals("sec"))
			sb.append("tunit=" + cal.getTimeUnit() + "\n");
		if (fi.nImages > 1) {
			if (fi.pixelDepth != 0.0 && fi.pixelDepth != 1.0)
				sb.append("spacing=" + fi.pixelDepth + "\n");
			if (cal.fps != 0.0) {
				if ((int) cal.fps == cal.fps)
					sb.append("fps=" + (int) cal.fps + "\n");
				else
					sb.append("fps=" + cal.fps + "\n");
			}
			sb.append("loop=" + (cal.loop ? "true" : "false") + "\n");
		}

		// get min and max display values
		ImageProcessor ip = imp.getProcessor();
		double min = ip.getMin();
		double max = ip.getMax();
		int type = imp.getType();
		boolean enhancedLut = (type == ImagePlus.GRAY8 || type == ImagePlus.COLOR_256) && (min != 0.0 || max != 255.0);
		if (enhancedLut || type == ImagePlus.GRAY16 || type == ImagePlus.GRAY32) {
			sb.append("min=" + min + "\n");
			sb.append("max=" + max + "\n");
		}

		// get non-zero origins
		if (cal.xOrigin != 0.0)
			sb.append("xorigin=" + cal.xOrigin + "\n");
		if (cal.yOrigin != 0.0)
			sb.append("yorigin=" + cal.yOrigin + "\n");
		if (cal.zOrigin != 0.0)
			sb.append("zorigin=" + cal.zOrigin + "\n");
		if (cal.info != null && cal.info.length() <= 64 && cal.info.indexOf('=') == -1 && cal.info.indexOf('\n') == -1)
			sb.append("info=" + cal.info + "\n");
		sb.append((char) 0);
		return new String(sb);
	}
}