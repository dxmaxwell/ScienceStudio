/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     StdCategories interface.
 *     
 */
package ca.sciencestudio.data.standard;

import ca.sciencestudio.data.standard.category.BeamI10Category;
import ca.sciencestudio.data.standard.category.BeamI10UniqueCategory;
import ca.sciencestudio.data.standard.category.BeamI11Category;
import ca.sciencestudio.data.standard.category.BeamI11UniqueCategory;
import ca.sciencestudio.data.standard.category.Category;
import ca.sciencestudio.data.standard.category.Epics10Category;
import ca.sciencestudio.data.standard.category.Epics10UniqueCategory;
import ca.sciencestudio.data.standard.category.MCA10Category;
import ca.sciencestudio.data.standard.category.MCA10UniqueCategory;
import ca.sciencestudio.data.standard.category.MapXY10Category;
import ca.sciencestudio.data.standard.category.MapXY10UniqueCategory;
import ca.sciencestudio.data.standard.category.MapXY11Category;
import ca.sciencestudio.data.standard.category.MapXY11UniqueCategory;
import ca.sciencestudio.data.standard.category.SS10Category;
import ca.sciencestudio.data.standard.category.SS10UniqueCategory;
import ca.sciencestudio.data.standard.category.SSModel10Category;
import ca.sciencestudio.data.standard.category.SSModel10UniqueCategory;
import ca.sciencestudio.data.standard.category.Scan10Category;
import ca.sciencestudio.data.standard.category.Scan10UniqueCategory;
import ca.sciencestudio.data.standard.category.XRF10Category;
import ca.sciencestudio.data.standard.category.XRF10UniqueCategory;

/**
 * @author maxweld
 *
 */
@SuppressWarnings("deprecation")
public interface StdCategories {
	public static final String CATEGORY_REGISTRY = "ScienceStudio";
	public static final BeamI10Category<BeamI10UniqueCategory<?>> BeamI10 = BeamI10Category.INSTANCE;
	public static final BeamI11Category<BeamI11UniqueCategory<?>> BeamI11 = BeamI11Category.INSTANCE;
	public static final Epics10Category<Epics10UniqueCategory<?>> Epics10 = Epics10Category.INSTANCE;
	public static final MapXY10Category<MapXY10UniqueCategory<?>> MapXY10 = MapXY10Category.INSTANCE;
	public static final MapXY11Category<MapXY11UniqueCategory<?>> MapXY11 = MapXY11Category.INSTANCE;
	public static final Scan10Category<Scan10UniqueCategory<?>> Scan10 = Scan10Category.INSTANCE;
	public static final SS10Category<SS10UniqueCategory<?>> SS10 = SS10Category.INSTANCE;
	public static final SSModel10Category<SSModel10UniqueCategory<?>> SSModel10 = SSModel10Category.INSTANCE;
	public static final XRF10Category<XRF10UniqueCategory<?>> XRF10 = XRF10Category.INSTANCE;
	public static final MCA10Category<MCA10UniqueCategory<?>> MCA10 = MCA10Category.INSTANCE;
	
	public static final Category<?>[] CATEGORIES = new Category[] {  
		BeamI10, BeamI11, Epics10, MapXY10, MapXY11, Scan10, SS10, SSModel10, XRF10, MCA10
	};
}
