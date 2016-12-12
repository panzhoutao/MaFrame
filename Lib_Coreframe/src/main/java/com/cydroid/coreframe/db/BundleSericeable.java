package com.cydroid.coreframe.db;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by yaocui on 15/7/11.
 */
public class BundleSericeable implements Serializable {
    public Set<? extends Serializable> data;

    public BundleSericeable(Set<? extends Serializable> data) {
        this.data = data;
    }

}
