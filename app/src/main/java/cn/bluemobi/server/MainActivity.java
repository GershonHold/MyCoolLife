package cn.bluemobi.server;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import cn.bluemobi.server.bean.WeatherBean;
import cn.bluemobi.server.service.APSService;
import cn.bluemobi.server.service.DateHelper;
import cn.bluemobi.server.service.StepService;
import cn.bluemobi.server.service.StepsSendService;
import cn.bluemobi.server.thread.GetWeatherThread;
import cn.bluemobi.step.step.UpdateUiCallBack;
import cn.bluemobi.step.step.utils.JsonUtils;
import cn.bluemobi.step.step.utils.SharedPreferencesUtils;
import cn.bluemobi.step.step.utils.XmlParserUtil;
import cn.bluemobi.step.view.StepArcView;
import static cn.bluemobi.server.service.UserService.getUserInfo;

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
    private TextView tv_weather_data;

    private SharedPreferencesUtils sp;
    private static Context context;
    private int usr_id =1;
    private String name ;
    private String userInfoString;

    public static boolean needToUpdate = false;
    private String locationCity = "";


    private void assignViews() {
        tv_data = (TextView) findViewById(R.id.tv_data);
        cc = (StepArcView) findViewById(R.id.cc);
        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_isSupport = (TextView) findViewById(R.id.tv_isSupport);
//        tv_username = findViewById(R.id.tv_user_name);
//        tv_intro = findViewById(R.id.tv_intro);
        tv_check_ranking_list = (TextView)findViewById(R.id.tv_check_ranking_list) ;
        tv_weather_data = (TextView)findViewById(R.id.tv_weather_data) ;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //添加Toolbar的返回按钮
//        if(getSupportActionBar() != null)
//            // Enable the Up button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//           View barItemProfilePicture = findViewById(R.id.action_favorite);
//        barItemProfilePicture.setBackground(ContextCompat.getDrawable(this, R.drawable.woman));

        tv_set.setOnClickListener(this);
        tv_data.setOnClickListener(this);
        tv_check_ranking_list.setOnClickListener(this);
        tv_weather_data.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册广播，以便接受定位服务信息
        registerBoardCast();
        assignViews();
        context = MainActivity.this;

        try {
            //此处userInforString是包含包括用户id内的所有用户信息Json字符串
            Bundle bundle = this.getIntent().getExtras();
            userInfoString = bundle.getString("userInfoString");
            System.out.println(userInfoString+"MainActivityActivity");


            usr_id = JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getUsr_id();
            name = JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getName();
//        tv_username.setText("欢迎回来！ "+name);
//        tv_intro.setText("个人简介："+ JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getIntro());

            initData();
        }catch(Exception e){
            e.getStackTrace();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        System.out.println("执行了onPrepareOptionsMenu");

        try {
            if (JsonUtils.getUserInfoBeanJsonList(getUserInfo(name, -1, "", "")).get(0).getSex() == 0) {
                menu.findItem(R.id.action_favorite).setIcon(
                        R.drawable.woman);
            } else {
                menu.findItem(R.id.action_favorite).setIcon(
                        R.drawable.man);
            }
        }catch (Exception e){
            e.getStackTrace();
        }

        // getSupportMenuInflater().inflate(R.menu.book_detail, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        System.out.println("执行了onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("执行了onOptionsItemSelected");


//        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_favorite:

                Bundle bundle = this.getIntent().getExtras();
                bundle.putString("userInfoString",userInfoString);

                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;


            case R.id.action_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                System.out.println("执行了 MainActivity onOptionsItemSelected Default");
                return super.onOptionsItemSelected(item);
        }

    }

    public static Context getContext() {
        return context;
    }


    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText("计步中...");
        setupService();

        //如果无法从定位服务获取到城市信息，就从用户信息中获取
        if(locationCity.equals("")){
            locationCity = JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getCity();
        }
        setWeatherData(locationCity);

    }


    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intentOfStepService = new Intent(this, StepService.class);
        Intent intentOfAPSService = new Intent(this, APSService.class);

        Bundle bundleForService = new Bundle();
        bundleForService.putInt("usr_id", usr_id);
        bundleForService.putString("name",name);
        bundleForService.putBoolean("localmode", false);
        System.out.println("MainActivityBundle"+name+usr_id);
        intentOfStepService.putExtras(bundleForService);

        isBind = bindService(intentOfStepService, conn, Context.BIND_AUTO_CREATE);
        startService(intentOfStepService);
        startService(intentOfAPSService);

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

                intent = new Intent(MainActivity.this, HistoryStepDataListView.class);
                Bundle HistoryStepBundle = new Bundle();
                HistoryStepBundle.putString("jsonDataString", getHistoryStepsData(usr_id));
                //此处很很关键，做好笔记！
                intent.putExtras(HistoryStepBundle);
                startActivity(intent);
                break;
            case R.id.tv_check_ranking_list:
                System.out.println(getOneDayRankStepsData());
                intent = new Intent(MainActivity.this, RankDataListView.class);
                Bundle rankDataBundle = new Bundle();
                rankDataBundle.putString("rankDataString",getOneDayRankStepsData());
                //此处很很关键，做好笔记！
                intent.putExtras(rankDataBundle);
                startActivity(intent);
                break;
            case R.id.tv_weather_data:

                break;

        }
    }

    public static String getHistoryStepsData(int usr_id){

        return StepsSendService.getUserAllStepData(usr_id);
//
    }

    public static String getOneDayRankStepsData(){

        return StepsSendService.getOneDayRankStepData(DateHelper.getTodayDate());
//
    }

    public  void setWeatherData(String city){

        GetWeatherThread getWeatherThread = new GetWeatherThread(city);
        try {
            getWeatherThread.start();
            getWeatherThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WeatherBean> weatherBeans = null;
        weatherBeans = WeatherBean.parse(XmlParserUtil.parse(getWeatherThread.getWeatherdata()));

        tv_weather_data.setText(weatherBeans.get(0).toString());

    }

    public class MainActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            locationCity = bundle.getString("city");
            locationCity = locationCity.substring(0,locationCity.length()-1);
        }
    }

    public void registerBoardCast() {
//注册广播接收器
        MainActivityReceiver receiver = new MainActivityReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("cn.bluemobi.server.service.APSService");
        MainActivity.this.registerReceiver(receiver,filter);

    }

//
//        public static void updateUi(){
//
//        if(needToUpdate){
//            needToUpdate = false;
//        }else {
//            needToUpdate = true;
//        }
//        System.out.println(needToUpdate+"321");
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
}
