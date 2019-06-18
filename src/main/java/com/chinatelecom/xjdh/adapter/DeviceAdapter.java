package com.chinatelecom.xjdh.adapter;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.BoardJsonData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class DeviceAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	public DeviceAdapter(Context mContext) {
//		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}
	
	
	String model;
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	BoardJsonData boardData;

	public BoardJsonData getBoardData() {
		return boardData;
	}

	public void setBoardData(BoardJsonData boardData) {
		this.boardData = boardData;
	}



	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(model.equals("302A"))
		{
			return 6 + 4;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderA holderA = null;
//		ViewHolderB holderB = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case 0:
				convertView = inflater.inflate(R.layout.port_status_view, null);
				holderA = new ViewHolderA();
				holderA.tvPortIndex = (TextView) convertView
						.findViewById(R.id.tvPortIndex);
				holderA.tvPortValue = (TextView) convertView
						.findViewById(R.id.tvPortValue);
				holderA.tvPortDisplay1 = (TextView) convertView
						.findViewById(R.id.tvPortDisplay1);
				convertView.setTag(holderA);
				break;

			default:
//				convertView = inflater.inflate(R.layout.port_wenshi_view, null);
//				holderB = new ViewHolderB();
//				holderB.tvValue1 = (TextView) convertView
//						.findViewById(R.id.tvValue1);
//				holderB.tvValue2 = (TextView) convertView
//						.findViewById(R.id.tvValue2);
//				holderB.tvValue3= (TextView) convertView
//						.findViewById(R.id.tvValue3);
//				holderB.spinner_value = (Spinner) convertView
//						.findViewById(R.id.spinner_value);
//				
//				SpinnerAdapter adapter = new SpinnerAdapter(mContext);
//				holderB.spinner_value.setAdapter(adapter);
//				holderB.spinner_value
//						.setOnItemSelectedListener(new ItemClickSelectListener(
//								holderB.spinner_value));
//				
//				convertView.setTag(holderB);
				break;
			}
			
		} else{
			switch (type) {
			case 0:
				holderA = (ViewHolderA) convertView.getTag();
				break;

			default:
//				holderB = (ViewHolderB) convertView.getTag();
				break;
			}
		}
		
		switch (type) {
		case 0:
			if(model.equals("302A"))
			{
				if(position+1 < 7)
				{
					int value = boardData.getDi()[position] > 48 ? boardData.getDi()[position] - 48 : 0;
					holderA.tvPortIndex.setText("DI" + Integer.toString(position+1));
					holderA.tvPortValue.setText( "值:" + Integer.toString(value));
					holderA.tvPortDisplay1.setText( "状态:" + (value == 1?"正常":"告警"));
				}
			}
			break;

		default:
//			if(model.equals("302A"))
//			{
//				if(position+1 >=7 && position+1 < 11){
//					int value = boardData.getAi()[position-6];
//					if(value < 200)
//						value = 0;
//					float realV = ((float)value * 5 )/10000;
//					float tempV = ((float)value * 120)/10000 - 40;
//					float humidV = ((float)value * 100)/10000;
//					
//					holderB.tvValue1.setText("AI" + Integer.toString(position-5));
//					holderB.tvValue2.setText("值:" + String.format("%.2fV",realV));
//					int count = holderB.spinner_value.getChildCount();
//					holderB.spinner_value.setSelection(count);
//					if (count == 0) {
//						holderB.tvValue3.setText(String.format("%.2f",tempV));
//					}else if(count == 1){
//						holderB.tvValue3.setText(String.format("%.2f",humidV));
//					}
//				}
//			}
			break;
		}
//		if(model.equals("302A"))
//		{
//			if(position+1 < 7)
//			{
//				//di
//				int value = boardData.getDi()[position] > 48 ? boardData.getDi()[position] - 48 : 0;
//				//"水浸:" + (value == 1?"正常":"告警")
//				portView.setTexts("DI" + Integer.toString(position+1), "值:" + Integer.toString(value), "状态:" + (value == 1?"正常":"告警"),"");
//			}else if(position+1 >=7 && position+1 < 11){
//				//ai
//				int value = boardData.getAi()[position-6];
//				if(value < 200)
//					value = 0;
//				float realV = ((float)value * 5 )/10000;
//				float tempV = ((float)value * 120)/10000 - 40;
//				float humidV = ((float)value * 100)/10000;
//				portView.setTexts("AI" + Integer.toString(position-5), "值:" + String.format("%.2fV",realV), "温度:" + String.format("%.2f",tempV), "湿度:" + String.format("%.2f",humidV));
//			}
//		}
		return convertView;
	}
	
	
	@Override
	public int getItemViewType(int position) {
		int type;
		if (position+1<7) {
			type = 0;
		}else{
			type = 1;
		}
		return type;
	}

	/**
	 * 该方法返回值为当前适配器Item样式的数量
	 */
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	
	class ViewHolderA {
		TextView  tvPortIndex, tvPortValue,tvPortDisplay1;
	}
	class ViewHolderB {
		TextView tvValue1,tvValue2,tvValue3;
		Spinner spinner_value;
	}
	
	
	private class ItemClickSelectListener implements OnItemSelectedListener {
		Spinner spinner_value ;

		public ItemClickSelectListener(Spinner spinner_value) {
			this.spinner_value = spinner_value;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			//关键代码
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	

	
}
