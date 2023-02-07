package ru.otus.test;

public interface Initializer {

    void init();

    enum ErrorMessage {
        NO_METHODS_DECLARED("No methods declared"),
        NO_ANNOTATIONS_DECLARED("There is no methods to testing. Methods should be annotated with @Before, @After," +
                "@Test"),
        NO_TESTS_DECLARED("No methods with @Test annotation exists"),
        STATIC_METHOD_DECLARED("There are methods that cannot be a static"),
        BEFORE_MUST_BE_DECLARED_ONCE("Only the one method with @Before should be declared"),
        AFTER_MUST_BE_DECLARED_ONCE("Only the one method with @After should be declared");

        private final String value;

        ErrorMessage(String s) {
            value = s;
        }

        public String value() {
            return value;
        }
    }
}
