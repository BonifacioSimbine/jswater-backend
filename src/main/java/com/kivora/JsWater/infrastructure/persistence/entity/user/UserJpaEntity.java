package com.kivora.JsWater.infrastructure.persistence.entity.user;

import com.kivora.JsWater.domain.model.user.UserRole;
import com.kivora.JsWater.domain.model.user.UserStatus;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserJpaEntity implements UserDetails {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    protected UserJpaEntity() {}

    public UserJpaEntity(UUID id, String name, String password, UserRole role, UserStatus status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.userStatus = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return name; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return userStatus == UserStatus.ACTIVE; }

    public UUID getId() { return id; }
    public UserRole getRole() { return role; }
}
