/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TestConverter class.
 *     
 */
package ca.lightsource.sciencestudio.image;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class TestConverter {

	@Test
	public void testConvert() throws IOException {
		File dest1 = new File("./test1.png");
		Converter.convert("/home/liud/Dev/test/scanSmall/c4-3_2300.spe", "png", 4, 4, "fire", false, "objectplanet", dest1);
		File dest2 = new File("./test2.png");
		Converter.convert("/home/liud/Dev/test/scanSmall/c4-3_2300.spe", "png", 4, 4, "fire", false, "java", dest2);
	}
	@After
	public void removeFile() {
		File dest1 = new File("./test1.png");
		dest1.delete();
		File dest2 = new File("./test2.png");
		dest2.delete();
	}

}
