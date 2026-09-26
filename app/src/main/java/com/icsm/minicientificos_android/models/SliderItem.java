package com.icsm.minicientificos_android.models;

public class SliderItem {
    private final String title;
    private final String subtitle;
    private final String badge;
    private final String buttonText;
    private final int iconResId;

    public SliderItem(String title, String subtitle, String badge, String buttonText, int iconResId) {
        this.title = title;
        this.subtitle = subtitle;
        this.badge = badge;
        this.buttonText = buttonText;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBadge() {
        return badge;
    }

    public String getButtonText() {
        return buttonText;
    }

    public int getIconResId() {
        return iconResId;
    }
}