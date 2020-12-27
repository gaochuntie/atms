package com.highsys.atms_obj;

public class rec_obj {
    private String rectitle=null;
    private String recversion=null;
    String size=null;
    private String recdetails=null;
    private String rec_remote_local=null;
    private String name=null;
    public rec_obj(String rectitle,String reccersion,String recdetails,String rec_remote_local,String name,String size){
        this.rectitle=rectitle;
        this.recversion=reccersion;
        this.size=size;
        this.recdetails=recdetails;
        this.rec_remote_local=rec_remote_local;
        this.name=name;
    }

    public String getSize() {
        return size;
    }

    public String getRec_remote_local() {
        return rec_remote_local;
    }

    public String getRecversion() {

        return recversion;
    }

    public String getRecdetails() {
        return recdetails;
    }

    public String getRectitle() {
        return rectitle;
    }

    public String getName() {
        return name;
    }
}
