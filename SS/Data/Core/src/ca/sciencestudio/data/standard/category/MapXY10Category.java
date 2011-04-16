/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXY10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.AbstractCategory;

/**
 * @author maxweld
 *
 */
public class MapXY10Category<T extends MapXY10UniqueCategory<?>> extends AbstractCategory<T> {

	public static final MapXY10Category<MapXY10UniqueCategory<?>> INSTANCE =
										new MapXY10Category<MapXY10UniqueCategory<?>>();

	private final int cmajor = 1;
	private final int cminor = 0;
	private final String cname = "MapXY";
	private final String I = join(getName(), "I");
	private final String J = join(getName(), "J");
	private final String X = join(getName(), "X");
	private final String Y = join(getName(), "Y");
	private final String EndX = join(getName(), "EndX");
	private final String EndY = join(getName(), "EndY");
	private final String StepX = join(getName(), "StepX");
	private final String StepY = join(getName(), "StepY");
	private final String SizeX = join(getName(), "SizeX");
	private final String SizeY = join(getName(), "SizeY");
	private final String StartX = join(getName(), "StartX");
	private final String StartY = join(getName(), "StartY");
	private final String Unit = join(getName(), "Unit");

	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new MapXY10UniqueCategory<MapXY10Category<MapXY10UniqueCategory<?>>>(uid);
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

	public String I() {
		return I;
	}
	public String I(String uid) {
		return join(uid, I());
	}

	public String J() {
		return J;
	}
	public String J(String uid) {
		return join(uid, J());
	}

	public String X() {
		return X;
	}
	public String X(String uid) {
		return join(uid, X());
	}

	public String Y() {
		return Y;
	}
	public String Y(String uid) {
		return join(uid, Y());
	}

	public String EndX() {
		return EndX;
	}
	public String EndX(String uid) {
		return join(uid, EndX());
	}

	public String EndY() {
		return EndY;
	}
	public String EndY(String uid) {
		return join(uid, EndY());
	}

	public String StepX() {
		return StepX;
	}
	public String StepX(String uid) {
		return join(uid, StepX());
	}

	public String StepY() {
		return StepY;
	}
	public String StepY(String uid) {
		return join(uid, StepY());
	}

	public String SizeX() {
		return SizeX;
	}
	public String SizeX(String uid) {
		return join(uid, SizeX());
	}

	public String SizeY() {
		return SizeY;
	}
	public String SizeY(String uid) {
		return join(uid, SizeY());
	}

	public String StartX() {
		return StartX;
	}
	public String StartX(String uid) {
		return join(uid, StartX());
	}
	
	public String StartY() {
		return StartY;
	}
	public String StartY(String uid) {
		return join(uid, StartY());
	}

	public String Unit() {
		return Unit;
	}
	public String Unit(String uid) {
		return join(uid, Unit());
	}
}
