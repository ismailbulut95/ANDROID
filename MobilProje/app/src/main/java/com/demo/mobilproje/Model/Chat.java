package com.demo.mobilproje.Model;

public class Chat {
    private String sender;
    private String receiver;
    private String message;

    public Chat(){
    }

    public Chat(String sender,String receiver,String message){
        this.setSender(sender);
        this.setReceiver(receiver);
        this.setMessage(message);
    }
    //region -->Encapsulation Chat
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    //endregion
}
