package cn.bluemobi.server;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import static cn.bluemobi.server.service.UserService.getUserInfo;

public class IndexActivity extends TabActivity {

    private  TabHost tab_host;

    private Intent mainActivityIntent;
    private Intent navigationActivityIntent;
    private String userInfoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tab_host = getTabHost();
        TabWidget tabWidget = tab_host.getTabWidget();
        LinearLayout lLayout = (LinearLayout) tab_host.getChildAt(0);
        lLayout.removeViewAt(0);
        lLayout.addView(tabWidget);

        initIntent();
        addSpec();
    }

    private void initIntent() {

        Bundle bundleFromLoginActivity = this.getIntent().getExtras();
        String name = bundleFromLoginActivity.getString("name");
        //后三个参数在此方法中无用，但是必须传入这三个参数
        userInfoString = getUserInfo(name, -1, "", "");
                                    System.out.println(userInfoString);

        mainActivityIntent = new Intent(this, MainActivity.class);
        Bundle bundleForMainactivity = new Bundle();
        bundleForMainactivity.putString("userInfoString", userInfoString);
        mainActivityIntent.putExtras(bundleForMainactivity);

        navigationActivityIntent= new Intent(this, NavigationActivity.class);
//        Bundle bundleFornavigationActivity = new Bundle();
//        bundleFornavigationActivity.putString("name","gershon");
//        navigationActivityIntent.putExtras(bundleFornavigationActivity);
    }

    /**
     * 为tabHost添加各个标签项
     */
    private void addSpec() {
        tab_host.addTab(this.buildTagSpec("主页",
                R.string.tabName1,R.drawable.cb_plan_selector, mainActivityIntent));
        tab_host.addTab(this.buildTagSpec("导航页",
                R.string.tabName2,R.drawable.cb_plan_selector, navigationActivityIntent));
    }

    private TabHost.TabSpec buildTagSpec(String tagName, int tagLable,
                                         int icon, Intent content) {
        return tab_host
                .newTabSpec(tagName)
                .setIndicator(getResources().getString(tagLable),
                        getResources().getDrawable(icon)).setContent(content);
    }

}








