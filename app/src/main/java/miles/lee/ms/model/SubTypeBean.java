package miles.lee.ms.model;

import java.io.Serializable;

/**
 * @classname: SubTypeBean
 *
 */
public class SubTypeBean implements Serializable {
    /*"child": null,
      "cpId": 32,
      "operationTagId": 698,
      "operationTagName": "本周佳片",
      "picUrl": "http://file.local.jushihuyu.com:8060/video_operation/2016/09/18/20/38/29/1474202309158.png",
      "position": 11,
      "seq": 2*/
    public Object child;
    public int cpId;
    public int operationTagId;
    public String operationTagName;
    public String picUrl;
    public int position;
    public int seq;
}
