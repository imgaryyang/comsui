package com.suidifu.watchman.message.core.request;

public enum InvokeType {

    Sync,

    Async,;

    public static InvokeType getSync(boolean sync) {
        return sync ? Sync : Async;
    }


}
