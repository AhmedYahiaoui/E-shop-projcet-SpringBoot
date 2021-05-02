package com.example.demo2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name",nullable=true, length=255)
    private String name;

    @Column(name = "size",nullable=true, length=255)
    private String size;

    @Column(name = "created_date",nullable=true)
    @Temporal(TemporalType.DATE)
    private Date created_date = new Date();

    @ManyToOne
    @JsonBackReference
    private Shop shop ;


    public Images() {
    }

    public Images(String name, String size, Date created_date, Shop shop) {
        this.name = name;
        this.size = size;
        this.created_date = created_date;
        this.shop = shop;
    }

    public Images(String name, String size, Date created_date) {
        this.name = name;
        this.size = size;
        this.created_date = created_date;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
