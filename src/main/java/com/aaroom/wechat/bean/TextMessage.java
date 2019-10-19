package com.aaroom.wechat.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement(name = "xml")
@Data
public class TextMessage extends Message{

    @XmlElement(name = "MsgType")
    protected String MsgType = "text";

    @XmlElement(name = "Content")
    protected String Content;

}
