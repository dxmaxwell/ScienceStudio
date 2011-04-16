/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     BlobToObjectTypeHandlerCallback class.
 *     
 */
package ca.sciencestudio.util.sql.ibatis;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * @author medrand
 *
 */
public class BlobToObjectTypeHandlerCallback implements TypeHandlerCallback {

	private final static String DEFAULT_BLOB_CONTENT = "An error occurred while serializing parameter.";
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		Object object = null;
		try {
			InputStream in = getter.getBlob().getBinaryStream();
			object = new ObjectInputStream(in).readObject();
		}
		catch (IOException e) {
			logger.warn("Exception while reading blob content from database.", e);
		}
		catch (ClassNotFoundException e) {
			logger.warn("Class of object contained in blob not found.", e);
		}

		return object;
	}

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		byte[] data = null;
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(parameter);
			oos.flush();
			data = baos.toByteArray();
		}
		catch (IOException e) {
			logger.warn("Exception while serializing object for blob content.", e);
		}
		
		if(data == null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(DEFAULT_BLOB_CONTENT);
				oos.flush();
				data = baos.toByteArray();
			}
			catch (IOException e) {
				logger.warn("Exception while serializing default blob content.", e);
			}
		}
		
		if(data == null) {
			data = DEFAULT_BLOB_CONTENT.getBytes();
		}
		
		setter.setBlob(new SerialBlob(data));
	}

	@Override
	public Object valueOf(String str) {
		// Required to satisfy interface //
		return null;
	}
}
