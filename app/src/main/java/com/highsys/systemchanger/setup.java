package com.highsys.systemchanger;
import com.highsys.*;

import java.io.File;

public class setup {
    public static int seten(){
        setCommand.resultCom r;
        r=setCommand.execCommand(new String[]{"cd /sdcard","mkdir highsys","cd highsys","mkdir backups","mkdir imgwork","mkdir music","mkdir systems","mkdir temp","touch setup.txt","cd /data","mkdir highsys","cd /data/highsys/","mkdir zips","cd /sdcard/highsys/imgwork","mkdir unpack","mkdir repack"},true,true);
        tools.preaik();
        return r.result1;
    }
}
