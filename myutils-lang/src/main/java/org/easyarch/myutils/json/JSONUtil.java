package org.easyarch.myutils.json;/**
 * Description : 
 * Created by YangZH on 16-5-29
 *  下午5:50
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.easyarch.myutils.collection.CollectionUtil;
import org.easyarch.myutils.lang.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description :
 * Created by YangZH on 16-5-29
 * 下午5:50
 */

public class JSONUtil {

    public static boolean isJson(String string) {
        try {
            JSON.parseObject(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String list2Json(List list) {
        if (CollectionUtil.isEmpty(list)){
            return "";
        }
        return JSON.parseArray(JSON.toJSONString(list, true)).toString();
    }

    public static<T> List<T> json2List(String json, Class<T> cls){
        if (StringUtil.isEmpty(json)){
            return new ArrayList<T>();
        }
        return JSON.parseArray(json,cls);
    }

    public static String pojo2Json(Object object) {
        if (object == null){
            return "";
        }
        return JSON.toJSONString(object, true);
    }
    public static<T> T json2Pojo(String json,Class<T> clz) {
        if (StringUtil.isEmpty(json)){
            return null;
        }
        return JSONObject.parseObject(json,clz);
    }

    public static String map2Json(Map<String,Object> jsonMap){
        if (jsonMap ==null||jsonMap.size()== 0){
            return "";
        }
        JSONObject jsonObject = new JSONObject(jsonMap);
        return jsonObject.toJSONString();
    }
    public static Map<String,Object> json2Map(String json){
        if (StringUtil.isEmpty(json)){
            return null;
        }
        JSONObject object = str2Json(json);
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        for (Map.Entry<String,Object> entry:object.entrySet()){
            jsonMap.put(entry.getKey(),entry.getValue());
        }
        return jsonMap;
    }

    public static JSONObject str2Json(String str) {
        if (StringUtil.isEmpty(str))
            return null;
        return JSON.parseObject(str);
    }

    public static<T> T map2Pojo(Map<String,Object> jsonMap,Class<T> clz){
        if (jsonMap ==null||jsonMap.size()== 0){
            return null;
        }
        return json2Pojo(map2Json(jsonMap),clz);
    }

    public static Map<String,Object> pojo2Map(Object object){
        if (object == null){
            return null;
        }
        return json2Map(pojo2Json(object));
    }

    public static String addKV(String json,String key,String value){
        Map<String,Object> jsonMap = json2Map(json);
        jsonMap.put(key,value);
        return map2Json(jsonMap);
    }

    public static String format(String str){
        return JSON.toJSONString(str.trim());
    }


}
