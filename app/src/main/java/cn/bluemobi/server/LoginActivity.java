package cn.bluemobi.server;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import cn.bluemobi.server.service.UserService;

import static cn.bluemobi.server.service.UserService.getUserInfo;

public class LoginActivity extends AppCompatActivity {

    private boolean LoginIsSuccessful = false;
    String userInfoString = "";

    private boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

//        setupService();
//        initData();


//        StepsService stepsService = new StepsService();

//        NotifyService notifyService = new NotifyService();
//        notifyService.initNotification(1000);

//        stepsService.initTodayData(1,"2021-05-25");

//        System.out.println(getUserOneDayStepData(1,"2021-05-28"));
//        System.out.println(        StepsService.queryStepsItems("2021-05-29",10000,1));
//        System.out.println(UserService.getStepData("  123","123"));

        Button btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = ((EditText) findViewById(R.id.etname)).getText().toString();

                String password = ((EditText) findViewById(R.id.etpassword)).getText().toString();
                if (UserService.signIn(name, password))
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            LoginIsSuccessful = true;
                            userInfoString = getUserInfo(name);
                            System.out.println(userInfoString);
                        }
                    });
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登录失败,请检查密码和用户名是否正确!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (LoginIsSuccessful) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("userInfoString", userInfoString);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.etname)).getText().toString();
                String password = ((EditText) findViewById(R.id.etpassword)).getText().toString();
                if (UserService.signUp(name, password)) {
                    UserService.addUserInfo(name);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册失败,用户名已存在！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Button btn_local_mode = (Button) findViewById(R.id.btn_local_mode);
        btn_local_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LocalModeMainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("name", ((EditText) findViewById(R.id.etname)).getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
