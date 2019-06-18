package com.chinatelecom.xjdh.bean;

import java.text.DecimalFormat;

import com.github.mikephil.charting.utils.ValueFormatter;
/**
 * @author peter
 * 
 */
public class IntValueFormatter implements ValueFormatter {
	private DecimalFormat mFormat;

	public IntValueFormatter() {
		super();
		mFormat = new DecimalFormat("###,###,###");
	}

	@Override
	public String getFormattedValue(float value) {
		return value != 0 ? mFormat.format(value) : "";
	}

}
