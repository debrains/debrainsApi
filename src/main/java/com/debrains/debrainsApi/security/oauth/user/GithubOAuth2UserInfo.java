package com.debrains.debrainsApi.security.oauth.user;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
    @Override
    public String getBlogUrl() {
        return (String) attributes.get("blog");
    }
    @Override
    public String getGithubUrl() {
        return (String) attributes.get("html_url");
    }
}
