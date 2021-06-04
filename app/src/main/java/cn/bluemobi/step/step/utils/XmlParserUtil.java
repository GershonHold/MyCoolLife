package cn.bluemobi.step.step.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlParserUtil {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<String> parse(String xml) {
        List<String> res = null;
        //创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //创建一个DocumentBuilder的对象
        try {
            //创建DocumentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过DocumentBuilder对象的parser方法加载
            Document document = db.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            //获取所有string节点的集合
            NodeList stringList = document.getElementsByTagName("string");
            res = new ArrayList<>(stringList.getLength());
            for (int i = 0; i < stringList.getLength(); i++) {
                Node s = stringList.item(i);
                res.add(s.getTextContent());
            }
        } catch (Exception e) {
            Log.e("xml解析异常:", e.getMessage());
            return Collections.emptyList();
        }
        return res;
    }
}
