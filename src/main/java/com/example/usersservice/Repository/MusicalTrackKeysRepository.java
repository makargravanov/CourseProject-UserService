package com.example.usersservice.Repository;

import com.example.usersservice.Entity.MusicalTrackKeysEntity;
import com.example.usersservice.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MusicalTrackKeysRepository extends JpaRepository<MusicalTrackKeysEntity, Long> {
    public Optional<ArrayList<MusicalTrackKeysEntity>> findAllByUsers(Set<UserEntity> user);
    public Optional<MusicalTrackKeysEntity> findByMusicalTrackKey(Long trackKey);
}
