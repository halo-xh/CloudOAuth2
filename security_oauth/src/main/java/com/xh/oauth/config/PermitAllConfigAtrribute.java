package com.xh.oauth.config;

import org.springframework.security.access.ConfigAttribute;

/**
 * flag class.just means permit all ConfigAttribute
 * <p>
 * Created by Xiao Hong on 2020-12-10
 */
public class PermitAllConfigAtrribute implements ConfigAttribute {

    @Override
    public String getAttribute() {
        return null;
    }
}
