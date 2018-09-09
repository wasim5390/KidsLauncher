package com.wiser.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.ui.message.chatMessage.ChatMessageEntity;

import java.util.List;

public class GetAllChatResponse extends BaseResponse {


    @SerializedName("files")
    public List<ChatMessageEntity> msgList;

    @SerializedName("file")
    public ChatMessageEntity entity;

    public ChatMessageEntity getEntity() {
        return entity;
    }

    public void setEntity(ChatMessageEntity entity) {
        this.entity = entity;
    }

    public List<ChatMessageEntity> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<ChatMessageEntity> msgList) {
        this.msgList = msgList;
    }


}
