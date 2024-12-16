package org.example.Backend;

public interface MessageInterface {


    String getMessage_id();

    void setMessage_id(String messageId);

    String getSender_id();

    void setSender_id(String senderId);

    String getReceiver_id();

    void setReceiver_id(String receiverId);

    String getMessage_content();

    void setMessage_content(String messageContent);
}
