package com.qimus.react;

import java.util.Map;

public class NotificationDto {
    private String targetRoute;
    private Map<String, Object> routeParams;
    private String title;
    private String body;
    private int channelId;
    private String smallIcon = "ic_launcher";
    private String largeIcon = "";
    private String priority = RNNotificationManager.PRIORITY_DEFAULT;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getChannelId() {
        if (channelId == 0) {
            return 1;
        }

        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTargetRoute() {
        return targetRoute;
    }

    public void setTargetRoute(String targetRoute) {
        this.targetRoute = targetRoute;
    }

    public Map<String, Object> getRouteParams() {
        return routeParams;
    }

    public void setRouteParams(Map<String, Object> routeParams) {
        this.routeParams = routeParams;
    }
}
