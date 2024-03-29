package ru.otus.model;

import ru.otus.core.annotation.Id;

public class Manager {
    @Id
    private Long no;
    private String label;
    private String param1;

    public Manager() {
    }

    public Manager(String label) {
        this.label = label;
    }

    public Manager(Long no, String label, String param1) {
        this.no = no;
        this.label = label;
        this.param1 = param1;
    }

    public Long getNo() {
        return no;
    }

    public String getLabel() {
        return label;
    }

    public String getParam1() {
        return param1;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "no=" + no +
                ", label='" + label + '\'' +
                ", param1='" + param1 + '\'' +
                '}';
    }
}
