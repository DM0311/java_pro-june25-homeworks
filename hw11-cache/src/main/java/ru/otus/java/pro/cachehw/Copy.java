package ru.otus.java.pro.cachehw;

import java.util.ArrayList;
import java.util.List;
import ru.otus.java.pro.crm.model.Address;
import ru.otus.java.pro.crm.model.Client;
import ru.otus.java.pro.crm.model.Phone;

public final class Copy {
    private Copy() {}

    public static Client copy(Client source) {
        if (source == null) {
            return null;
        }
        Client copyClient = new Client();
        copyClient.setId(source.getId());
        copyClient.setName(source.getName());
        if (source.getAddress() != null) {
            Address addr = new Address();
            addr.setId(source.getAddress().getId());
            addr.setStreet(addr.getStreet());
            copyClient.setAddress(addr);
        }
        if (source.getPhones() != null) {
            List<Phone> copyPhones = new ArrayList<>();
            for (Phone ph : source.getPhones()) {
                Phone copyPhone = new Phone();
                copyPhone.setId(ph.getId());
                copyPhone.setNumber(ph.getNumber());
                copyPhone.setClient(copyClient);
                copyPhones.add(copyPhone);
            }
            copyClient.setPhones(copyPhones);
        }

        return copyClient;
    }
}
