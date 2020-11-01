package com.pivot.flibo.data.network.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ApiResponse implements Serializable
{

    @SerializedName("session_id")
    @Expose
    private String sessionId;
    private final static long serialVersionUID = -7042879241880863065L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ApiResponse() {
    }

    /**
     *
     * @param sessionId
     */
    public ApiResponse(String sessionId) {
        super();
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(sessionId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ApiResponse) == false) {
            return false;
        }
        ApiResponse rhs = ((ApiResponse) other);
        return new EqualsBuilder().append(sessionId, rhs.sessionId).isEquals();
    }

}
