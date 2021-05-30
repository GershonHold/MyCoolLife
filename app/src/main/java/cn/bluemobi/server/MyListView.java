/**
 * 
 */
package cn.bluemobi.server;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bluemobi.server.bean.StepsBean;
import cn.bluemobi.step.step.utils.JsonUtils;

public class MyListView extends ListActivity {

	private static Context context;
	private String jsonDataString;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.vlist,
				new String[]{"date","steps","img"},
				new int[]{R.id.date,R.id.steps,R.id.img});
		setListAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		context = getApplicationContext();

		Bundle bundle = this.getIntent().getExtras();
		jsonDataString = bundle.getString("jsonDataString");
		Iterator<StepsBean> iterator = JsonUtils.getStepsBeanJsonList(jsonDataString).iterator();

        while (iterator.hasNext()){
            StepsBean stepsBean = iterator.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", stepsBean.getDate());
			map.put("steps", stepsBean.getSteps());
			map.put("img", R.drawable.i1);
			list.add(map);
            System.out.println(stepsBean.getSi_id()+ " "+stepsBean.getDate()+" "+ stepsBean.getSteps()+" "+ stepsBean.getUsr_id());
        }




//		map = new HashMap<String, Object>();
//		map.put("date", "G2");
//		map.put("info", "google 2");
//		map.put("img", R.drawable.i2);
//		list.add(map);
//
//		map = new HashMap<String, Object>();
//		map.put("date", "G3");
//		map.put("info", "google 3");
//		map.put("img", R.drawable.i2);
//		list.add(map);
		
		return list;
	}
}
