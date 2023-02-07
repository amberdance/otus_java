package ru.otus;


import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {
    private final NavigableMap<Customer, String> customers = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return copyEntry(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyEntry(customers.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        if (customer == null || data == null || data.isEmpty()) throw new IllegalArgumentException();

        customers.put(customer, data);
    }

    private Map.Entry<Customer, String> copyEntry(Map.Entry<Customer, String> entry) {
        if (entry == null) return null;

        Customer original = entry.getKey();

        return Map.entry(new Customer(original.getId(), original.getName(), original.getScores()), entry.getValue());
    }

}
