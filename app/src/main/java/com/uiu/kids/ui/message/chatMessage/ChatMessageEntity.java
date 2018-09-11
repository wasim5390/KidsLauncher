package com.uiu.kids.ui.message.chatMessage;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatMessageEntity implements Serializable {


    @SerializedName("id")
    public String msgId;
    @SerializedName("sender_id")
    public String senderId;
    @SerializedName("file_url")
    public String url;
    @SerializedName("type")
    public int msgMode;
    @SerializedName("created_at")
    public String time;

    public boolean isAudioPlaying;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMsgMode() {
        return msgMode;
    }

    public void setMsgMode(int msgMode) {
        this.msgMode = msgMode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public boolean isAudioPlaying() {
        return isAudioPlaying;
    }

    public void setAudioPlaying(boolean audioPlaying) {
        isAudioPlaying = audioPlaying;
    }
}
