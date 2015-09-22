package com.eduworks.lang;

/**
 * Represents a string with positional information about where it appears within
 * a tokenized series of strings.
 * 
 * @author mhald
 */
public class EwText {
	private String string;
	private int position;

	public EwText(String s, int offset) {
		this.string = s;
		this.position = offset;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
