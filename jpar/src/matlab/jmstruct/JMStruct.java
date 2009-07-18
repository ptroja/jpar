package matlab.jmstruct;

import java.io.Serializable;

public class JMStruct implements Serializable {
	
	private static final long serialVersionUID = 7526472295622776147L;
	
	private Object fields, values;
	private int dimX, dimY;

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

	public Object getFields() {
		return fields;
	}

	public void setFields(Object fields) {
		this.fields = fields;
	}

	public Object getValues() {
		return values;
	}

	public void setValues(Object values) {
		this.values = values;
	}
}
