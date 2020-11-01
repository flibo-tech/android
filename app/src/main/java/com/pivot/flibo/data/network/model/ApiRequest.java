package com.pivot.flibo.data.network.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ApiRequest implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("provider_id")
    @Expose
    private String providerId;
    @SerializedName("token")
    @Expose
    private ApiToken token;
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
    private final static long serialVersionUID = 529448769856038592L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ApiRequest() {
    }

    /**
     *
     * @param provider
     * @param providerId
     * @param name
     * @param picture
     * @param email
     * @param token
     */
    public ApiRequest(String name, String picture, String email, String provider, String providerId, ApiToken token, String firebaseToken) {
        super();
        this.name = name;
        this.picture = picture;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.token = token;
        this.firebaseToken = firebaseToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public ApiToken getToken() {
        return token;
    }

    public void setToken(ApiToken token) {
        this.token = token;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(provider).append(providerId).append(name).append(picture).append(email).append(token).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ApiRequest) == false) {
            return false;
        }
        ApiRequest rhs = ((ApiRequest) other);
        return new EqualsBuilder().append(provider, rhs.provider).append(providerId, rhs.providerId).append(name, rhs.name).append(picture, rhs.picture).append(email, rhs.email).append(token, rhs.token).isEquals();
    }

}