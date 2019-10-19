package com.aaroom.wechat.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import com.aaroom.wechat.bean.model.Voice;

@XmlRootElement(name = "xml")
@Data
public class VoiceMessage extends Message {

    @XmlElement(name = "MsgType")
    protected String MsgType = "voice";
    
    /**
     * 语音对象
     */
    @XmlElement(name = "Voice")
    private Voice Voice;

}
