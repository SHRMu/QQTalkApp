package Client;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class Message implements Serializable{
    private int Sender;
    private int Receiver;
    private int MessageType;
    private String Info;
    private ArrayList<Integer> Userlist;

    public ArrayList<Integer> getUserlist() {
        return Userlist;
    }

    public Message(ArrayList<Integer> userlist) {
        Userlist = userlist;
    }

    public void setUserlist(ArrayList<Integer> userlist) {
        Userlist = userlist;
    }

    public int getSender() {
        return Sender;
    }

    public int getReceiver() {
        return Receiver;
    }

    public int getMessageType() {
        return MessageType;
    }

    public String getInfo() {
        return Info;
    }

    public void setSender(int sender) {
        Sender = sender;
    }

    public void setReceiver(int receiver) {
        Receiver = receiver;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public Message() {
    }

    public Message(int sender, int receiver, int messageType, String info) {
        Sender = sender;
        Receiver = receiver;
        MessageType = messageType;
        Info = info;
    }
}
