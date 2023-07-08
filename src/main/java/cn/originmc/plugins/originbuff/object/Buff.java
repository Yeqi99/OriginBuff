package cn.originmc.plugins.originbuff.object;

import cn.originmc.plugins.originbuff.data.BuffData;
import cn.originmc.plugins.origincore.util.text.VariableString;

import java.util.HashMap;
import java.util.Map;

public class Buff {
    private String name;
    private int duration;
    private long startTime;
    private Map<String, Object> attributes;

    public Buff(String name, int duration) {
        this.name = name;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        this.attributes = new HashMap<>();
    }

    public String getSymbolAndRemainingTime() {
        VariableString vString=new VariableString(getBuffFormat().getFormat());
        for (String s : vString.getAllVariable()) {
            if (s.contains("!")){
                String key=s.replace("!","");
                vString.setVariable(s, (String) attributes.get(key));
            }
        }

        int remainingTime = getDuration();
        String timeString;

        if (remainingTime < 60) {
            timeString = remainingTime + "秒";
        } else {
            int minutes = remainingTime / 60;
            timeString = minutes + "分钟";
        }
        vString.setVariable("duration",timeString+"");
        return vString.getResultString();
    }
    public String getName() {
        return name;
    }
    public BuffFormat getBuffFormat(){
        return BuffData.getBuffFormatByName(getName());
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    public void increaseDuration(int amount) {
        duration += amount;
    }

    public void decreaseDuration(int amount) {
        duration -= amount;
        if (duration < 0) {
            duration = 0;
        }
    }
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
