package com.chinatelecom.xjdh.ui;

import java.sql.SQLException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.db.DatabaseHelper;
import com.chinatelecom.xjdh.model.GroupingName;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.widget.EditText;

@EActivity(R.layout.activity_grouping_name)
public class GroupingNameActivity extends Activity {

	private GroupingName gName;
	@ViewById(R.id.et_append_grouping_name)
	EditText et_append_grouping_name;
	@Extra
	String stationName;
	@OrmLiteDao(helper = DatabaseHelper.class, model = GroupingName.class)
	Dao<GroupingName, Integer> sDao;
	@AfterViews
	void initData() {
		gName = new GroupingName();
	}

	/**
	 * 保存分组名称
	 */
	@Click(R.id.btn_save_grouping_name)
	void groupingName() {
		judge();
	}

	private void judge() {
		if (et_append_grouping_name.getText().toString() == null
				|| et_append_grouping_name.getText().toString().equals("")) {
			et_append_grouping_name.setError("局站名称不能为空");
			return;
		}
		getData();
		this.finish();
	}

	private void getData() {
		try {
			gName.setGroupingName(et_append_grouping_name.getText().toString());
			gName.setStationName(stationName);
			sDao.createOrUpdate(gName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
