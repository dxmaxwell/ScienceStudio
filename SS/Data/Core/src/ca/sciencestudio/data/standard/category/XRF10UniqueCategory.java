/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     XRF10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
@Deprecated
public class XRF10UniqueCategory<T extends XRF10Category<?>> extends AbstractUniqueCategory<T> {

	public XRF10UniqueCategory(String uid) {
		super(uid);
	}

	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) XRF10Category.INSTANCE;
	}
	
	public String Unit() {
		return getCategory().Unit(getUID());
	}
	
	public String Spectrum() {
		return getCategory().Spectrum(getUID());
	}
	
	public String MinEnergy() {
		return getCategory().MinEnergy(getUID());
	}

	public String MaxEnergy() {
		return getCategory().MaxEnergy(getUID());
	}
}
