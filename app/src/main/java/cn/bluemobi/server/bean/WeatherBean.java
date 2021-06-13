package cn.bluemobi.server.bean;

import java.util.ArrayList;
import java.util.List;

public class WeatherBean {

    private List<String> pics;
    private String date;
    private String temperature;
    private String wind;


    //该方法的作用是将从API获取的天气数据，参数为XML解析后返回的List，将此List中的天气信息转化为WeatherBean里面的对应属性信息
    public static List<WeatherBean> parse(List<String> nodes) {

        ArrayList<WeatherBean> weatherBeans = new ArrayList<>();


        WeatherBean weatherBean1 = new WeatherBean();
        List<String> pic1 = new ArrayList<>();
//        pic1.add("a_" + nodes.get(8));
//        pic1.add("a_" + nodes.get(9));
        weatherBean1.setPics(pic1);
        weatherBean1.setDate(nodes.get(6));
        weatherBean1.setTemperature(nodes.get(5));
        weatherBean1.setWind(nodes.get(7));


        weatherBeans.add(weatherBean1);


//        WeatherBean weatherBean2 = new WeatherBean();
//        List<String> pic2 = new ArrayList<>();
////        pic2.add("a_" + nodes.get(15));
////        pic2.add("a_" + nodes.get(16));
//        weatherBean1.setPics(pic2);
//        weatherBean2.setDate(nodes.get(13));
//        weatherBean2.setTemperature(nodes.get(12));
//        weatherBean2.setWind(nodes.get(14));
//        weatherBeans.add(weatherBean2);
//
//        WeatherBean weatherBean3 = new WeatherBean();
//        List<String> pic3 = new ArrayList<>();
////        pic3.add("a_" + nodes.get(20));
////        pic3.add("a_" + nodes.get(21));
//        weatherBean1.setPics(pic3);
//        weatherBean3.setDate(nodes.get(18));
//        weatherBean3.setTemperature(nodes.get(17));
//        weatherBean3.setWind(nodes.get(19));
//        weatherBeans.add(weatherBean3);
        return weatherBeans;


    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "日期：" + date + '\n' +
                "温度：" + temperature + '\n' +
                "风力：" + wind;
    }
}
