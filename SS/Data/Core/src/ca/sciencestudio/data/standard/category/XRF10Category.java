/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     XRF10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.AbstractCategory;

/**
 * @author maxweld
 *
 */
@Deprecated
public class XRF10Category<T extends XRF10UniqueCategory<?>> extends AbstractCategory<T> {

	public static final XRF10Category<XRF10UniqueCategory<?>> INSTANCE =
								new XRF10Category<XRF10UniqueCategory<?>>();

	private final int cmajor = 1;
	private final int cminor = 0;
	private final String cname = "XRF";
	private final String Unit = join(getName(), "Unit");
	private final String Spectrum = join(getName(), "Spectrum");
	private final String MinEnergy = join(getName(), "MinEnergy");
	private final String MaxEnergy = join(getName(), "MaxEnergy");

	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new XRF10UniqueCategory<XRF10Category<XRF10UniqueCategory<?>>>(uid);
	}

	public int getMajor() {
		return cmajor;
	}

	public int getMinor() {
		return cminor;
	}

	public String getName() {
		return cname;
	}

	public String Unit() {
		return Unit;
	}
	public String Unit(String uid) {
		return join(uid, Unit());
	}
	
	public String Spectrum() {
		return Spectrum;
	}
	public String Spectrum(String uid) {
		return join(uid, Spectrum());
	}
	
	public String MinEnergy() {
		return MinEnergy;
	}
	public String MinEnergy(String uid) {
		return join(uid, MinEnergy());
	}

	public String MaxEnergy() {
		return MaxEnergy;
	}
	public String MaxEnergy(String uid) {
		return join(uid, MaxEnergy());
	} 
}
