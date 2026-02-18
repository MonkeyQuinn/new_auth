package org.example.new_auth.model.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Role {

    private Long id;
    private String code;
    private String description;
    private Integer organization;
    private List<Permission> permissions;

    public Role(Long id, String code, String description, Integer organization, List<Permission> permissions) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.organization = organization;
        this.permissions = permissions == null ? new ArrayList<>() : new ArrayList<>(permissions);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrganization() {
        return organization;
    }

    public void setOrganization(Integer organization) {
        this.organization = organization;
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions == null ? new ArrayList<>() : new ArrayList<>(permissions);
    }

    public void addPermission(Permission permission) {
        if (permission == null) return;
        this.permissions.add(permission);
    }

    public void addPermissions(Collection<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) return;
        permissions.forEach(this::addPermission);
    }

}
