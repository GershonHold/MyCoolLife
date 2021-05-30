package cn.bluemobi.server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.bluemobi.server.service.DateHelper;
import cn.bluemobi.server.service.StepsSendService;
import cn.bluemobi.step.step.UpdateUiCallBack;
import cn.bluemobi.server.service.StepService;
import cn.bluemobi.step.step.utils.JsonUtils;
import cn.bluemobi.step.step.utils.SharedPreferencesUtils;
import cn.bluemobi.step.view.StepArcView;

import static android.util.Log.e;

/**
 * 记步主页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_data;
    private StepArcView cc;
    private TextView tv_set;
    private TextView tv_isSupport;
    private TextView tv_intro;
    private TextView tv_username;
    private TextView tv_check_ranking_list;

    private SharedPreferencesUtils sp;
    private static Context context;
    private int usr_id =1;
    private String name ;


    private void assignViews() {
        tv_data = (TextView) findViewById(R.id.tv_data);
        cc = (StepArcView) findViewById(R.id.cc);
        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_isSupport = (TextView) findViewById(R.id.tv_isSupport);
//        tv_username = findViewById(R.id.tv_user_name);
//        tv_intro = findViewById(R.id.tv_intro);
        tv_check_ranking_list = (TextView)findViewById(R.id.tv_check_ranking_list) ;

        tv_set.setOnClickListener(this);
        tv_data.setOnClickListener(this);
        tv_check_ranking_list.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        Bundle bundle = this.getIntent().getExtras();
        String userInfoString = bundle.getString("userInfoString");

        usr_id = JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getUsr_id();
        name = JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getName();
//        tv_username.setText("欢迎回来！ "+name);
//        tv_intro.setText("个人简介："+ JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getIntro());

        initData();
    }



    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText("计步中...");
        setupService();
    }

    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);

        Bundle bundleForService = new Bundle();
        bundleForService.putInt("usr_id", usr_id);
        bundleForService.putString("name",name);
        bundleForService.putBoolean("localmode", false);
        System.out.println("MainActivityBundle"+name+usr_id);
        intent.putExtras(bundleForService);

        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_set:
                startActivity(new Intent(this, SetPlanActivity.class));
                break;
            case R.id.tv_data:

                intent = new Intent(MainActivity.this, MyListView.class);
                Bundle bundle = new Bundle();
                bundle.putString("jsonDataString", getHistoryStepsData(usr_id));
                //此处很很关键，做好笔记！
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_check_ranking_list:
//
        }
    }

    public static String getHistoryStepsData(int usr_id){

        return StepsSendService.getUserAllStepData(usr_id);
//
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
}
