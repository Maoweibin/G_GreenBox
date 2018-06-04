package com.task.dd.greenbox.jsonpull;

import android.util.Log;

import com.task.dd.greenbox.bean.Pot_Message;

import org.json.JSONException;
import org.json.JSONObject;

/**处理花盆信息的Json解析类
 * Created by dd on 2017/5/1.
 */

public class PotMessageJson {
    private Pot_Message pot_message;
    private String temperature;//温度
    private String humidity;//环境湿度
    private String soil_humidity;//土壤湿度
    private String sun;//光照
    private String water_level;//水槽水位
    private  String pump;
    private  String light;
    private String switch_num;
    private String url;
    public Pot_Message  messagePull(String jsonString) throws JSONException {
        JSONObject jsonObject=new JSONObject(jsonString);
        JSONObject dataObject=jsonObject.getJSONObject("data");

        switch_num = dataObject.getString("switch");
        light = switch_num.substring(0, 1);
        pump = switch_num.substring(1, 2);
        pot_message=new Pot_Message();
        pot_message.setWater(pump);
        pot_message.setLight(light);

        url = dataObject.getString("image");
        pot_message.setUrl(url);

        JSONObject paramsObject = dataObject.getJSONObject("params");
        Log.e("param", String.valueOf(paramsObject));
        temperature=paramsObject.getString("environment_temperature");
        humidity=paramsObject.getString("environment_humidity");
        soil_humidity=paramsObject.getString("soil_humidity");
        sun=paramsObject.getString("light_intensity");
        water_level=paramsObject.getString("water_level");

        pot_message.setTemperature(temperature);
        pot_message.setHumidity(humidity);
        pot_message.setSoil_humidity(soil_humidity);
        pot_message.setSun(sun);
        pot_message.setWater_level(water_level);

        return pot_message;
    }
    /*public List<String> SwitchPull (String jsonString) throws JSONException {
        switch_num=new ArrayList<>();
        JSONObject jsonObject=new JSONObject(jsonString);
        JSONObject valueJsonObject=jsonObject.getJSONObject("value");
        String lignt_string=valueJsonObject.getString("light");
        String pumb_string=valueJsonObject.getString("pumb");
        switch_num.add(lignt_string);
        switch_num.add(pumb_string);
        return switch_num;
    }*/

}
