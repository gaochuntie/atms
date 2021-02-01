package atms_pro.activities.ui.objs;

public class pro_obj {
    private String rectitle=null;
    private String recversion=null;
    String size=null;
    private String recdetails=null;
    private String rec_remote_local=null;
    private String name=null;
    private String require_level=null;
    private String sdcard_cutway=null;
    private String[] required_items=null;

    public pro_obj(String rectitle, String reccersion, String recdetails, String rec_remote_local, String name, String size,String require_level,String sdcard_cutway,String [] required_items){
        this.require_level=require_level;
        this.sdcard_cutway=sdcard_cutway;
        this.rectitle=rectitle;
        this.recversion=reccersion;
        this.size=size;
        this.recdetails=recdetails;
        this.rec_remote_local=rec_remote_local;
        this.name=name;
        this.required_items=required_items;
    }

    public String getRequire_level() {
        return require_level;
    }

    public String[] getRequired_items() {
        return required_items;
    }

    public String getSdcard_cutway() {
        return sdcard_cutway;
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
