package ru.otus;


import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {
    private final Deque<Customer> customers = new LinkedList<>();

    public void add(Customer customer) {
        if (customer == null) throw new IllegalArgumentException();

        customers.addFirst(customer);
    }

    public Customer take() {
        return customers.poll();
    }

}
