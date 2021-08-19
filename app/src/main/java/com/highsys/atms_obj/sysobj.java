package com.highsys.atms_obj;

public class sysobj {
    private String dtbo_path="";
    private String boot_path="";
    private String partition_tab_sda="";
    private String partition_tab_sdb="";
    private String partition_tab_sdc="";
    private String partition_tab_sdd="";
    private String partition_tab_sde="";
    private String partition_tab_sdf="";
    private String partition_tab_mmcblk0="";
    private String ID="";
    private int CORE_TYPE=-1;


    public void setDtbo_path(String s){
        this.dtbo_path=s;
    }
    public void setBoot_path(String s){
        this.boot_path=s;
    }

    public void setPartition_tab_sdb(String partition_tab_sdb) {
        this.partition_tab_sdb = partition_tab_sdb;
    }

    public void setPartition_tab_sdc(String partition_tab_sdc) {
        this.partition_tab_sdc = partition_tab_sdc;
    }

    public void setPartition_tab_sdd(String partition_tab_sdd) {
        this.partition_tab_sdd = partition_tab_sdd;
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

    public String getPartition_tab_sdb() {
        return partition_tab_sdb;
    }

    public String getPartition_tab_sdc() {
        return partition_tab_sdc;
    }

    public String getPartition_tab_sdd() {
        return partition_tab_sdd;
    }

    public  sysobj(String ID, String dtbo_path, String boot_path, String partition_tab_mmcblk0){
        this.setCORE_TYPE(1);
        this.ID=ID;
        this.dtbo_path=dtbo_path;
        this.boot_path=boot_path;
        this.partition_tab_mmcblk0=partition_tab_mmcblk0;
   }
    public  sysobj(String ID,String dtbo_path,String boot_path,String partition_tab_sda,String partition_tab_sde,String partition_tab_sdf,String partition_tab_sdb,String partition_tab_sdc,String partition_tab_sdd){
        this.ID=ID;
        this.setCORE_TYPE(0);
        this.dtbo_path=dtbo_path;
        this.boot_path=boot_path;
        this.partition_tab_sda=partition_tab_sda;
        this.partition_tab_sde=partition_tab_sde;
        this.partition_tab_sdf=partition_tab_sdf;
        this.partition_tab_sdb=partition_tab_sdb;
        this.partition_tab_sdc=partition_tab_sdc;
        this.partition_tab_sdd=partition_tab_sdd;
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
