package com.pivot.flibo.data.network.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Detail implements Serializable
{

    @SerializedName("notification_id")
    @Expose
    private int notificationId;
    @SerializedName("tapped_on")
    @Expose
    private String tappedOn;
    @SerializedName("button_index")
    @Expose
    private int buttonIndex;
    @SerializedName("is_snoozed")
    @Expose
    private boolean isSnoozed;
    @SerializedName("is_turned_off")
    @Expose
    private boolean isTurnedOff;
    private final static long serialVersionUID = 2461670750529058849L;

    public Detail() {
    }

    public Detail(int notificationId, String tappedOn, int buttonIndex, boolean isSnoozed, boolean isTurnedOff) {
        this.notificationId = notificationId;
        this.tappedOn = tappedOn;
        this.buttonIndex = buttonIndex;
        this.isSnoozed = isSnoozed;
        this.isTurnedOff = isTurnedOff;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getTappedOn() {
        return tappedOn;
    }

    public void setTappedOn(String tappedOn) {
        this.tappedOn = tappedOn;
    }

    public int getButtonIndex() {
        return buttonIndex;
    }

    public void setButtonIndex(int buttonIndex) {
        this.buttonIndex = buttonIndex;
    }

    public boolean isSnoozed() {
        return isSnoozed;
    }

    public void setSnoozed(boolean snoozed) {
        isSnoozed = snoozed;
    }

    public boolean isTurnedOff() {
        return isTurnedOff;
    }

    public void setTurnedOff(boolean turnedOff) {
        isTurnedOff = turnedOff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Detail detail = (Detail) o;

        if (notificationId != detail.notificationId) return false;
        if (buttonIndex != detail.buttonIndex) return false;
        if (isSnoozed != detail.isSnoozed) return false;
        if (isTurnedOff != detail.isTurnedOff) return false;
        return tappedOn != null ? tappedOn.equals(detail.tappedOn) : detail.tappedOn == null;
    }

    @Override
    public int hashCode() {
        int result = notificationId;
        result = 31 * result + (tappedOn != null ? tappedOn.hashCode() : 0);
        result = 31 * result + buttonIndex;
        result = 31 * result + (isSnoozed ? 1 : 0);
        result = 31 * result + (isTurnedOff ? 1 : 0);
        return result;
    }
}
