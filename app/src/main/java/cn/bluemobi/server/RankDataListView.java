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

import cn.bluemobi.server.bean.RankStepDataBean;
import cn.bluemobi.server.bean.StepsBean;
import cn.bluemobi.step.step.utils.JsonUtils;

public class RankDataListView extends ListActivity {

	private static Context context;
	private String rankDataString;
	private int sex;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.rank_data_vlist,
				new String[]{"name","steps","intro","province_city","img"},
				new int[]{R.id.name,R.id.steps,R.id.intro,R.id.province_city,R.id.img,});
		setListAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		context = getApplicationContext();

		Bundle bundle = this.getIntent().getExtras();
		rankDataString = bundle.getString("rankDataString");


		Iterator<RankStepDataBean> iterator = JsonUtils.getRankStepDataBeanJsonList(rankDataString).iterator();

        while (iterator.hasNext()){
			RankStepDataBean rankStepDataBean = iterator.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", rankStepDataBean.getName());
			map.put("steps", rankStepDataBean.getSteps());
			map.put("intro", rankStepDataBean.getIntro());
			map.put("province_city", rankStepDataBean.getProvince()+"-"+rankStepDataBean.getCity());
			if(rankStepDataBean.getSex()==1){
				map.put("img", R.drawable.man);
			}else if(rankStepDataBean.getSex()==0){
				map.put("img", R.drawable.woman);
			}
			list.add(map);
        }
		
		return list;
	}
}
