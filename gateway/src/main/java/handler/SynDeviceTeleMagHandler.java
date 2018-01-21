package handler;

import data.CommonData;
import data.Device;
import util.ThingsBoardApi;
import config.Config;

/**
 * Created by Administrator on 2017/10/24.
 */
public class SynDeviceTeleMagHandler implements  Handler{

    private static SynDeviceTeleMagHandler instance;
    private  ThingsBoardApi thingsBoardApi;
    String userTocken;

    private SynDeviceTeleMagHandler(){
        try{
            thingsBoardApi   = ThingsBoardApi.getInstance(Config.THINGSBOARD_URL,Config.THINGSBOARD_PORT);
            userTocken = thingsBoardApi.api_token(Config.USER_NAME,Config.PASSWORD) ;
        }catch(Exception e){
            System.err.println("init SynDeviceTeleMagHandler failed");
        }
    }

    public static SynDeviceTeleMagHandler getInstance(){
        if (instance==null){
            synchronized (SynDeviceTeleMagHandler.class){
                if (instance==null) instance = new SynDeviceTeleMagHandler();
            }
        }
        return instance;
    }
    public void handleMsg(Object obj){

        Device device = (Device)obj;
        System.out.println("handle a Tele msg ="+device.toString());
        try {
            if (CommonData.getInstance().devicesTokens.containsKey(device.getuId())) {
                String deviceToken = CommonData.getInstance().devicesTokens.get(device.getuId());
                thingsBoardApi.api_telemetry(userTocken, deviceToken, device.getInfo());
            } else {
                String deviceId = thingsBoardApi.api_device(userTocken, device.getDeviceName(), "default");
                String deviceToken = thingsBoardApi.api_accessToken(userTocken, deviceId);
                thingsBoardApi.api_telemetry(userTocken, deviceToken, device.getInfo());
                CommonData.getInstance().devicesTokens.put(device.getuId(), deviceToken);
            }
        }catch(Exception e){
            System.err.println("fail to handle msg ");
            try{
                userTocken = thingsBoardApi.api_token(Config.USER_NAME,Config.PASSWORD) ;
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }
    }
}
