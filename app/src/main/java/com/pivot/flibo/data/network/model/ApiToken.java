package com.pivot.flibo.data.network.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ApiToken implements Serializable
{

    @SerializedName("access_token")
    @Expose
    private String token;
    private final static long serialVersionUID = -749491618961973844L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ApiToken() {
    }

    /**
     *
     * @param token
     */
    public ApiToken(String token) {
        super();
        this.token = token;
    }

    public String getAccess() {
        return token;
    }

    public void setAccess(String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(token).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ApiToken) == false) {
            return false;
        }
        ApiToken rhs = ((ApiToken) other);
        return new EqualsBuilder().append(token, rhs.token).isEquals();
    }

}
