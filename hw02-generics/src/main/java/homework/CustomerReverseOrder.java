package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private Deque<Customer> stack = new ArrayDeque<>();

    public void add(Customer customer) {
        stack.addLast(customer);
    }

    public Customer take() {
        return stack.pollLast();
    }
}
