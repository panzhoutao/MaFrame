package com.cydroid.coreframe.model;

/**
 * Created by yaocui on 15/7/1.
 */
public interface Model<T> {
    void excuteParams(String[] params);
    void close(boolean mayInterrupt);
    void excuteParams(T params);
}
