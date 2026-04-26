package com.example.tienda.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerid;

    // Cada customer pertenece a un unico user (1:1)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false, unique = true)
    private User user;

    // Un customer puede tener varias direcciones (1:N)
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    private String phone;
    private Boolean status = true;
    @CreatedDate
    private LocalDateTime dataCreated;
    @LastModifiedDate
    private LocalDateTime dataUpdated;

    public Customer() {}

    public Long getId() {
        return customerid;
    }

    public void setId(Long id) {
        this.customerid = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(LocalDateTime dataCreated) {
        this.dataCreated = dataCreated;
    }

    public LocalDateTime getDataUpdated() {
        return dataUpdated;
    }

    public void setDataUpdated(LocalDateTime dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    // Placeholder for Integrant 2 (orders):
    // @OneToMany(mappedBy = "customer")
    // private List<Order> orders;

    // Helper para añadir direccion y dejar bien hecha la relacion en ambos lados.
    public void addAddress(Address address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    // Helper para borrar todas las direcciones sin borrar el customer.
    public void clearAddresses() {
        for (Address address : addresses) {
            address.setCustomer(null);
        }
        addresses.clear();
    }

}
