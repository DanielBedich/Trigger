package com.example.danielbedich.trigger;

/**
 * Created by Brock on 3/21/2016.
 */

public class Trigger {
    private String triggerType;
    private String actionType;
    private String contactNumber;
    private String actionName;
    private String message;
    private int timeHour;
    private int timeMinute;
    private String gps;

    public Trigger(String tType, String aType, String contact, String message, String name, int timeHour, int timeMinute, String gps){
        this.triggerType = tType;
        this.actionType = aType;
        this.contactNumber = contact;
        this.message = message;
        this.actionName = name;
        this.timeHour = timeMinute;
        this.timeMinute = timeMinute;
        this.gps = gps;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String tType) {
        this.triggerType = tType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String aType) {
        this.triggerType = aType;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContact(String contact) {
        this.contactNumber = contact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String name) {
        this.actionName = name;
    }

    public int getTimeHour() {
        return timeHour;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setTime(int timeHour, int timeMinute) {
        this.timeHour = timeHour;
        this.timeMinute = timeMinute;
    }

    public String getGPS() {
        return gps;
    }

    public void setGPS(String gps) {
        this.gps = gps;
    }

}
