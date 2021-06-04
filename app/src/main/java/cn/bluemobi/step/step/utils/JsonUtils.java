package cn.bluemobi.step.step.utils;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.bluemobi.server.bean.RankStepDataBean;
import cn.bluemobi.server.bean.StepsBean;
import cn.bluemobi.server.bean.UserInfoBean;

public class JsonUtils {


    //根据json数据解析返回一个List<HashMap<String, Object>>集合

    public static List<StepsBean> getStepsBeanJsonList(String json) {

        List<StepsBean> dataList;
        dataList = new ArrayList();

        try {
            dataList = JSON.parseArray(json, StepsBean.class);

            return dataList;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return dataList;
    }

    public static List<UserInfoBean> getUserInfoBeanJsonList(String json) {

        List<UserInfoBean> dataList;
        dataList = new ArrayList();

        try {
            dataList = JSON.parseArray(json, UserInfoBean.class);

            return dataList;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return dataList;
    }

    public static List<RankStepDataBean> getRankStepDataBeanJsonList(String json) {

        List<RankStepDataBean> dataList;
        dataList = new ArrayList();

        try {
            dataList = JSON.parseArray(json, RankStepDataBean.class);

            return dataList;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return dataList;
    }


}
