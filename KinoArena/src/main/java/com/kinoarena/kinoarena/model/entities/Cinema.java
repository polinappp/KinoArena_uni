package com.kinoarena.kinoarena.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cinemas")
@Builder
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String contacts;
    @Column
    private String workHours;
    @Column
    private int hallNumber;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    @ManyToMany
    @JoinTable(name = "cinemas_has_medias",
            joinColumns = @JoinColumn(name = "cinema_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id"))
    private Set<Media> medias = new HashSet<>();
}
