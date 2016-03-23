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

    public Trigger(String tType, String aType, String contact, String message, String name, int timeHour, int timeMinute){
        this.triggerType = tType;
        this.actionType = aType;
        this.contactNumber = contact;
        this.message = message;
        this.actionName = name;
        this.timeHour = timeMinute;
        this.timeMinute = timeMinute;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void TriggerType(String tType) {
        this.triggerType = tType;
    }

    public String getActionType() {
        return actionType;
    }

    public void ActionType(String aType) {
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

}
