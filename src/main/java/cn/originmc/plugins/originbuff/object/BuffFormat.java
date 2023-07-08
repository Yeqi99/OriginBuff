package cn.originmc.plugins.originbuff.object;

import cn.originmc.plugins.origincore.util.text.FormatText;

import java.util.ArrayList;
import java.util.List;

public class BuffFormat {
    private String id;
    private String name;
    private List<String> info=new ArrayList<>();
    private List<String> attributesFTs=new ArrayList<>();
    private String format;

    public BuffFormat(String id,String name){
        setId(id);
        setName(name);
    }
    public Buff createBuff(String[] args,int duration){
        Buff buff=new Buff(getName(),duration);
        for (String attributesFT : attributesFTs) {
            FormatText formatText=new FormatText(attributesFT);
            String key=formatText.getValue("key");
            String value=formatText.getValue("value");
            if (value.contains("!")){
                int index= Integer.parseInt(value.replace("!",""));
                value=args[index];
            }
            buff.setAttribute(key,value);
        }
        return buff;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }

    public List<String> getAttributesFTs() {
        return attributesFTs;
    }

    public void setAttributesFTs(List<String> attributesFTs) {
        this.attributesFTs = attributesFTs;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
