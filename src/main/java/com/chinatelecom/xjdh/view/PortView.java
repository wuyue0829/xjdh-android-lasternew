package com.chinatelecom.xjdh.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
@EViewGroup(R.layout.port_status_view)
public class PortView extends LinearLayout{
	@ViewById
    protected TextView tvPortIndex, tvPortValue,tvPortDisplay1,tvPortDisplay2;

    public PortView(Context context) {
        super(context);
    }

    public void setTexts(String index, String value, String display1,String display2) {
    	tvPortIndex.setText(index);
    	tvPortValue.setText(value);
    	tvPortDisplay1.setText(display1);
    	tvPortDisplay2.setText(display2);
    }
}
