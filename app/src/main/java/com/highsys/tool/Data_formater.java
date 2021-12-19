/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.highsys.tool;

import android.util.Log;

import java.security.PublicKey;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author jackmaxpale
 */
public class Data_formater {
    public  Map Data_to_Map(String s){
        
        Map<String,String> key_valusMap=new TreeMap<String, String>();
        key_valusMap.clear();
        String[] stage1=s.split(";");

        
        for(int i=0;i<stage1.length;i++){
            System.out.println(stage1[i].split("=").length);
            key_valusMap.put((stage1[i].split("="))[0], (stage1[i].split("="))[1]);
        }
        if (key_valusMap.isEmpty()) {
            printMsg("Null key_value map.");
            return null;
        }
        return key_valusMap;
        
    }
    public static String[] Data_to_StringArry(String s){
        String[] tarStrings=s.split(";");
        if (tarStrings.length==0) {
            printMsg("Null string[] arry.");
            return null;
        }
        return tarStrings;
        
    }
    public static void printMsg(String msg){
        Log.d("Console : ",msg);
    }
    
}
