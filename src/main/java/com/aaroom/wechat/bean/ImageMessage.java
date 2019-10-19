package com.aaroom.wechat.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import com.aaroom.wechat.bean.model.Image;

@XmlRootElement(name = "xml")
@Data
public class ImageMessage extends Message {

    @XmlElement(name = "MsgType")
    protected String MsgType = "image";
    
    @XmlElement(name = "Image")
    private Image Image;

}
