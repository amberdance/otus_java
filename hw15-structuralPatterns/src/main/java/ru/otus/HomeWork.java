package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.homework.HandleField11And12Processor;
import ru.otus.processor.homework.ThrowingExceptionProcessor;

import java.util.List;
import java.util.stream.IntStream;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {
        var complexProcessor = new ComplexProcessor(List.of(new LoggerProcessor(new HandleField11And12Processor()),
                new ThrowingExceptionProcessor())
                , handler -> {
        });
        var historyListener = new HistoryListener();
        var listenerPrinter = new ListenerPrinterConsole();

        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var messages = IntStream.range(1, 4).mapToObj(HomeWork::generateMessage).toList();

        messages.forEach(complexProcessor::handle);

        complexProcessor.removeListener(historyListener);
        complexProcessor.removeListener(listenerPrinter);
    }

    private static Message generateMessage(long messageId) {
        var o4m = new ObjectForMessage();
        o4m.setData(IntStream.range(0, 10).mapToObj(String::valueOf).toList());

        return new Message.Builder(messageId)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(o4m)
                .build();
    }
}
