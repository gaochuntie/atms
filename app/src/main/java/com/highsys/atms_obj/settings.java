package com.highsys.systemchanger;

import android.text.Editable;

public class settings {
    //1 emmc
    //0 ufs
    public static String processmsg;
    public static String processtitle;
    public static int processcache;
    public static String packpath;
    public static String[] allsettings=new String[20];
    public static String getTempdir(){
        return "/sdcard/highsys/temp/";
    }
    public static String getSDCARDTYPE(){
        return allsettings[0];
    }
    public static String getMUSICENABLE(){
        return allsettings[1];
    }
    public static String getBACKUPSDIR(){
        return allsettings[2];
    }
    public static String getSYSTEMFILE(){
        return allsettings[3];
    }
    public static String getIMAGEWORKDIR(){
        return allsettings[4];
    }
    public static String getSHELLPATH(){
        return allsettings[5];
    }
    public static boolean IS_SHELLFILE(){
      if (Integer.valueOf(allsettings[5])==0){
          return false;
      }
      return true;
    }
    public static String getUSERNAME(){
        return allsettings[6];
    }
    public static String getSYSORDER(){
        return allsettings[7];
    }
    public static void setProcessTitle(String s ){
        processtitle=s;

    }
    public static void setProcessMsg(String s ){
      processmsg=s;

    }
}
