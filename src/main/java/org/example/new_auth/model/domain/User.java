package org.example.new_auth.model.domain;

import java.util.*;
import java.util.function.Function;

public class User {

    private Long id;
    private Integer clientId;
    private Byte status;
    private Boolean passChangeNeeded;
    private Boolean twoFactorEnabled;
    private Boolean inheritClient;
    private String firstname;
    private String lastname;
    private Integer refreshTokenTtl;
    private Integer accessTokenTtl;
    private Integer tokenCount;
    private List<String> usernames;
    private List<Permission> permissions;
    private List<Role> roles;

    public User(Long id, Integer clientId, Byte status,
                Boolean passChangeNeeded, Boolean twoFactorEnabled, Boolean inheritClient,
                String firstname, String lastname,
                Integer refreshTokenTtl, Integer accessTokenTtl, Integer tokenCount,
                List<String> usernames, List<Permission> permissions, List<Role> roles) {
        this.id = id;
        this.clientId = clientId;
        this.status = status;
        this.passChangeNeeded = passChangeNeeded;
        this.twoFactorEnabled = twoFactorEnabled;
        this.inheritClient = inheritClient;
        this.firstname = firstname;
        this.lastname = lastname;
        this.refreshTokenTtl = refreshTokenTtl;
        this.accessTokenTtl = accessTokenTtl;
        this.tokenCount = tokenCount;
        this.usernames = usernames == null ? new ArrayList<>() : new ArrayList<>(usernames);
        this.permissions = permissions == null ? new ArrayList<>() : new ArrayList<>(permissions);
        this.roles = roles == null ? new ArrayList<>() : new ArrayList<>(roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Boolean getPassChangeNeeded() {
        return passChangeNeeded;
    }

    public void setPassChangeNeeded(Boolean passChangeNeeded) {
        this.passChangeNeeded = passChangeNeeded;
    }

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public Boolean getInheritClient() {
        return inheritClient;
    }

    public void setInheritClient(Boolean inheritClient) {
        this.inheritClient = inheritClient;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getRefreshTokenTtl() {
        return refreshTokenTtl;
    }

    public void setRefreshTokenTtl(Integer refreshTokenTtl) {
        this.refreshTokenTtl = refreshTokenTtl;
    }

    public Integer getAccessTokenTtl() {
        return accessTokenTtl;
    }

    public void setAccessTokenTtl(Integer accessTokenTtl) {
        this.accessTokenTtl = accessTokenTtl;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }

    public List<String> getUsernames() {
        return Collections.unmodifiableList(usernames);
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames == null ? new ArrayList<>() : new ArrayList<>(usernames);
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions == null ? new ArrayList<>() : new ArrayList<>(permissions);
    }

    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles == null ? new ArrayList<>() : new ArrayList<>(roles);
    }

    public void grantPermission(Permission permission) {
        if (permission == null) return;
        this.permissions.add(permission);
    }

    public void grantPermissions(Collection<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) return;
        permissions.forEach(this::grantPermission);
    }

    public void addRole(Role role) {
        if (role == null) return;
        this.roles.add(role);
    }

    public void addRoles(Collection<Role> roles) {
        if (roles == null || roles.isEmpty()) return;
        roles.forEach(this::addRole);
    }

    public void revokePermissionsByAreas(Collection<String> areas) {
        removeFromPermissions(areas, Permission::area);
    }

    public void revokePermissionsByOperations(Collection<String> operations) {
        removeFromPermissions(operations, Permission::operation);
    }

    private void removeFromPermissions(Collection<String> values, Function<Permission, String> extractor) {
        if (values == null || values.isEmpty() || this.permissions.isEmpty()) return;
        Set<String> valuesSet = new HashSet<>(values);
        permissions.removeIf(permission -> Objects.nonNull(permission) && valuesSet.contains(extractor.apply(permission)));
    }

}
