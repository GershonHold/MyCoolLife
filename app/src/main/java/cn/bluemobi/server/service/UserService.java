package cn.bluemobi.server.service;

import cn.bluemobi.server.thread.GetOrSendUserInfoThread;
import cn.bluemobi.server.thread.LoginThread;

public class UserService {
    public static boolean signIn(String name, String password) {
        LoginThread loginThread = new LoginThread("http://47.98.46.194:8080/MyWeb/SignIn", name, password);
        try {
            loginThread.start();
            loginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return loginThread.getResult();
    }

    public static boolean signUp(String name, String password) {
        LoginThread loginThread = new LoginThread("http://47.98.46.194:8080/MyWeb/SignUp", name, password);
        try {
            loginThread.start();
            loginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loginThread.getResult();
    }

    public static String getUserInfo(String name) {
        GetOrSendUserInfoThread getOrSendUserInfoThread = new GetOrSendUserInfoThread("http://47.98.46.194:8080/MyWeb/GetUserInfoForUser", name);
        try {
            getOrSendUserInfoThread.start();
            getOrSendUserInfoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getOrSendUserInfoThread.getJson();
    }

    public static String addUserInfo(String name) {
        GetOrSendUserInfoThread getOrSendUserInfoThread = new GetOrSendUserInfoThread("http://47.98.46.194:8080/MyWeb/AddUsrInfo", name);
        try {
            getOrSendUserInfoThread.start();
            getOrSendUserInfoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getOrSendUserInfoThread.getJson();
    }




}