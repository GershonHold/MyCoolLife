package cn.bluemobi.server.thread;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginThread extends Thread {
        private String path;
        private String name;
        private String password;
        private  int sex;
        private String province;
        private String city;


    private int resultcode;
        private boolean result;
        private String json;

    public LoginThread(String path, String name, String password,int sex,String province,String city ) {
            this.path = path;
            this.name = name;
            this.password = password;
            this.sex = sex;
            this.province = province;
            this.city = city;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(path);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(8000);//设置连接超时时间
                httpURLConnection.setReadTimeout(8000);//设置读取超时时间
                httpURLConnection.setRequestMethod("POST");//设置请求方法,post

                String data = "name=" + URLEncoder.encode(name, "utf-8") + "&password=" + URLEncoder.encode(password, "utf-8")+ "&sex=" + URLEncoder.encode(String.valueOf(sex), "utf-8")+ "&province=" + URLEncoder.encode(province, "utf-8")+ "&city=" + URLEncoder.encode(String.valueOf(city), "utf-8");//设置数据
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置响应类型
                httpURLConnection.setRequestProperty("Content-Length", data.length() + "");//设置内容长度
                httpURLConnection.setDoOutput(true);//允许输出

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(data.getBytes("utf-8"));//写入数据

                result = (httpURLConnection.getResponseCode()==200);

                StringBuilder stringBuilder = new StringBuilder();
                try (
                        //接收服务器端发回的响应
                        BufferedReader in = new BufferedReader(new InputStreamReader
                                (httpURLConnection.getInputStream(), "utf-8"))) {

                    String line;
                    while ((line = in.readLine()) != null) {
                        if (!line.equals(null))
                            stringBuilder.append(line);
                    }
                    json = stringBuilder.toString();
                }

            } catch (Exception e) {
                System.out.println("POST异常" + e);
                e.printStackTrace();
            }
        }

        public int getResultCode() {
            return resultcode;
        }

    public boolean getResult() {
        return result;
    }

}
