package cn.bluemobi.server.service;


import cn.bluemobi.server.thread.GetOneDayRankDataThread;
import cn.bluemobi.server.thread.GetUsersAllStepsDataThread;
import cn.bluemobi.server.thread.GetUsersOneDayStepsDataThread;
import cn.bluemobi.server.thread.StepsItemsThread;

public class StepsSendService{

    //记录的步数会上传服务器，服务器端程序后续会上传为另一项目
    public  static boolean queryStepsItems(String date, int steps, int usr_id) {
        StepsItemsThread loginThread = new StepsItemsThread("http://{your host}:8080/MyWeb/QueryStepsItems", date, steps,usr_id);
        try {
            loginThread.start();
            loginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loginThread.getResult();
    }

    public  static boolean addStepsItems(String date, int steps, int usr_id) {
        StepsItemsThread loginThread = new StepsItemsThread("http://{your host}:8080/MyWeb/AddStepsItems", date, steps,usr_id);
        try {
            loginThread.start();
            loginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loginThread.getResult();
    }

    public  static boolean updateStepsItems(String date, int steps, int usr_id) {
        StepsItemsThread loginThread = new StepsItemsThread("http://{your host}:8080/MyWeb/UpdateStepsItems", date, steps,usr_id);
        try {
            loginThread.start();
            loginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loginThread.getResult();
    }



    public static String getUserAllStepData(int usr_id) {
        GetUsersAllStepsDataThread getUsersAllStepsDataThread = new GetUsersAllStepsDataThread("http://{your host}:8080/MyWeb/GetDataForUsers", usr_id);
        try {
            getUsersAllStepsDataThread.start();
            getUsersAllStepsDataThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getUsersAllStepsDataThread.getJson();
    }

    public static String getUserOneDayStepData(int usr_id, String date) {
        GetUsersOneDayStepsDataThread getUsersOneDayStepsDataThread = new GetUsersOneDayStepsDataThread("http://{your host}:8080/MyWeb/GetStepDatasByDateUsrid", usr_id,date);
        try {
            getUsersOneDayStepsDataThread.start();
            getUsersOneDayStepsDataThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getUsersOneDayStepsDataThread.getJson();
    }

    public static String getOneDayRankStepData(String date) {
        GetOneDayRankDataThread getOneDayRankDataThread = new GetOneDayRankDataThread("http://{your host}:8080/MyWeb/GetOneDayRankDataForUsr", date);
        try {
            getOneDayRankDataThread.start();
            getOneDayRankDataThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getOneDayRankDataThread.getJson();
    }


}



