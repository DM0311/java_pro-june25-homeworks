package ru.otus.java.pro.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.pro.model.Address;
import ru.otus.java.pro.model.Client;
import ru.otus.java.pro.model.Phone;
import ru.otus.java.pro.repository.AddressRepository;
import ru.otus.java.pro.repository.ClientRepository;
import ru.otus.java.pro.repository.PhoneRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final AddressRepository addressRepository;

    private final PhoneRepository phoneRepository;

    @Autowired
    public ClientService(
            ClientRepository clientRepository, AddressRepository addressRepository, PhoneRepository phoneRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
    }

    public Optional<Client> getClient(long id) {
        Optional<Client> client = clientRepository.findById(id);
        client.ifPresent(this::joinAddress);
        return client;
    }

    public Iterable<Client> findAll() {
        Iterable<Client> clients = clientRepository.findAll();
        clients.forEach(this::joinAddress);
        return clients;
    }

    @Transactional
    public Client createClient(Client client, String street, List<String> phoneNums) {
        Address address = null;
        if (client.getAddress_id() != null) {
            address = addressRepository.findById(client.getAddress_id()).orElse(null);
        }
        if (address == null) address = new Address(null, street);
        else address.setStreet(street);
        address = addressRepository.save(address);
        client.setAddress_id(address.getId());

        client = clientRepository.save(client);

        Set<Phone> phones = new HashSet<>();
        for (String phoneNumber : phoneNums) {
            if (phoneNumber != null && !phoneNumber.isBlank()) {
                phones.add(new Phone(null, phoneNumber.trim(), client.getId()));
            }
        }
        phoneRepository.saveAll(phones);
        client.setPhones(phones);
        client.setAddress(address);
        return client;
    }

    private void joinAddress(Client client) {
        if (client.getAddress_id() != null) {
            addressRepository.findById(client.getAddress_id()).ifPresent(client::setAddress);
        }
    }
}
