package matlab.jmarray;

import java.io.Serializable;

public class JMArray implements Serializable {
	
	private static final long serialVersionUID = 7526472295622776147L;
	
	private Object realpart, imagpart;
	private int dimX, dimY;
	private boolean isReal;
	
	public int getDimX() {
		return dimX;
	}
	public void setDimX(int dimX) {
		this.dimX = dimX;
	}
	public int getDimY() {
		return dimY;
	}
	public void setDimY(int dimY) {
		this.dimY = dimY;
	}
	public Object getImagpart() {
		return imagpart;
	}
	public void setImagpart(Object imagpart) {
		this.imagpart = imagpart;
	}
	public Object getRealpart() {
		return realpart;
	}
	public void setRealpart(Object realpart) {
		this.realpart = realpart;
	}
	public boolean isReal() {
		return isReal;
	}
	public void setReal(boolean isReal) {
		this.isReal = isReal;
	}
}
