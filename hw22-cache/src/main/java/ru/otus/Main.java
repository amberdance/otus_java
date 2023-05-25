package ru.otus;

import ru.otus.model.Avatar;
import ru.otus.model.Course;
import ru.otus.model.EMail;
import ru.otus.model.OtusStudent;
import ru.otus.utils.HibernateUtils;

public class Main {
    public static void main(String[] args) {
        var session = HibernateUtils.buildSessionFactory(OtusStudent.class, Avatar.class,
                EMail.class, Course.class);



    }
}
