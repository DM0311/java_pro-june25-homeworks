package ru.otus.java.pro.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phone")
@Getter
@Setter
@NoArgsConstructor
public class Phone {

    @Id
    @SequenceGenerator(name = "phone_SEQ", sequenceName = "phone_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_SEQ")
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;
    // ?
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Phone(String number) {
        this.number = number;
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
