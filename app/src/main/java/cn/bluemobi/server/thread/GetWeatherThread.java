package cn.bluemobi.server.thread;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cn.bluemobi.server.service.GetWeatherHelper;

public class GetWeatherThread extends Thread {

    private String province;
    private String city;


    private boolean result;
    private String json;
    private String weatherData;

    public GetWeatherThread(String city) {

        this.city = city;
    }

    @Override
    public void run() {
        try {

//            System.out.println(GetWeatherHelper.getWeatherByCityName(city)+"===");
            weatherData = GetWeatherHelper.getWeatherByCityName(city);
        }catch (Exception e){
            e.getStackTrace();
    }

}

    public String getWeatherdata() {
        return weatherData;
    }

    public boolean getResult() {
        return result;
    }

}
