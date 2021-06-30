package cn.bluemobi.server;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.bluemobi.step.step.utils.JsonUtils;
import cn.bluemobi.step.step.utils.SharedPreferencesUtils;

import static cn.bluemobi.server.service.UserService.getUserInfo;
import static cn.bluemobi.server.service.UserService.updateUserInfo;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferencesUtils sp;

    private LinearLayout layout_titlebar;
    private ImageView iv_left;
    private ImageView iv_right;
    private EditText et_info;
    private Button btn_save_profile;

    private String userInfoString;

    private ArrayAdapter<CharSequence> province_adapter;
    private ArrayAdapter<CharSequence> city_adapter;
    private Integer provinceId, cityId;
    private String strProvince, strCity, strCounty;
    private int sex =-1;
    private String intro = "";
    private String pre_province = "";
    private String pre_city = "";
    private String province = "";
    private String city = "";

    private RadioGroup SexradioGroup;
    private RadioButton radioButton_Man;
    private RadioButton radioButton_Woman;
    private Spinner city_spinner_profile;
    private Spinner province_spinner_profile;

    private int infoTextMaxLenth = 99;  //用户的个性签名最长允许的长度为99个字符

    private int[] citys = {R.array.empty_item,R.array.beijin_province_item,
            R.array.tianjin_province_item, R.array.heibei_province_item,
            R.array.shanxi1_province_item, R.array.neimenggu_province_item,
            R.array.liaoning_province_item, R.array.jilin_province_item,
            R.array.heilongjiang_province_item, R.array.shanghai_province_item,
            R.array.jiangsu_province_item, R.array.zhejiang_province_item,
            R.array.anhui_province_item, R.array.fujian_province_item,
            R.array.jiangxi_province_item, R.array.shandong_province_item,
            R.array.henan_province_item, R.array.hubei_province_item,
            R.array.hunan_province_item, R.array.guangdong_province_item,
            R.array.guangxi_province_item, R.array.hainan_province_item,
            R.array.chongqing_province_item, R.array.sichuan_province_item,
            R.array.guizhou_province_item, R.array.yunnan_province_item,
            R.array.xizang_province_item, R.array.shanxi2_province_item,
            R.array.gansu_province_item, R.array.qinghai_province_item,
            R.array.linxia_province_item, R.array.xinjiang_province_item,
            R.array.hongkong_province_item, R.array.aomen_province_item,
            R.array.taiwan_province_item};

    private void assignViews() {
        layout_titlebar = (LinearLayout) findViewById(R.id.layout_titlebar);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        et_info = (EditText) findViewById(R.id.et_info);

        btn_save_profile = (Button) findViewById(R.id.btn_save_profile);

        SexradioGroup = (RadioGroup) findViewById(R.id.SexradioGroup);
        radioButton_Man = (RadioButton) findViewById(R.id.radioButton_Man);
        radioButton_Woman = (RadioButton) findViewById(R.id.radioButton_Woman);
        city_spinner_profile = (Spinner) findViewById(R.id.city_spinner_profile);
        province_spinner_profile = (Spinner) findViewById(R.id.province_spinner_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_profile_layout);
        assignViews();
        initData();
        addListener();
        loadSpinner();


        et_info.addTextChangedListener(new TextWatcher() {
            private int cou = 0;
            int selectionEnd = 0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                cou = before + count;
                String editable = et_info.getText().toString();
                String str = stringFilter(editable); //过滤特殊字符
                if (!editable.equals(str)) {
                    et_info.setText(str);
                }
                et_info.setSelection(et_info.length());
                cou = et_info.length();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (cou > infoTextMaxLenth) {
                    selectionEnd = et_info.getSelectionEnd();
                    s.delete(infoTextMaxLenth, selectionEnd);
                }
            }
        });

        SexradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//              选中 文字 显示红色，没有选中显示黑色
                if (radioButton_Man.isChecked()) {
                    radioButton_Man.setTextColor(Color.parseColor("#FF0033"));
                    sex = 1;
                } else {
                    radioButton_Man.setTextColor(Color.parseColor("#000000"));
                }

                if (radioButton_Woman.isChecked()) {
                    radioButton_Woman.setTextColor(Color.parseColor("#FF0033"));
                    sex = 0;
                } else {
                    radioButton_Woman.setTextColor(Color.parseColor("#000000"));
                }
                System.out.println(sex);
            }
        });


    }


    public static String stringFilter(String str)throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    public void initData() {

        Bundle bundle = this.getIntent().getExtras();
        userInfoString = bundle.getString("userInfoString");
        userInfoString = getUserInfo(JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getName(),-1,"","");

        et_info.setText(JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getIntro());
        pre_province = JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getProvince();
        pre_city = JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getCity();

        if(JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getSex()==1){
            radioButton_Man.isChecked();
            SexradioGroup.check(radioButton_Man.getId());
        }else{
            radioButton_Woman.isChecked();
            SexradioGroup.check(radioButton_Woman.getId());

        }
    }


    public void addListener() {
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        btn_save_profile.setOnClickListener(this);
    }

    private void save() {

        sex = radioButton_Man.isChecked()?1:0;
        intro = ((EditText) findViewById(R.id.et_info)).getText().toString();

        if(!province.equals("")&&!city.equals("")){
            if(updateUserInfo(JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getName(),sex,intro,province,city)){
                Toast.makeText(ProfileActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(ProfileActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(updateUserInfo(JsonUtils.getUserInfoBeanJsonList(userInfoString).get(0).getName(),sex,intro,pre_province,pre_city)){
                Toast.makeText(ProfileActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(ProfileActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadSpinner() {
//        display = (EditText) findViewById(R.id.display_edit);
        province_spinner_profile = (Spinner) findViewById(R.id.province_spinner_profile);
        // 绑定省份的数据
//        province_spinner_profile.setPrompt("请选择省份");
        province_adapter = ArrayAdapter.createFromResource(this,
                R.array.province_item, android.R.layout.simple_spinner_item);
        province_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province_spinner_profile.setAdapter(province_adapter);
         select(province_spinner_profile, province_adapter, R.array.province_item);
        // 添加监听，一开始的时候城市，县区的内容是不显示的而是根据省的内容进行联动
        province_spinner_profile
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        provinceId = province_spinner_profile.getSelectedItemPosition();
                        strProvince = province_spinner_profile.getSelectedItem()
                                .toString();// 得到选择的内容，也就是省的名字
                        city_spinner_profile = (Spinner) findViewById(R.id.city_spinner_profile);

                        if (true) {
                            System.out.println("province: "
                                    + province_spinner_profile.getSelectedItem()
                                    .toString() + provinceId.toString());
                            //获得选中省份
                            province = province_spinner_profile.getSelectedItem()
                                    .toString();

                            city_spinner_profile = (Spinner) findViewById(R.id.city_spinner_profile);
//                            city_spinner_profile.setPrompt("请选择城市");// 设置标题
                            select(city_spinner_profile, city_adapter, citys[provinceId]);// 城市一级的数据绑定
                            /*
                             * 通过这个city[provinceId]指明了该省市的City集合 R。array.beijing
                             */
                            city_spinner_profile
                                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(
                                                AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                                            cityId = city_spinner_profile
                                                    .getSelectedItemPosition();// 得到city的id
                                            strCity = city_spinner_profile
                                                    .getSelectedItem()
                                                    .toString();// 得到city的内容
                                            Log.v("test", "city: "
                                                    + city_spinner_profile
                                                    .getSelectedItem()
                                                    .toString()// 输出测试一下
                                                    + cityId.toString());
                                            //获得选中城市
                                            city = city_spinner_profile
                                                    .getSelectedItem()
                                                    .toString();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }

                                    });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

    }


    /* 通过方法动态的添加适配器 */
    private void select(Spinner spin, ArrayAdapter<CharSequence> adapter,
                        int arry) {
        // 注意这里的arry不仅仅但是一个整形，他代表了一个数组！
        adapter = ArrayAdapter.createFromResource(this, arry,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        // spin.setSelection(0,true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_save_profile:
                save();
                break;
        }
    }
}

