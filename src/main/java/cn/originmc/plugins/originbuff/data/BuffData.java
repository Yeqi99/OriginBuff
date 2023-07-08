package cn.originmc.plugins.originbuff.data;

import cn.originmc.plugins.originbuff.OriginBuff;
import cn.originmc.plugins.originbuff.object.Buff;
import cn.originmc.plugins.originbuff.object.BuffFormat;
import cn.originmc.plugins.origincore.util.data.yaml.YamlManager;

import java.util.ArrayList;
import java.util.List;

public class BuffData {
    public static YamlManager yamlManager;
    public static List<BuffFormat> buffFormats;
    public static void load(){
        buffFormats=new ArrayList<>();
        yamlManager=new YamlManager(OriginBuff.getInstance(),"buff",true);
        for (String id : yamlManager.getIdList()) {
            String name= (String) yamlManager.get(id,"name");
            List<String> info= (List<String>) yamlManager.get(id,"info");
            List<String> attributeFTs= (List<String>) yamlManager.get(id,"attributes");
            String format=(String) yamlManager.get(id,"format");
            BuffFormat buffFormat=new BuffFormat(id,name);
            buffFormat.setInfo(info);
            buffFormat.setAttributesFTs(attributeFTs);
            buffFormat.setFormat(format);
            buffFormats.add(buffFormat);
        }
    }
    public static BuffFormat getBuffFormat(String id){
        for (BuffFormat buffFormat : buffFormats) {
            if (buffFormat.getId().equalsIgnoreCase(id)){
                return buffFormat;
            }
        }
        return null;
    }
    public static BuffFormat getBuffFormatByName(String name){
        for (BuffFormat buffFormat : buffFormats) {
            if (buffFormat.getName().equalsIgnoreCase(name)){
                return buffFormat;
            }
        }
        return null;
    }
    public static List<String> getBuffFormatIds(){
        List<String> ids=new ArrayList<>();
        for (BuffFormat buffFormat : buffFormats) {
            ids.add(buffFormat.getId());
        }
        return ids;
    }
}
