package cn.bluemobi.server;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bluemobi.server.service.DateHelper;
import cn.bluemobi.step.adapter.CommonAdapter;
import cn.bluemobi.step.adapter.CommonViewHolder;
import cn.bluemobi.server.bean.LocalDbStepsBean;
import cn.bluemobi.step.step.utils.DbUtils;

/**
 * Created by yuandl on 2016-10-18.
 */

public class HistoryActivity extends AppCompatActivity {
    private LinearLayout layout_titlebar;
    private ImageView iv_left;
    private ImageView iv_right;
    private ListView lv;

    private String name;

    private void assignViews() {
        layout_titlebar = (LinearLayout) findViewById(R.id.layout_titlebar);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        lv = (ListView) findViewById(R.id.lv);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_history);

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("name");

        assignViews();
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        initData();
    }



    private void initData() {
        setEmptyView(lv);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }

        List<LocalDbStepsBean> stepDatas =   DbUtils.getQueryAll(LocalDbStepsBean.class, "name",new String[]{name});
        Logger.d("stepDatas="+stepDatas);
        lv.setAdapter(new CommonAdapter<LocalDbStepsBean>(this,stepDatas,R.layout.item) {
            @Override
            protected void convertView(View item, LocalDbStepsBean stepData) {
                TextView tv_date= CommonViewHolder.get(item,R.id.tv_date);
                TextView tv_step= CommonViewHolder.get(item,R.id.tv_step);
                tv_date.setText(stepData.getDate());
                tv_step.setText(stepData.getSteps()+"步");
            }
        });
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }
}
