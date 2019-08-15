package ru.mdashlw.rankedwho.base;

public class VersionInfo {
    private String tag;
    private String downloadUrl;

    public VersionInfo(String tag, String downloadUrl) {
        this.tag = tag;
        this.downloadUrl = downloadUrl;
    }

    public String getTag() {
        return tag;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
