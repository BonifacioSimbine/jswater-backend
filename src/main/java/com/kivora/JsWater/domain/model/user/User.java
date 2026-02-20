package com.kivora.JsWater.domain.model.user;

import java.util.UUID;


public class User {

    private UUID id;
    private String name;
    private String password;
    private UserRole role;
    private UserStatus status;

    protected User() {}

    private User(UUID id, String name, String password, UserRole role, UserStatus status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public static User create(String name, String hashedPassword, UserRole role) {
        return new User(
                UUID.randomUUID(),
                name,
                hashedPassword,
                role,
                UserStatus.ACTIVE
        );
    }

    public static User restore(
            UUID id,
            String name,
            String password,
            UserRole role,
            UserStatus status
    ) {
        return new User(id, name, password, role, status);
    }

    public void changePassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public void changeStatus(UserStatus newStatus) {
        this.status = newStatus;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public UserStatus getStatus() { return status; }
}
