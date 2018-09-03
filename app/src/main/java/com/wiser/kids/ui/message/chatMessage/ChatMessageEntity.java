package com.wiser.kids.ui.message.chatMessage;

public class ChatMessageEntity {

   public String msgId;

   public String url;

   public int msgMode;

   public String time;


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
}
