package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> res = new TreeMap<>();

        // Stream API is good, but not
        for (Measurement me : data) {
            var key = me.getName();
            var val = me.getValue();

            res.put(key, res.containsKey(key) ? res.get(key) + val : val);
        }

        return res;
    }
}
