package ru.otus.java.pro.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("client")
public class Client  {

    @Id
    private Long id;

    private String name;

    private Long address_id;

    @Transient
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;
}
