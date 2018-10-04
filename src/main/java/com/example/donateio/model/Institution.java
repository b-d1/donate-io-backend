package com.example.donateio.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "institutions")
public class Institution {


    @Id
    @GeneratedValue
    public Long id;

    public String name;
    @Column(unique=true, nullable = false)
    public String email;

    public String password;
    public String secret;
    public String hash;

    @Lob
    public String description;

    public String thumbnail;
    public String photo;

    @ElementCollection
    public List<String> addresses;

}
