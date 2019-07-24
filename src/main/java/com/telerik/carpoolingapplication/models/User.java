package com.telerik.carpoolingapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.carpoolingapplication.models.enums.Role;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters.")
    private String username;

    @NotNull
    @Size(min = 3, max = 25, message = "First name should be between 3 and 25 characters.")
    private String firstName;

    @NotNull
    @Size(min = 3, max = 25, message = "Last name should be between 3 and 25 characters.")
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 4, max = 25, message = "Phone number should be between 4 and 25 characters.")
    private String phone;

    @NotNull
    @ColumnDefault("0")
    private double ratingAsDriver;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "users_ratings_as_driver",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "rating_id")}
    )
    private List<Rating> ratingsAsDriver;

    @NotNull
    @ColumnDefault("0")
    private double ratingAsPassenger;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "users_ratings_as_passenger",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "rating_id")}
    )
    private List<Rating> ratingsAsPassenger;

    private String avatarUri;

    @NotNull
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @NotNull
    @JsonIgnore
    @Column(name = "enabled")
    private boolean enabled = true;

    public User() {
    }

    public User(String username,
                String firstName,
                String lastName,
                String email,
                String phone,
                String password,
                double ratingAsDriver,
                double ratingAsPassenger,
                String avatarUri) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.ratingAsDriver = ratingAsDriver;
        this.ratingsAsDriver = new ArrayList<>();
        this.ratingAsPassenger = ratingAsPassenger;
        this.ratingsAsPassenger = new ArrayList<>();
        this.avatarUri = avatarUri;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> userRoles) {
        this.roles = userRoles;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRatingAsDriver() {
        return ratingAsDriver;
    }

    public void setRatingAsDriver(double ratingAsDriver) {
        this.ratingAsDriver = ratingAsDriver;
    }

    public List<Rating> getRatingsAsDriver() {
        return ratingsAsDriver;
    }

    public void setRatingsAsDriver(List<Rating> ratingsAsDriver) {
        this.ratingsAsDriver = ratingsAsDriver;
    }

    public double getRatingAsPassenger() {
        return ratingAsPassenger;
    }

    public void setRatingAsPassenger(double ratingAsPassenger) {
        this.ratingAsPassenger = ratingAsPassenger;
    }

    public List<Rating> getRatingsAsPassenger() {
        return ratingsAsPassenger;
    }

    public void setRatingsAsPassenger(List<Rating> ratingsAsPassenger) {
        this.ratingsAsPassenger = ratingsAsPassenger;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
