public class ProxyDemo {

    public static void main(String[] args) {
        var log = new TestLoggingProxy().getProxy();

        log.calculation(1);
        log.calculation(1, 2);
        log.calculation(1, 2, 3);

        //почему не получается "развернуть" varargs ? ([I@4783da3f)
        log.calculation(1, 2, 3, 4, 5);
    }
}
