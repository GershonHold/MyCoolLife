package cn.bluemobi.server.service;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class GetWeatherHelper {

    private static GetWeatherHelper getWeatherHelper = new GetWeatherHelper();



//    private static ThreadPoolExecutor weatherService
//            = new ThreadPoolExecutor(8,
//            8,
//            2000,
//            TimeUnit.MILLISECONDS,
//            new LinkedBlockingDeque<Runnable>(),
//            new ThreadFactory() {
//                @Override
//                public Thread newThread(Runnable r) {
//                    Thread thread = new Thread(r);
//                    thread.setName("weather-service");
//                    thread.setDaemon(true);
//                    return thread;
//                }
//            });

    private static OkHttpClient httpClient = new OkHttpClient();

    private static final String URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName?";

    private static final String THE_CITY_NAME = "theCityName";

    private static final int HTTP_OK = 200;

    public GetWeatherHelper() {

    }

    public static GetWeatherHelper getInstance() {
        return getWeatherHelper;
    }



    public static String getWeatherByCityName(final String cityName) {
                try {
                    Request request = new Request.Builder()
                            .url(URL + THE_CITY_NAME + "=" + cityName)
                            .build();
                    Call call = httpClient.newCall(request);
                    Response response = call.execute();
                    if (response.code() == HTTP_OK) {
                        String str = response.body().string();
                        return str;
                    } else {
                        // TODO do with the error info
                        return "";
                    }
                } catch (IOException e) {
                    Log.e("查询出错！:", e.getMessage());
                    return "";
                }
            }

    }
