package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final ConcurrentSkipListSet<SensorData> dataBuffer = new ConcurrentSkipListSet<>(Comparator.comparing(SensorData::getMeasurementTime));
    private final ReentrantReadWriteLock dataBufferLock = new ReentrantReadWriteLock();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        int currentSize;

        dataBufferLock.writeLock().lock();

        dataBuffer.add(data);
        currentSize = dataBuffer.size();

        dataBufferLock.writeLock().unlock();

        if (currentSize >= bufferSize) {
            flush();
        }
    }

    public void flush() {
        dataBufferLock.writeLock().lock();
        try {
            if (!dataBuffer.isEmpty()) {
                writer.writeBufferedData(dataBuffer.stream().toList());
                dataBuffer.clear();
            }

        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        } finally {
            dataBufferLock.writeLock().unlock();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
