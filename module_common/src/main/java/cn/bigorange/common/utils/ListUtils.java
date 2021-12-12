package cn.bigorange.common.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    private ListUtils() {
    }

    public static List<String> strSplitToList(final String str) {
        List<String> list = new ArrayList<String>();
        final String nameStr = StringUtils.trim(str);
        if (StringUtils.isEmpty(nameStr)) {
            return list;
        }
        String[] array = nameStr.split("[、，,\\s]+");
        if (array.length == 0) {
            return list;
        }
        for (String temp : array) {
            final String value = StringUtils.trim(temp);
            if (StringUtils.isNotEmpty(value)) {
                list.add(value);
            }
        }
        return list;
    }


    public static String listToString(final List<String> optionList) {
        final List<String> list = (optionList == null ? new ArrayList<String>(0) : optionList);
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public static List<String> stringToList(final String str) {
        final String nameStr = StringUtils.trim(str);
        if (StringUtils.isEmpty(nameStr)) {
            return new ArrayList<String>(0);
        }
        Gson gson = new Gson();
        return gson.fromJson(nameStr, new TypeToken<List<String>>() {
        }.getType());
    }

}
