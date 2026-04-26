package com.example.tienda.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name="adress")
@Entity
@EntityListeners(AuditingEntityListener.class)

public class Address {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long adressid;

    @OneToMany(cascade= CascadeType.PERSIST)
    @JoinColumn(name = "customerid", referencedColumnName = "customerid", nullable = false, unique = true)
    private Customer customer;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private Boolean isDefault;

    public Long getAdressid() {
        return adressid;
    }

    public void setAdressid(Long adressid) {
        this.adressid = adressid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

}
