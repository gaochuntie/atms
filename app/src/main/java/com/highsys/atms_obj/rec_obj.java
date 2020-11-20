package com.highsys.atms_obj;

public class rec_obj {
    private String rectitle=null;
    private String recversion=null;
    private String recdetails=null;
    private String rec_remote_local=null;
    public rec_obj(String rectitle,String reccersion,String recdetails,String rec_remote_local){
        this.rectitle=rectitle;
        this.recversion=reccersion;
        this.recdetails=recdetails;
        this.rec_remote_local=rec_remote_local;
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
}
