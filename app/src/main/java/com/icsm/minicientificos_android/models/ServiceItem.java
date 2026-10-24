package com.icsm.minicientificos_android.models;

public class ServiceItem {
    private final String title;
    private final String description;
    private final String badge;
    private final int iconResId;

    public ServiceItem(String title, String description, String badge, int iconResId) {
        this.title = title;
        this.description = description;
        this.badge = badge;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBadge() {
        return badge;
    }

    public int getIconResId() {
        return iconResId;
    }
}