package com.nineclock.common.entity;

import org.springframework.security.core.GrantedAuthority;

public class NcAuthority implements GrantedAuthority {
    
    @Override
    public String getAuthority() {
        return null;
    }
}