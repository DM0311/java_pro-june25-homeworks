package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    TreeMap<Customer, String> dataByCustomer = new TreeMap<>(Comparator.comparing(Customer::getScores));

    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {

        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> originalEntry = dataByCustomer.firstEntry();
        if (originalEntry == null) {
            return null;
        }
        return Map.entry(
                new Customer(
                        originalEntry.getKey().getId(),
                        originalEntry.getKey().getName(),
                        originalEntry.getKey().getScores()),
                originalEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> originalEntry = dataByCustomer.higherEntry(customer);

        if (originalEntry == null) {
            return null;
        }
        return Map.entry(
                new Customer(
                        originalEntry.getKey().getId(),
                        originalEntry.getKey().getName(),
                        originalEntry.getKey().getScores()),
                originalEntry.getValue());
    }

    public void add(Customer customer, String data) {
        dataByCustomer.put(customer, data);
    }
}
