/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentValidator class.
 *     
 */
package ca.sciencestudio.model.session.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 * 
 *
 */
public class ExperimentValidator extends AbstractModelValidator<Experiment> {

	// Maximum field length should match database limits. //
	public static final int MAX_LENGTH_NAME = 80;
	public static final int MAX_LENGTH_DESCRIPTION = 2000;
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_SESSION_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final String DEFAULT_SOURCE_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_INSTRUMENT_TECHNIQUE_GID = GID.DEFAULT_GID;
	
	@Override
	public void validate(Experiment t, Errors errors) {
		
		errors.pushNestedPath("name");
		if(t.getName() == null) {
			t.setName(DEFAULT_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Name field is empty or whitespce.");
		rejectIfExceedsLength(MAX_LENGTH_NAME, errors, null, EC_MAX_LENGTH, "Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("description");
		if(t.getDescription() == null) {
			t.setDescription(DEFAULT_DESCRIPTION);
		}
		rejectIfExceedsLength(MAX_LENGTH_DESCRIPTION, errors, null, EC_MAX_LENGTH, "Description field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("sessionGid");
		GID gid = GID.parse(t.getSessionGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Session GID field is required.");
		}
		else if(!gid.isType(Session.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Session GID field must have type: " + Session.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Session GID field must have a facility specifed.");
		}
		errors.popNestedPath();

		errors.pushNestedPath("sourceGid");
		gid = GID.parse(t.getSourceGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Source GID field is required.");
		}
		else if(!(gid.isType(Sample.GID_TYPE) || gid.isType(Scan.GID_TYPE))) {
			errors.rejectValue(null, EC_INVALID, "Source GID field must have type: " + Sample.GID_TYPE + " or " + Scan.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Source GID field must have a facility specifed.");
		}	
		errors.popNestedPath();
		
		errors.pushNestedPath("instrumentTechniqueGid");
		gid = GID.parse(t.getInstrumentTechniqueGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Instrument Technique GID field is required.");
		}
		else if(!gid.isType(InstrumentTechnique.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Instrument Technique GID field must have type: " + InstrumentTechnique.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Instrument Technique GID field must have a facility specifed.");
		}	
		errors.popNestedPath();
	}

	@Override
	protected Class<Experiment> getModelClass() {
		return Experiment.class;
	}
}
