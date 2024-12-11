package com.event.management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Author: atquil
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name="Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(nullable = false, name = "email", unique = true)
    private String emailId;

    @Column(name = "mobilenumber")
    private String mobileNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(nullable = false, name = "roles")
    private String roles;

    @Column(nullable = false, name = "password")
    private String password;

    // Many-to-One relationship with RefreshTokenEntity
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // This is the owning side of the relationship
    private List<RefreshTokenEntity> refreshTokens;
}
