package com.example.demo2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Column(columnDefinition = "boolean default false")
    private Boolean active;

    @Column(name = "created_date",nullable=true)
    @Temporal(TemporalType.DATE)
    private Date created_date = new Date();

    @Column(name = "adresse",nullable=true, length=255)
    private String adresse;

    @Column(name = "phone_number",nullable=true, length=25)
    private String phone_number;

    @Column(columnDefinition = "boolean default false")
    private Boolean banned;

    @Column(name = "actived_by",nullable=true, length=25)
    private String actived_by;

    @Column(name = "activation_date", nullable=true)
    @Temporal(TemporalType.DATE)
    private Date activation_date ;

    @Column(name = "reset_token",nullable=true, length=255)
    private String reset_token;


//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<Shop> shops ;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password, Boolean active, Date created_date, String adresse, String phone_number, Boolean banned, String actived_by, Date activation_date) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
        this.created_date = created_date;
        this.adresse = adresse;
        this.phone_number = phone_number;
        this.banned = banned;
        this.actived_by = actived_by;
        this.activation_date = activation_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Collection<Shop> getShops() {
        return shops;
    }

    public void setShops(Collection<Shop> shops) {
        this.shops = shops;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public String getActived_by() {
        return actived_by;
    }

    public void setActived_by(String actived_by) {
        this.actived_by = actived_by;
    }

    public Date getActivation_date() {
        return activation_date;
    }

    public void setActivation_date(Date activation_date) {
        this.activation_date = activation_date;
    }

    public String getReset_token() {
        return reset_token;
    }

    public void setReset_token(String reset_token) {
        this.reset_token = reset_token;
    }
}
