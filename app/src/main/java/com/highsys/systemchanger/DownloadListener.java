package com.highsys.systemchanger;

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFaild();
    void onPaused();
    void onCanceled();
}
