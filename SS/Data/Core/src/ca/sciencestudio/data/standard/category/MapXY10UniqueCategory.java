/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXY10UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public class MapXY10UniqueCategory<T extends MapXY10Category<?>> extends AbstractUniqueCategory<T> {

	public MapXY10UniqueCategory(String uid) {
		super(uid);
	}

	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) MapXY10Category.INSTANCE;
	}
	
	public String I() {
		return getCategory().I(getUID());
	}

	public String J() {
		return getCategory().J(getUID());
	}

	public String X() {
		return getCategory().X(getUID());
	}

	public String Y() {
		return getCategory().Y(getUID());
	}

	public String EndX() {
		return getCategory().EndX(getUID());
	}

	public String EndY() {
		return getCategory().EndY(getUID());
	}

	public String StepX() {
		return getCategory().StepX(getUID());
	}

	public String StepY() {
		return getCategory().StepY(getUID());
	}

	public String SizeX() {
		return getCategory().SizeX(getUID());
	}

	public String SizeY() {
		return getCategory().SizeY(getUID());
	}

	public String StartX() {
		return getCategory().StartX(getUID());
	}
	
	public String StartY() {
		return getCategory().StartY(getUID());
	}

	public String Unit() {
		return getCategory().Unit(getUID());
	}	
}
