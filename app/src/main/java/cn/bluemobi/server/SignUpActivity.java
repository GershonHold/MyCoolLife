package cn.bluemobi.server;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cn.bluemobi.server.service.UserService;

public class SignUpActivity extends AppCompatActivity {

    private RadioGroup SexradioGroup;
    private RadioButton radioButton_Man;
    private RadioButton radioButton_Woman;
    private Spinner city_spinner;
    private Spinner province_spinner;

    private int sex =-1 ;
    private String name;
    private String password;
    private String province = "";
    private String city = "";

    private ArrayAdapter<CharSequence> province_adapter;
    private ArrayAdapter<CharSequence> city_adapter;
    private Integer provinceId, cityId;
    private String strProvince, strCity, strCounty;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        SexradioGroup = (RadioGroup) findViewById(R.id.SexradioGroup);
        radioButton_Man = (RadioButton) findViewById(R.id.radioButton_Man);
        radioButton_Woman = (RadioButton) findViewById(R.id.radioButton_Woman);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        province_spinner = (Spinner) findViewById(R.id.province_spinner);

        loadSpinner();

        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.etname)).getText().toString();
                String password = ((EditText) findViewById(R.id.etpassword)).getText().toString();

                if (!UserService.signUp(name, password,sex,city,province)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String name = ((EditText) findViewById(R.id.etname)).getText().toString();
                            String password = ((EditText) findViewById(R.id.etpassword)).getText().toString();

                            if(!UserService.queryUserName(name)) {
                                Toast.makeText(SignUpActivity.this, "用户名已存在！", Toast.LENGTH_SHORT).show();
                            }
                            if (name.equals("") || name.contains(" ")) {
                                Toast.makeText(SignUpActivity.this, "用户名不合法！", Toast.LENGTH_SHORT).show();
                            }
                            if (password.equals("") || password.contains(" ")) {
                                Toast.makeText(SignUpActivity.this, "密码不合法！", Toast.LENGTH_SHORT).show();
                            }
                            if (sex==-1) {
                                Toast.makeText(SignUpActivity.this, "请选择性别！", Toast.LENGTH_SHORT).show();
                            }
                            if (province.equals("")||city.equals("")) {
                                Toast.makeText(SignUpActivity.this, "请选择城市！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    UserService.addUserInfo(name,sex,province,city);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
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

    private void loadSpinner() {
//        display = (EditText) findViewById(R.id.display_edit);
        province_spinner = (Spinner) findViewById(R.id.province_spinner);
        // 绑定省份的数据
        province_spinner.setPrompt("请选择省份");
        province_adapter = ArrayAdapter.createFromResource(this,
                R.array.province_item, android.R.layout.simple_spinner_item);
        province_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province_spinner.setAdapter(province_adapter);
        // select(province_spinner, province_adapter, R.array.province_item);
        // 添加监听，一开始的时候城市，县区的内容是不显示的而是根据省的内容进行联动
        province_spinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        provinceId = province_spinner.getSelectedItemPosition();
                        strProvince = province_spinner.getSelectedItem()
                                .toString();// 得到选择的内容，也就是省的名字
                        city_spinner = (Spinner) findViewById(R.id.city_spinner);

                        if (true) {
                            System.out.println("province: "
                                    + province_spinner.getSelectedItem()
                                    .toString() + provinceId.toString());
                            //获得选中省份
                            province = province_spinner.getSelectedItem()
                                    .toString();

                            city_spinner = (Spinner) findViewById(R.id.city_spinner);
                            city_spinner.setPrompt("请选择城市");// 设置标题
                            select(city_spinner, city_adapter, citys[provinceId]);// 城市一级的数据绑定
                            /*
                             * 通过这个city[provinceId]指明了该省市的City集合 R。array.beijing
                             */
                            city_spinner
                                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(
                                                AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                                            cityId = city_spinner
                                                    .getSelectedItemPosition();// 得到city的id
                                            strCity = city_spinner
                                                    .getSelectedItem()
                                                    .toString();// 得到city的内容
                                            Log.v("test", "city: "
                                                    + city_spinner
                                                    .getSelectedItem()
                                                    .toString()// 输出测试一下
                                                    + cityId.toString());
                                            //获得选中城市
                                            city = city_spinner
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

    private boolean checkProfile(){

        if(sex==-1||city.equals("")||province.equals("")){
            return false;
        }

        return true;
    }
}








