/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MCA10UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public class MCA10UniqueCategory<T extends MCA10Category<?>> extends AbstractUniqueCategory<T> {

	public MCA10UniqueCategory(String uid) {
		super(uid);
	}

	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) MCA10Category.INSTANCE;
	}
	
	public String Unit() {
		return getCategory().Unit(getUID());
	}
	
	public String Name() {
		return getCategory().Name(getUID());
	}
	
	public String Desc() {
		return getCategory().Desc(getUID());
	}
	
	public String NElements() {
		return getCategory().NElements(getUID());
	}
	
	public String MinEnergy() {
		return getCategory().MinEnergy(getUID());
	}	

	public String MaxEnergy() {
		return getCategory().MaxEnergy(getUID());
	}
	
	public String EnergyGapTime() {
		return getCategory().EnergyGapTime(getUID());
	}
	
	public String PresetRealTime() {
		return getCategory().PresetRealTime(getUID());
	}
	
	public String ModSpectrumType() {
		return getCategory().ModSpectrumType(getUID());
	}
	
	public String EnergyPeakingTime() {
		return getCategory().EnergyPeakingTime(getUID());
	}
	
	public String SumSpectrum() {
		return getCategory().SumSpectrum(getUID());
	}
	
	public String SumSpectrumCrts() {
		return getCategory().SumSpectrumCrts(getUID());
	}
	
	public String Spectrum() {
		return getCategory().Spectrum(getUID());
	}
	public String Spectrum(int n) {
		return getCategory().Spectrum(getUID(), n);
	}
	
	public String FastCount() {
		return getCategory().FastCount(getUID());
	}
	public String FastCount(int n) {
		return getCategory().FastCount(getUID(), n);
	}
	
	public String SlowCount() {
		return getCategory().SlowCount(getUID());
	}
	public String SlowCount(int n) {
		return getCategory().SlowCount(getUID(), n);
	}
	
	public String DeadTimePct() {
		return getCategory().DeadTimePct(getUID());
	}
	public String DeadTimePct(int n) {
		return getCategory().DeadTimePct(getUID(), n);
	}
	
	public String ElapsedRealTime() {
		return getCategory().ElapsedRealTime(getUID());
	}
	public String ElapsedRealTime(int n) {
		return getCategory().ElapsedRealTime(getUID(), n);
	}
	
	public String ElapsedLiveTime() {
		return getCategory().ElapsedLiveTime(getUID());
	}
	public String ElapsedLiveTime(int n) {
		return getCategory().ElapsedLiveTime(getUID(), n);
	}	
}
