package com.example.usersservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "track_keys")
public class MusicalTrackKeysEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long musicalTrackKey;

    @ManyToMany(mappedBy = "tracks")
    private Set<UserEntity> users = new HashSet<>();
}
