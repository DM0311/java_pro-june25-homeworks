package ru.otus.java.pro.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {

    private String userName;

    private String userAddress;

    private List<String> userPhones;
}
