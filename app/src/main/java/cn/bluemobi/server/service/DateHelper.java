package cn.bluemobi.server.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public  class DateHelper {

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
