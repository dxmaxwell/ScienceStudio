/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MCA10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.AbstractCategory;

/**
 * @author maxweld
 *
 */
public class MCA10Category<T extends MCA10UniqueCategory<?>> extends AbstractCategory<T> {

	public static final MCA10Category<MCA10UniqueCategory<?>> INSTANCE =
									new MCA10Category<MCA10UniqueCategory<?>>();

	private final int cmajor = 1;
	private final int cminor = 0;
	private final String cname = "MCA";
	private final String Unit = join(getName(), "Unit");
	private final String Name = join(getName(), "Name");
	private final String Desc = join(getName(), "Desc");
	private final String NElements = join(getName(), "NElements");
	private final String MinEnergy = join(getName(), "MinEnergy");
	private final String MaxEnergy = join(getName(), "MaxEnergy");
	private final String EnergyGapTime = join(getName(), "EnergyGapTime");
	private final String PresetRealTime = join(getName(), "PresetRealTime");
	private final String ModSpectrumType = join(getName(), "ModSpectrumType");
	private final String EnergyPeakingTime = join(getName(), "EnergyPeakingTime");
	private final String SumSpectrum = join(getName(), "SumSpectrum");
	private final String SumSpectrumCrts = join(getName(), "SumSpectrumCrts");
	private final String Spectrum = join(getName(), "Spectrum");
	private final String FastCount = join(getName(), "FastCount");
	private final String SlowCount = join(getName(), "SlowCount");
	private final String DeadTimePct = join(getName(), "DeadTimePct");
	private final String ElapsedRealTime = join(getName(), "ElapsedRealTime");
	private final String ElapsedLiveTime = join(getName(), "ElapsedLiveTime");
	
	public final String INaughtCorrection = "Inaught";
	public final String DeadTimeCorrection = "DeadTime";
	
	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new MCA10UniqueCategory<MCA10Category<MCA10UniqueCategory<?>>>(uid);
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
	
	public String Name() {
		return Name;
	}
	public String Name(String uid) {
		return join(uid, Name());
	}
	
	public String Desc() {
		return Desc;
	}
	public String Desc(String uid) {
		return join(uid, Desc());
	}
	
	public String NElements() {
		return NElements;
	}
	public String NElements(String uid) {
		return join(uid, NElements());
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
	
	public String EnergyGapTime() {
		return EnergyGapTime;
	}
	public String EnergyGapTime(String uid) {
		return join(uid, EnergyGapTime());
	}
	
	public String PresetRealTime() {
		return PresetRealTime;
	}
	public String PresetRealTime(String uid) {
		return join(uid, PresetRealTime());
	}
	
	public String ModSpectrumType() {
		return ModSpectrumType;
	}
	public String ModSpectrumType(String uid) {
		return join(uid, ModSpectrumType());
	}
	
	public String EnergyPeakingTime() {
		return EnergyPeakingTime;
	}
	public String EnergyPeakingTime(String uid) {
		return join(uid, EnergyPeakingTime());
	}
	
	public String SumSpectrum() {
		return SumSpectrum;
	}
	public String SumSpectrum(String uid) {
		return join(uid, SumSpectrum());
	}
	
	public String SumSpectrumCrts() {
		return SumSpectrumCrts;
	}
	public String SumSpectrumCrts(String uid) {
		return join(uid, SumSpectrumCrts());
	}
	
	public String Spectrum() {
		return Spectrum;
	}
	public String Spectrum(int n) {
		return join(Spectrum(), Math.abs(n));
	}
	public String Spectrum(String uid) {
		return join(uid, Spectrum());
	}
	public String Spectrum(String uid, int n) {
		return join(uid, Spectrum(n));
	}
	
	public String FastCount() {
		return FastCount;
	}
	public String FastCount(int n) {
		return join(FastCount(), Math.abs(n));
	}
	public String FastCount(String uid) {
		return join(uid, FastCount());
	}
	public String FastCount(String uid, int n) {
		return join(uid, FastCount(n));
	}
	
	public String SlowCount() {
		return SlowCount;
	}
	public String SlowCount(int n) {
		return join(SlowCount(), Math.abs(n));
	}
	public String SlowCount(String uid) {
		return join(uid, SlowCount());
	}
	public String SlowCount(String uid, int n) {
		return join(uid, SlowCount(n));
	}
	
	public String DeadTimePct() {
		return DeadTimePct;
	}
	public String DeadTimePct(int n) {
		return join(DeadTimePct(), Math.abs(n));
	}
	public String DeadTimePct(String uid) {
		return join(uid, DeadTimePct());
	}
	public String DeadTimePct(String uid, int n) {
		return join(uid, DeadTimePct(n));
	}
	
	public String ElapsedRealTime() {
		return ElapsedRealTime;
	}
	public String ElapsedRealTime(int n) {
		return join(ElapsedRealTime(), Math.abs(n));
	}
	public String ElapsedRealTime(String uid) {
		return join(uid, ElapsedRealTime());
	}
	public String ElapsedRealTime(String uid, int n) {
		return join(uid, ElapsedRealTime(n));
	}
	
	public String ElapsedLiveTime() {
		return ElapsedLiveTime;
	}
	public String ElapsedLiveTime(int n) {
		return join(ElapsedLiveTime(), Math.abs(n));
	}
	public String ElapsedLiveTime(String uid) {
		return join(uid, ElapsedLiveTime());
	}
	public String ElapsedLiveTime(String uid, int n) {
		return join(uid, ElapsedLiveTime(n));
	}
}
