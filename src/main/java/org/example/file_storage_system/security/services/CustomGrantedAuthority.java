package org.example.file_storage_system.security.services;

import org.example.file_storage_system.Models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {

    public String role;

    public CustomGrantedAuthority() {
    }

    public CustomGrantedAuthority(Role role) {
        this.role = role.getName();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
