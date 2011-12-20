/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ImageType class.
 *     
 */
package ca.lightsource.sciencestudio.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;

public class ImageType {
	public static final int UNKNOWN = 0, TIFF = 1, DICOM = 2, FITS = 3,
			PGM = 4, JPEG = 5, GIF = 6, LUT = 7, BMP = 8, PNG = 9,
			TIFF_AND_DICOM = 10, SPE = 11; // don't forget to also update 'types'
	public static final String[] types = { "unknown", "tiff", "dcm", "fits",
			"pgm", "jpg", "gif", "lut", "bmp", "png", "t&d", "spe" };

	public static final String[] mimeTypes = { "", "image/tiff", "",
			"image/fits", "image/x-portable-graymap", "image/jpeg",
			"image/gif", "","", "image/png", "",""};

	public static int getFileType(String path) {

		File file = new File(path);
		String name = file.getName();
		InputStream is;
		byte[] buf = new byte[132];
		try {
			is = new FileInputStream(file);
			is.read(buf, 0, 132);
			is.close();
		} catch (IOException e) {
			return UNKNOWN;
		}

		int b0 = buf[0] & 255, b1 = buf[1] & 255, b2 = buf[2] & 255, b3 = buf[3] & 255;
		//IJ.log("getFileType: "+ name+" "+b0+" "+b1+" "+b2+" "+b3);

		// Combined TIFF and DICOM created by GE Senographe scanners
		if (buf[128] == 68 && buf[129] == 73 && buf[130] == 67
				&& buf[131] == 77
				&& ((b0 == 73 && b1 == 73) || (b0 == 77 && b1 == 77)))
			return TIFF_AND_DICOM;

		/*if (b0==73 && b1==73 && b2==42 && b3==0 && !(bioformats&&name.endsWith(".flex")))
			return TIFF;*/

		// Little-endian TIFF ("II")
		if (b0 == 77 && b1 == 77 && b2 == 0 && b3 == 42)
			return TIFF;

		// JPEG
		if (b0 == 255 && b1 == 216 && b2 == 255)
			return JPEG;

		// GIF ("GIF8")
		if (b0 == 71 && b1 == 73 && b2 == 70 && b3 == 56)
			return GIF;

		name = name.toLowerCase(Locale.US);

		// DICOM ("DICM" at offset 128)
		if (buf[128] == 68 && buf[129] == 73 && buf[130] == 67
				&& buf[131] == 77 || name.endsWith(".dcm")) {
			return DICOM;
		}

		// ACR/NEMA with first tag = 00002,00xx or 00008,00xx
		if ((b0 == 8 || b0 == 2) && b1 == 0 && b3 == 0
				&& !name.endsWith(".spe") && !name.equals("fid"))
			return DICOM;

		// PGM ("P1", "P4", "P2", "P5", "P3" or "P6")
		if (b0 == 80
				&& (b1 == 49 || b1 == 52 || b1 == 50 || b1 == 53 || b1 == 51 || b1 == 54)
				&& (b2 == 10 || b2 == 13 || b2 == 32 || b2 == 9))
			return PGM;

		// Lookup table
		if (name.endsWith(".lut"))
			return LUT;

		// PNG
		if (b0 == 137 && b1 == 80 && b2 == 78 && b3 == 71)
			return PNG;

		// FITS ("SIMP")
		if ((b0 == 83 && b1 == 73 && b2 == 77 && b3 == 80)
				|| name.endsWith(".fts.gz") || name.endsWith(".fits.gz"))
			return FITS;

		// BMP ("BM")
		if ((b0 == 66 && b1 == 77) || name.endsWith(".dib"))
			return BMP;

		// add SPE here
		if (name.endsWith(".spe"))
			return SPE;

		return UNKNOWN;
	}

	public static int getType(String type) {

		String t = type.toLowerCase(Locale.US);

		if (t.equals("tif"))
			return Arrays.asList(types).indexOf("tiff");

		int index = Arrays.asList(types).indexOf(type);

		if (index >= 0) {
			return index;
		} else {
			return UNKNOWN;
		}
	}

}
