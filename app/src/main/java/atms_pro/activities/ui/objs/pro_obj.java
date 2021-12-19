package atms_pro.activities.ui.objs;

import java.util.Map;

public class pro_obj {
//    private String rectitle=null;
//    private String recversion=null;
//    String size=null;
//    private String recdetails=null;
//    private String rec_remote_local=null;
//    private String name=null;
//    private String require_level=null;
//    private String sdcard_cutway=null;
//    private String[] required_items=null;

    //previous value
    private String rectitle = null;
    private String recversion = null;
    String size = null;
    private String recdetails = null;
    private String rec_remote_local = null;
    private String name = null;
    private String require_level = null;
    private String sdcard_cutway = null;
    private String[] required_items = null;
    private String app_type = null;
    private String[] key_valus = null;//get value from input box
    //addition
    private String[] pt_sources = null;
    private Map<String, String> extraMap = null;
    private String flashable_type = null;
    private Map<String, String> input_item_translate_Map = null;
    private Map<String, String> pt_filesystem_Map = null;
    private String[] flash_order = null;
    boolean isMultiBoot = false;

    public pro_obj(String rectitle, String reccersion, String recdetails, String rec_remote_local, String name, String size, String require_level, String sdcard_cutway, String[] required_items) {
        this.require_level = require_level;
        this.sdcard_cutway = sdcard_cutway;
        this.rectitle = rectitle;
        this.recversion = reccersion;
        this.size = size;
        this.recdetails = recdetails;
        this.rec_remote_local = rec_remote_local;
        this.name = name;
        this.required_items = required_items;
    }

    //New add
    //get set function

    public void setPt_sources(String[] pt_sources) {
        this.pt_sources = pt_sources;
    }

    public void setFlash_order(String[] flash_order) {
        this.flash_order = flash_order;
    }

    public String[] getFlash_order() {
        return flash_order;
    }


    public String[] getPt_sources() {
        return pt_sources;
    }

    public void setExtraMap(Map<String, String> extraMap) {
        this.extraMap = extraMap;
    }

    public Map<String, String> getExtraMap() {
        return extraMap;
    }

    public void setFlashable_type(String flashable_type) {
        this.flashable_type = flashable_type;
    }

    public String getFlashable_type() {
        return flashable_type;
    }

    public void setInput_item_translate_Map(Map<String, String> input_item_translate_Map) {
        this.input_item_translate_Map = input_item_translate_Map;
    }

    public Map<String, String> getInput_item_translate_Map() {
        return input_item_translate_Map;
    }

    public void setPt_filesystem_Map(Map<String, String> pt_filesystem_Map) {
        this.pt_filesystem_Map = pt_filesystem_Map;
    }

    public Map<String, String> getPt_filesystem_Map() {
        return pt_filesystem_Map;
    }

    public void setIsMultiBoot(boolean isMultiBoot) {
        this.isMultiBoot = isMultiBoot;
    }
    public boolean getIsMultiBoot(boolean isMultiBoot) {
        return this.isMultiBoot ;
    }



    public void setKey_valus(String[] key_valus) {
        this.key_valus = key_valus;
    }

    public String[] getKey_valus() {
        return key_valus;
    }

    public String getType() {
        return app_type;
    }

    public void setType(String type) {
        this.app_type = type;
    }

    public pro_obj(String rectitle, String reccersion, String recdetails, String rec_remote_local, String name, String size, String require_level, String sdcard_cutway, String [] required_items,String app_type){
        this.require_level=require_level;
        this.sdcard_cutway=sdcard_cutway;
        this.rectitle=rectitle;
        this.recversion=reccersion;
        this.size=size;
        this.recdetails=recdetails;
        this.rec_remote_local=rec_remote_local;
        this.name=name;
        this.required_items=required_items;
        this.app_type=app_type;
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

//    public String getRequire_level() {
//        return require_level;
//    }
//
//    public String[] getRequired_items() {
//        return required_items;
//    }
//
//    public String getSdcard_cutway() {
//        return sdcard_cutway;
//    }
//
//    public String getSize() {
//        return size;
//    }
//
//    public String getRec_remote_local() {
//        return rec_remote_local;
//    }
//
//    public String getRecversion() {
//
//        return recversion;
//    }
//
//    public String getRecdetails() {
//        return recdetails;
//    }
//
//    public String getRectitle() {
//        return rectitle;
//    }
//
//    public String getName() {
//        return name;
//    }
//}
