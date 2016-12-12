package com.cydroid.coreframe.tool.util;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtil {

    /**
     * 使用反射机制解析json
     *
     * @param cls        目标holder
     * @param jsonString json字符串
     * @return 目标holder的对象
     */
    @SuppressLint("DefaultLocale")
    public static <T> T parseJson(String jsonString, Class<T> cls) {
        try {
            JSONObject json = new JSONObject(jsonString);
            Map<String, Method> methodMap = new HashMap<String, Method>();
            Method[] methods = cls.getMethods();
            for (Method m : methods) {
                String name = m.getName().toUpperCase();
                if (name.startsWith("SET")) {
                    methodMap.put(name.substring(3), m);
                }
            }
            Iterator<String> iterator = json.keys();
            T out = cls.newInstance();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String uppercasekey = key.toUpperCase();

                if (methodMap.containsKey(uppercasekey)) {
                    Method _m = methodMap.get(uppercasekey);
                    Class<?> c = _m.getParameterTypes()[0];
                    if (c.getName().equals("int")) {
                        int value = json.getInt(key);
                        _m.invoke(out, value);
                    } else if (c.getName().equals("long")) {
                        long value = json.getLong(key);
                        _m.invoke(out, value);
                    } else if (c.getName().equals("double")) {
                        double value = json.getDouble(key);
                        _m.invoke(out, value);
                    } else if (c.getName().equals("boolean")) {
                        boolean value = json.getBoolean(key);
                        _m.invoke(out, value);
                    } else if (c == Integer.class) {
                        _m.invoke(out, (Integer) json.getInt(key));
                    } else if (c == Long.class) {
                        _m.invoke(out, (Long) json.getLong(key));
                    } else if (c == Double.class) {
                        _m.invoke(out, json.getDouble(key));
                    } else if (c == Boolean.class) {
                        _m.invoke(out, json.getBoolean(key));
                    } else if (c == String.class) {
                        _m.invoke(out, json.getString(key));
                    }
                }
            }
            return out;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 使用反射机制解析json
     *
     * @param array 要解析的jsonarry
     * @param cls   目标holder.class
     * @return 目标holder的集合
     */
    public static <T> ArrayList<T> parseArray(JSONArray array, Class<T> cls) {
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                list.add(parseJson(array.get(i).toString(), cls));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return list;
    }
}
