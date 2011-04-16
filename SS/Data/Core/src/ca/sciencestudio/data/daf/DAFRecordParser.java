/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DAFRecordParser class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.io.IOException;
import java.util.Collection;

import ca.sciencestudio.data.daf.DAFEvent;
import ca.sciencestudio.data.daf.DAFRecord;
import ca.sciencestudio.util.text.PeekableBufferedReader;

/**
 * @author maxweld
 *
 */
public interface DAFRecordParser {

	public DAFRecord parseRecord(Collection<DAFEvent> events, PeekableBufferedReader reader) throws IOException;
}
