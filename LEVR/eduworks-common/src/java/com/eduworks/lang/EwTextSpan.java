package com.eduworks.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * An text span contains text that includes more than one words and therefore
 * spans a series of positions within a sentence.
 * 
 * @author mhald
 */
public class EwTextSpan {
	private String string;
	private List<Integer> positions = new ArrayList<Integer>();

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public List<Integer> getPositions() {
		return positions;
	}

	public void setPositions(List<Integer> positions) {
		this.positions = positions;
	}
}
