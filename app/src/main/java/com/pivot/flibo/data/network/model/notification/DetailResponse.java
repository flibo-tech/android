package com.pivot.flibo.data.network.model.notification;


import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DetailResponse implements Serializable
{

    @SerializedName("data")
    @Expose
    private Object data;
    private final static long serialVersionUID = 7951326103807952814L;

    /**
     * No args constructor for use in serialization
     *
     */
    public DetailResponse() {
    }

    /**
     *
     * @param data
     */
    public DetailResponse(Object data) {
        super();
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DetailResponse) == false) {
            return false;
        }
        DetailResponse rhs = ((DetailResponse) other);
        return new EqualsBuilder().append(data, rhs.data).isEquals();
    }

}