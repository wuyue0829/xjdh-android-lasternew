package com.chinatelecom.xjdh.adapter;

import com.chinatelecom.xjdh.bean.EnergyData;
import com.chinatelecom.xjdh.view.EnergyView_;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class EnergyDataAdapter extends BaseAdapter {
	private Context mContext;

	public EnergyDataAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	private EnergyData energyData;

	public EnergyData getEnergyData() {
		return energyData;
	}

	public void setEnergyData(EnergyData energyData) {
		this.energyData=energyData;
		this.notifyDataSetChanged();
	}

	String model;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(model.equals("302A"))
		{
			return 13;
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
		if (convertView == null) {
			convertView = EnergyView_.build(mContext);
		}
		EnergyView_ energyView = (EnergyView_) convertView;
		if (model.equals("302A")) {
			if (position == 0) {
				energyView.setTexts("", "A相值", "B相值", "C相值", "合相值");
			} else if (position == 1) {
				
				energyView.setTexts("有功功率", 
						String.format("%.2f", energyData.getPxValue()[0]),
						String.format("%.2f", energyData.getPxValue()[1]),
						String.format("%.2f", energyData.getPxValue()[2]),
						String.format("%.2f", energyData.getPxValue()[2]));

			} else if (position == 2) {
				energyView.setTexts("电压有效值", 
						String.format("%.2f", energyData.getUxRmsValue()[0]),
						String.format("%.2f", energyData.getUxRmsValue()[1]),
						String.format("%.2f", energyData.getUxRmsValue()[2]),
						"");

			} else if (position == 3) {
				energyView.setTexts("电流有效值", 
						String.format("%.2f", energyData.getIxRmsValue()[0]),
						String.format("%.2f", energyData.getIxRmsValue()[1]),
						String.format("%.2f", energyData.getIxRmsValue()[2]),
						String.format("%.2f", energyData.getIxRmsValue()[2]));

			} else if (position == 4) {
				energyView.setTexts("功率因数", 
						String.format("%.2f", energyData.getPfxValue()[0]),
						String.format("%.2f", energyData.getPfxValue()[1]),
						String.format("%.2f", energyData.getPfxValue()[2]),
						String.format("%.2f", energyData.getPfxValue()[2]));

			} else if (position == 5) {
				energyView.setTexts("频率", 
						"",
						"",
						"",
						String.format("%.2f", energyData.getFreValue()[0]));

			} 
			else if (position == 6) {
				energyView.setTexts("",
						"Ua/Ia",
						"Ub/Ib",
						"Uc/Ic",
						"");
			} 
			else if (position == 7) {
				energyView.setTexts("U/I夹角值",
						String.format("%.2f", energyData.getRpgValue()[0]),
						String.format("%.2f", energyData.getRpgValue()[1]),
						String.format("%.2f", energyData.getRpgValue()[2]),
						"");
			} 
			else if (position == 8) {
				energyView.setTexts("",
						"Ua/Ub",
						"Ua/Uc",
						"Ub/Uc",
						"");

			} 
			else if (position == 9) {
				energyView.setTexts("U/U夹角值",
						String.format("%.2f", energyData.getRyuValue()[0]),
						String.format("%.2f", energyData.getRyuValue()[1]),
						String.format("%.2f", energyData.getRyuValue()[2]),
						"");

			} 
			else if (position == 10) {
				
				if (energyData.getOrderValue()[0] == 0 &&energyData.getOrderValue()[2] == 0
						) {
					energyView.setTexts("电压相序",
							"正常","",
							"有功功率",
							"正常");
				}else if(energyData.getOrderValue()[0] == 1 &&energyData.getOrderValue()[2] == 1){
					energyView.setTexts("电压相序",
							"异常","",
							"有功功率",
							"异常");
				}
				else if(energyData.getOrderValue()[0] == 0 &&energyData.getOrderValue()[2] == 1){
					energyView.setTexts("电压相序",
							"正常","",
							"有功功率",
							"异常");
				}
				else if(energyData.getOrderValue()[0] == 1 &&energyData.getOrderValue()[2] == 0){
					energyView.setTexts("电压相序",
							"异常","",
							"有功功率",
							"正常");
				}

			} 
			else if (position == 11) {
				if (energyData.getOrderValue()[1] == 0 &&energyData.getOrderValue()[3] == 0) {
					energyView.setTexts("电流相序",
							"正常","",
							"无功功率",
							"正常");
				}else if(energyData.getOrderValue()[1] == 1 &&energyData.getOrderValue()[3] == 1){
					energyView.setTexts("电流相序",
							"异常","",
							"无功功率",
							"异常");
				}
				
				else if(energyData.getOrderValue()[1] == 0 &&energyData.getOrderValue()[3] == 1){
					energyView.setTexts("电流相序",
							"正常","",
							"无功功率",
							"异常");
				}else if(energyData.getOrderValue()[1] == 1 &&energyData.getOrderValue()[3] == 0){
					energyView.setTexts("电流相序",
							"异常","",
							"无功功率",
							"正常");
				}
			} 
			else if (position == 12) {
				if (energyData.getOrderValue()[4] == 0) {
					energyView.setTexts("合相有功",
							"正常","",
							"",
							"");
				}
				else{
					energyView.setTexts("合相有功",
							"异常","",
							"",
							"");
				}
			} 
		}
		return convertView;
	}

}
