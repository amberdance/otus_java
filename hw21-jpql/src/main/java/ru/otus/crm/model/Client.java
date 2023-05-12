package ru.otus.crm.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    @JoinColumn(name = "street_id")
    @Fetch(value = FetchMode.JOIN)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Phone> phones = new ArrayList<>();

    public Client() {
    }

    public Client(String name) {
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;

        if (address != null) {
            this.address = new Address(address.getId(), address.getStreet(), this);
        } else {
            this.address = null;
        }

        this.phones = phones.stream().map(p -> new Phone(p.getId(), p.getNumber(), this)).toList();
    }


    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.address, this.phones);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
