package com.chinatelecom.xjdh.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
@EViewGroup(R.layout.energy_mian)
public class EnergyView extends LinearLayout{
	@ViewById
    protected TextView name,pa, pb,pc,pt;

    public EnergyView(Context context) {
        super(context);
    }

    public void setTexts(String index, String value1, String value2, String value3,String value4) {
    	name.setText(index);
    	pa.setText(value1);
    	pb.setText(value2);
    	pc.setText(value3);
    	pt.setText(value4);
    }
}
