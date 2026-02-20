package com.kivora.JsWater.infrastructure.persistence.mapper.user;

import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.model.user.UserStatus;
import com.kivora.JsWater.infrastructure.persistence.entity.user.UserJpaEntity;

public class UserMapper {

    public static UserJpaEntity toJpa(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getRole(),
                user.getStatus()
        );
    }

    public static User toDomain(UserJpaEntity entity) {
        return User.restore(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole(),
                entity.isEnabled()
                        ? UserStatus.ACTIVE
                        : UserStatus.INACTIVE
        );
    }
}
