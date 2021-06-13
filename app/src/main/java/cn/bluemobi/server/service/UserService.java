package cn.bluemobi.server.service;

import cn.bluemobi.server.thread.GetOrSendUserInfoThread;
import cn.bluemobi.server.thread.LoginThread;
import cn.bluemobi.server.thread.QueryNameThread;
import cn.bluemobi.server.thread.UpdateUsrInfoThread;

public class UserService {
    public static boolean signIn(String name, String password) {
        LoginThread loginThread = new LoginThread("http://47.98.46.194:8080/MyWeb/SignIn", name, password,-1,"","");
        try {
            loginThread.start();
            loginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return loginThread.getResult();
    }

    public static boolean signUp(String name, String password,int sex,String province,String city) {
        LoginThread loginThread = new LoginThread("http://47.98.46.194:8080/MyWeb/SignUp", name, password,sex,province,city);
        try {
            loginThread.start();
            loginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loginThread.getResult();
    }

    public static String getUserInfo(String name,int sex,String province,String city) {
        GetOrSendUserInfoThread getOrSendUserInfoThread = new GetOrSendUserInfoThread("http://47.98.46.194:8080/MyWeb/GetUserInfoForUser", name,sex,province,city);
        try {
            getOrSendUserInfoThread.start();
            getOrSendUserInfoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getOrSendUserInfoThread.getJson();
    }

    public static boolean queryUserName(String name) {
        QueryNameThread queryNameThread = new QueryNameThread("http://47.98.46.194:8080/MyWeb/QueryUserName", name);
        try {
            queryNameThread.start();
            queryNameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return queryNameThread.getResult();
    }

    public static String addUserInfo(String name,int sex,String province,String city) {
        GetOrSendUserInfoThread getOrSendUserInfoThread = new GetOrSendUserInfoThread("http://47.98.46.194:8080/MyWeb/AddUsrInfo", name,sex,province,city);
        try {
            getOrSendUserInfoThread.start();
            getOrSendUserInfoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getOrSendUserInfoThread.getJson();
    }

    public static Boolean updateUserInfo(String name,int sex,String intro,String province,String city) {
        UpdateUsrInfoThread updateUsrInfoThread = new UpdateUsrInfoThread("http://47.98.46.194:8080/MyWeb/UpdateUserInfo", name,sex,intro,province,city);
        try {
            updateUsrInfoThread.start();
            updateUsrInfoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return updateUsrInfoThread.getResult();
    }


}