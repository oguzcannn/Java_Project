package org.example.Backend;

import java.time.Instant;

public class Message implements MessageInterface {
    private String message_id;
    private String sender_id;
    private String receiver_id;
    private String message_content;

    public Message(String sender_id,String receiver_id,String message_content)
    {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message_content = message_content;
        this.message_id = "MSG-" + Instant.now().toEpochMilli();
    }
    public String getSender_id(){
        return sender_id;
    }

    public String getReceiver_id(){
        return receiver_id;
    }

    public String getMessage_content(){
        return message_content;
    }

    public String getMessage_id(){
        return message_id;
    }

    public void setSender_id(String sender_id){
        this.sender_id = sender_id;
    }

    public void setReceiver_id(String receiver_id){
        this.receiver_id = receiver_id;
    }

    public void setMessage_content(String message_content){
        this.message_content = message_content;
    }

    public void setMessage_id(String message_id){
        this.message_id = getMessage_id();
    }
}

