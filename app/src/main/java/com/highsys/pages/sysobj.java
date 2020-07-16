package com.highsys.pages;

public class sysobj {
    private String dtbo_path=null;
    private String boot_path=null;
    private String partition_tab=null;
    private String ID="";


    public void setDtbo_path(String s){
        this.dtbo_path=s;
    }
    public void setBoot_path(String s){
        this.boot_path=s;
    }
    public void setPartition_tab(String s ){
        this.partition_tab=s;

    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public String getBoot_path() {
        return boot_path;
    }

    public String getDtbo_path() {
        return dtbo_path;
    }

    public String getPartition_tab() {
        return partition_tab;
    }
   public  sysobj(String ID,String dtbo_path,String boot_path,String partition_tab){
        this.ID=ID;
        this.dtbo_path=dtbo_path;
        this.boot_path=boot_path;
        this.partition_tab=partition_tab;
   }
}
