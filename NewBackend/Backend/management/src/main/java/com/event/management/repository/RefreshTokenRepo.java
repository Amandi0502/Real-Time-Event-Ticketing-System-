package com.event.management.repository;

import com.event.management.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
