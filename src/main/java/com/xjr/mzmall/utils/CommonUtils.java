package com.xjr.mzmall.utils;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    /**
     * 多id转integer列表
     * @param ids
     * @return
     */
    public static List<Integer> StringsToList(String ids){
        String[] split = ids.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : split) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }


    /**
     * 将list 平均分配n等份
     * @param source
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    public static List<String> splitDetailImg(String s) {
        String[] imgSplit = s.split("#");
        ArrayList<String> imgList = new ArrayList<>();
        for (String s1 : imgSplit) {
            imgList.add(s1);
        }
        return imgList;
    }
}
