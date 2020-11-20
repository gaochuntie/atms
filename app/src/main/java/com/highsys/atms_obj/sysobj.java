package com.highsys.pages;

public class sysobj {
    private String dtbo_path=null;
    private String boot_path=null;
    private String partition_tab_sda=null;
    private String partition_tab_sde=null;
    private String partition_tab_sdf=null;
    private String partition_tab_mmcblk0=null;
    private String ID="";
    private int CORE_TYPE=-1;


    public void setDtbo_path(String s){
        this.dtbo_path=s;
    }
    public void setBoot_path(String s){
        this.boot_path=s;
    }

    public void setPartition_tab_sdf(String partition_tab_sdf) {
        this.partition_tab_sdf = partition_tab_sdf;
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

    public void setCORE_TYPE(int CORE_TYPE) {
        this.CORE_TYPE = CORE_TYPE;
    }

    public void setPartition_tab_mmcblk0(String partition_tab_mmcblk0) {
        this.partition_tab_mmcblk0 = partition_tab_mmcblk0;
    }

    public void setPartition_tab_sda(String partition_tab_sda) {
        this.partition_tab_sda = partition_tab_sda;
    }

    public void setPartition_tab_sde(String partition_tab_sde) {
        this.partition_tab_sde = partition_tab_sde;
    }

    public  sysobj(String ID, String dtbo_path, String boot_path, String partition_tab_mmcblk0){
        this.setCORE_TYPE(1);
        this.ID=ID;
        this.dtbo_path=dtbo_path;
        this.boot_path=boot_path;
        this.partition_tab_mmcblk0=partition_tab_mmcblk0;
   }
    public  sysobj(String ID,String dtbo_path,String boot_path,String partition_tab_sda,String partition_tab_sde,String partition_tab_sdf){
        this.ID=ID;
        this.setCORE_TYPE(0);
        this.dtbo_path=dtbo_path;
        this.boot_path=boot_path;
        this.partition_tab_sda=partition_tab_sda;
        this.partition_tab_sde=partition_tab_sde;
        this.partition_tab_sdf=partition_tab_sdf;
    }

    public int getCORE_TYPE() {
        return CORE_TYPE;
    }

    public String getPartition_tab_mmcblk0() {
        return partition_tab_mmcblk0;
    }

    public String getPartition_tab_sda() {
        return partition_tab_sda;
    }

    public String getPartition_tab_sde() {
        return partition_tab_sde;
    }

    public String getPartition_tab_sdf() {
        return partition_tab_sdf;
    }
}
