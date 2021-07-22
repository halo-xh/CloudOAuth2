package com.xh.oauth.security.authenticate;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/22 10:47
 * @description
 */
public class Oauth2Request {

    private Long requestId;

    private String clientId;

    private String responseType;

    private String redirectUri;

    private String state;

    private String scope;

    public Oauth2Request() {
    }

    public Oauth2Request(String clientId, String responseType, String redirectUri, String state, String scope) {
        this.clientId = clientId;
        this.responseType = responseType;
        this.redirectUri = redirectUri;
        this.state = state;
        this.scope = scope;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Oauth2Request{" +
                "requestId=" + requestId +
                ", clientId='" + clientId + '\'' +
                ", responseType='" + responseType + '\'' +
                ", redirectUri='" + redirectUri + '\'' +
                ", state='" + state + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
