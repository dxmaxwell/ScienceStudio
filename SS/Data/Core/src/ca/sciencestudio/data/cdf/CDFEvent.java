/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFEvent class.
 *     
 */
package ca.sciencestudio.data.cdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.Variable; 

/**
 * @author maxweld
 *
 */
public class CDFEvent {

	private List<Variable> variables = new ArrayList<Variable>();
	
	public CDFEvent(Variable[] variables) {
		this(Arrays.asList(variables));
	}
	
	public CDFEvent(Collection<Variable> variables) {
		this.variables.addAll(variables);
	}
	
	public int getNumberOfVariables() {
		return variables.size();
	}
	
	public List<Variable> getVariables() {
		return Collections.unmodifiableList(variables);
	}
	
	public Variable getVariable(int idx) {
		try {
			return variables.get(idx);
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getVariableIndexByName(String name) {
		int variableIdx = 0;
		for(Variable variable : variables) {
			if(variable.getName().equals(name)) {
				return variableIdx;
			}
			variableIdx++;
		}
		return -1;
	}
	
	public Variable getVariableByName(String name) {
		try {
			return variables.get(getVariableIndexByName(name));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	public int getVariableIndexByID(long ID) {
		int variableIdx = 0;
		for(Variable variable : variables) {
			if(variable.getID() == ID) {
				return variableIdx;
			}
			variableIdx++;
		}
		return -1;
	}
	
	public Variable getVariableByID(long ID) {
		try {
			return variables.get(getVariableIndexByID(ID));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public String[] getVariableNames() {
		int n = getNumberOfVariables();
		String[] varNames = new String[n];
		for(int i = 0; i < n; i++) {
			varNames[i] = variables.get(i).getName();
		}
		return varNames;
	}
	
	public long[] getVariableIDs() {
		int n = getNumberOfVariables();
		long[] varIDs = new long[n];
		for(int i = 0; i < n; i++) {
			varIDs[i] = variables.get(i).getID();
		}
		return varIDs;
	}
	
	public long[] getVariableTypes() {
		int n = getNumberOfVariables();
		long[] varTypes = new long[n];
		for(int i = 0; i < n; i++) {
			varTypes[i] = variables.get(i).getDataType();
		}
		return varTypes;
	}
	
	public long getMinimumNumberOfRecords() throws CDFException {
		long numberOfRecords = Long.MAX_VALUE;
		for(Variable variable : variables) {
			numberOfRecords = Math.min(numberOfRecords, variable.getNumWrittenRecords());
		}
		return numberOfRecords;
	}
	
	public long getMaximumNumberOfRecords() throws CDFException {
		long numberOfRecords = Long.MIN_VALUE;
		for(Variable variable : variables) {
			numberOfRecords = Math.max(numberOfRecords, variable.getNumWrittenRecords());
		}
		return numberOfRecords;
	}
}
