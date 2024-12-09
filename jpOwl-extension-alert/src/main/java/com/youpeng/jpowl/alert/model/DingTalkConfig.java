package com.youpeng.jpowl.alert.model;

/**
 * 钉钉告警配置
 */
public class DingTalkConfig {
    private String webhook;
    private String secret;
    private String[] atMobiles;

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String[] getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(String[] atMobiles) {
        this.atMobiles = atMobiles;
    }
}
