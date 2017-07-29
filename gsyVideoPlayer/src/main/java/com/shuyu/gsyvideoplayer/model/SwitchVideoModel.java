package com.shuyu.gsyvideoplayer.model;

/**
 * @author yinhui
 * @classname: SwitchVideoModel
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017/5/2 15:57
 */
public class SwitchVideoModel {
    private String url;
    private String name;

    public SwitchVideoModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
