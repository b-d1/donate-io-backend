package com.example.donateio.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue
    public Long id;

    public String name;
    @Column(unique=true, nullable = false)
    public String email;

    public String password;
    public String secret;
    public String hash;

    @ElementCollection
    public List<String> addresses;

//    @OneToMany(mappedBy = "user")
//    public List<Donation> donations;


}
