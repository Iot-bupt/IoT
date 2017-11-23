package extensions;

import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.scripts.JO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tangjialiang on 2017/11/6.
 *
 */
public class MessgeRecorder {

    private JSONObject msgJson = null ;

    public MessgeRecorder() {
        msgJson = new JSONObject() ;
    }

    public MessgeRecorder(String jsonStr) {
        msgJson = JSONObject.parseObject(jsonStr) ;
    }

    public MessgeRecorder(JSONObject jsonObject) {
        this.msgJson = jsonObject ;
    }

    public MessgeRecorder addCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String timeStr = df.format(new Date()) ;
        msgJson.put("current_time", timeStr) ;
        return this ;
    }

    public MessgeRecorder addDeviceName(String deviceName) {
        msgJson.put("device_name", deviceName) ;
        return this ;
    }

    public MessgeRecorder addValue(String value) {
        msgJson.put("value", value) ;
        return this ;
    }

    public MessgeRecorder addUid(String uid) {
        msgJson.put("uid", uid) ;
        return this ;
    }
    public MessgeRecorder addData(String data) {
        msgJson.put("Data", data) ;
        return this ;
    }

    public JSONObject toJson() {
        return msgJson ;
    }

    public static void main(String[] args) {
        MessgeRecorder mr = new MessgeRecorder() ;
        mr.addCurTime().addDeviceName("mac") ;
        String s = mr.toJson().toString();
        System.out.println(s) ;
    }
}