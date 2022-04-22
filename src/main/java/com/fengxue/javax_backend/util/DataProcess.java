package com.fengxue.javax_backend.util;

import java.util.*;

public class DataProcess {

    public static Set<String> getDelimitSet(String org, String delimiter){
        if(org==null||org.equals("")) return new HashSet<>();
        String[] words;
        words = org.split(delimiter); // 分割字符串
        return new HashSet<>(Arrays.asList(words));
    }

    public static String[] getDelimitArray(String org, String delimiter){
        if(org==null||org.equals("")) return new String[0];
        String[] words;
        words = org.split(delimiter); // 分割字符串
        return words;
    }

    public static Map<String,String> getBisectMap(String org, String delimiter, String biDelimiter){
        if(org==null||org.equals("")) return new HashMap<>();
        String[] words;
        words = org.split(delimiter); // 分割字符串
        Map<String,String> retMap = new HashMap<>();
        for(String word:words){
            String[] bis;
            bis = word.split(biDelimiter); // 分割字符串
            retMap.put(bis[0],bis[1]);
        }
        return retMap;
    }

    public static String set2String(Set<String> set,String delimiter){
        if(set == null) return "";
        StringBuilder ret = new StringBuilder();
        for(String str:set){
            ret.append(str);
            ret.append(delimiter);
        }
        return ret.toString();
    }
}
