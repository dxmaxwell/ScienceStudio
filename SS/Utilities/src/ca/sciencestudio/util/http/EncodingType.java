/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     EncodingType class.
 *     
 */
package ca.sciencestudio.util.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maxweld
 * 
 *
 */
public class EncodingType {
	
	private static final String WILDCARD_TYPE = "*";
	
	public static final EncodingType GZIP = new EncodingType("gzip");
	
	public static final EncodingType DEFLATE = new EncodingType("deflate");
	
	public static final EncodingType COMPRESS = new EncodingType("compress");

	public static final EncodingType IDENTITY = new EncodingType("identity");
	
	public static final EncodingType WILDCARD = new EncodingType(WILDCARD_TYPE);
	
	private static final String PARAM_QUALITY_VALUE = "q";
	
	private static final double DEFAULT_QUALITY_VALUE = 0.001;
	
	private static final Comparator<EncodingType> QUALITY_VALUE_COMPARATOR = new Comparator<EncodingType>() {

		@Override
		public int compare(EncodingType encodingType1, EncodingType encodingType2) {
			double qualityValue1 = encodingType1.getQualityValue();
			double qualityValue2 = encodingType2.getQualityValue();
			return Double.compare(qualityValue1, qualityValue2);
		}		
	};
	
	private String type;
	private Map<String,String> parameters;
	
	public static EncodingType valueof(String value) {
		return parseEncodingType(value);
	}
	
	public static EncodingType parseEncodingType(String encodingType) {
		List<EncodingType> encodingTypes = parseEncodingTypes(encodingType);
		if(encodingTypes.isEmpty()) {
			throw new IllegalArgumentException("Encoding type could not be parsed.");
		}
		return encodingTypes.iterator().next();
	}
	
	public static List<EncodingType> parseEncodingTypes(String encodingTypes) {
		if(encodingTypes == null) {
			return Collections.emptyList();
		}
		
		encodingTypes = encodingTypes.trim();
		if(encodingTypes.length() == 0) {
			return Collections.emptyList();
		}
		
		List<EncodingType> result = new ArrayList<EncodingType>();
		
		String[] splitEncodingTypes = encodingTypes.split(",");
		for(String encodingType : splitEncodingTypes) {
			encodingType = encodingType.trim();
			if(encodingType.length() == 0) {
				continue;
			}
			
			String[] splitEncodingType = encodingType.split(";");
			String type = splitEncodingType[0].trim();
			if(type.length() == 0) {
				continue;
			}
			
			Map<String,String> parameters = new HashMap<String,String>();
			if(splitEncodingType.length > 1) {
				for(int idx = 1; idx < splitEncodingType.length; idx++) {		
					String[] kv = splitEncodingType[idx].split("=", 2);
					if(kv.length == 2) {
						parameters.put(kv[0].trim(), kv[1].trim());
					}
				}
			}
			
			result.add(new EncodingType(type, parameters));
		}
		
		return result;
	}
	
	public static void sortByQualityValue(List<EncodingType> encodingTypes) {
		Collections.sort(encodingTypes, QUALITY_VALUE_COMPARATOR);
	}
	
	public EncodingType(String type) {
		this.type = type;
		this.parameters = Collections.emptyMap();
	}
	
	public EncodingType(String type, double qualityValue) {
		this(type, Collections.singletonMap(PARAM_QUALITY_VALUE, String.valueOf(qualityValue)));
	}

	public EncodingType(String type, Map<String,String> parameters) {
		this.parameters = new HashMap<String,String>(parameters);
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public double getQualityValue() {
		double qualityValue;
		try {
			qualityValue = Double.parseDouble(getParameter(PARAM_QUALITY_VALUE));
		}
		catch(Exception e) {
			return DEFAULT_QUALITY_VALUE;	
		}
	
		if(qualityValue > 1.0) {
			qualityValue = 1.0;
		}
		else if(qualityValue < 0.0) {
			qualityValue = 0.0;
		} 
		
		return qualityValue;
	}

	public boolean isWildcardType() {
		return equals(WILDCARD);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof EncodingType)) {
			return false;
		}
		return type.equalsIgnoreCase(((EncodingType)obj).getType());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(type);
		for(Map.Entry<String,String> entry : parameters.entrySet()) {
			buffer.append(";").append(entry.getKey());
			buffer.append("=").append(entry.getValue());
		}
		return buffer.toString();
	}
}
