package com.pivot.flibo.data.network.model.notification;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Action implements Serializable
{

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("button_text")
    @Expose
    private String buttonText;
    private final static long serialVersionUID = 2461670750529058849L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Action() {
    }

    /**
     *
     * @param buttonText
     * @param link
     * @param action
     */
    public Action(String action, String link, String buttonText) {
        super();
        this.action = action;
        this.link = link;
        this.buttonText = buttonText;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(link).append(action).append(buttonText).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Action) == false) {
            return false;
        }
        Action rhs = ((Action) other);
        return new EqualsBuilder().append(link, rhs.link).append(action, rhs.action).append(buttonText, rhs.buttonText).isEquals();
    }

}