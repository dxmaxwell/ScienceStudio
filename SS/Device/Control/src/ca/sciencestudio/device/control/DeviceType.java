/* Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     Devicetype enum.
 *
 */
package ca.sciencestudio.device.control;

import java.io.Serializable;

/**
 * @author chabotd
 */
public enum DeviceType implements Serializable {
	Simple("Simple"),/** all leaf-devices (DeviceComponents) are of this type */
	Motor("Motor"),
	Detector("Detector"),
	Scaler("Scaler"),
	Camera("Camera"),
	CCD("Charge-collecting Device"),
	PhotonShutter("Shutter"),
	BPM("Beam Position Monitor"),
	MCA("Multi-Channel Analyzer"),
	PhotonSlits("Slits"),
	Goniometer("Goni"),
	Automounter("Sample Mounter"),
	Attenuator("Attenuator"),
	StorageRing("SR1"),
	PseudoMotor("PseudoMotor"),
	Robot("Sample Robot");
	
	private final String alias;
	
	private DeviceType(String str) {
		alias = str;
	}
	
	public String fullName() {
		return alias;
	}
}

