package com.example.demo2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name",nullable=true, length=255)
    private String name;

    @Column(name = "adresse",nullable=true, length=255)
    private String adresse;

    @Column(name = "created_date",nullable=true)
    @Temporal(TemporalType.DATE)
    private Date created_date = new Date();

    @Column(name = "number",nullable=true, length=255)
    private String number;

    @Column(name = "lng",nullable=true, length=255)
    private Float lng;

    @Column(name = "lat",nullable=true, length=255)
    private Float lat;

    @Column(name = "country",nullable=true, length=255)
    private String country;

    @Column(name = "departement",nullable=true, length=255)
    private String departement;

    @Column(name = "city",nullable=true, length=255)
    private String city;

    @Column(name = "chaine",nullable=true, length=255)
    private String chaine;

    @Column(name = "nomMainImage",nullable=true, length=255)
    private String nomMainImage;

    @OneToMany(mappedBy="shop", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<Images> images ;

    @ManyToOne
    @JsonBackReference
    private User user ;


    public Shop() {
    }

    public Shop (String name, String adresse, String number, Float lng, Float lat, String country, String departement, String city, String chaine, String nomMainImage) {
        this.name = name;
        this.adresse = adresse;
        this.number = number;
        this.lng = lng;
        this.lat = lat;
        this.country = country;
        this.departement = departement;
        this.city = city;
        this.chaine = chaine;
        this.nomMainImage = nomMainImage;
    }

    public Shop(String name, String adresse, String number, Float lng, Float lat, String country, String departement, String city, String chaine, String nomMainImage, User user) {
        this.name = name;
        this.adresse = adresse;
        this.number = number;
        this.lng = lng;
        this.lat = lat;
        this.country = country;
        this.departement = departement;
        this.city = city;
        this.chaine = chaine;
        this.nomMainImage = nomMainImage;
        this.user = user;
    }

    public Shop(String name, String adresse, Date created_date, String number, Float lng, Float lat, String country, String departement, String city, String chaine, String nomMainImage, Collection<Images> images) {
        this.name = name;
        this.adresse = adresse;
        this.created_date = created_date;
        this.number = number;
        this.lng = lng;
        this.lat = lat;
        this.country = country;
        this.departement = departement;
        this.city = city;
        this.chaine = chaine;
        this.nomMainImage = nomMainImage;
        this.images = images;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getChaine() {
        return chaine;
    }

    public void setChaine(String chaine) {
        this.chaine = chaine;
    }

    public String getNomMainImage() {
        return nomMainImage;
    }

    public void setNomMainImage(String nomMainImage) {
        this.nomMainImage = nomMainImage;
    }

    public Collection<Images> getImages() {
        return images;
    }

    public void setImages(Collection<Images> images) {
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adresse='" + adresse + '\'' +
                ", created_date='" + created_date + '\'' +
                ", number='" + number + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", country='" + country + '\'' +
                ", departement='" + departement + '\'' +
                ", city='" + city + '\'' +
                ", chaine='" + chaine + '\'' +
                ", nomMainImage='" + nomMainImage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shop shop = (Shop) o;
        return id == shop.id &&
                Objects.equals(name, shop.name) &&
                Objects.equals(adresse, shop.adresse) &&
                Objects.equals(created_date, shop.created_date) &&
                Objects.equals(number, shop.number) &&
                Objects.equals(lng, shop.lng) &&
                Objects.equals(lat, shop.lat) &&
                Objects.equals(country, shop.country) &&
                Objects.equals(departement, shop.departement) &&
                Objects.equals(city, shop.city) &&
                Objects.equals(chaine, shop.chaine) &&
                Objects.equals(nomMainImage, shop.nomMainImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, adresse, created_date, number, lng, lat, country, departement, city, chaine, nomMainImage);
    }
}
