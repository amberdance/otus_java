import java.util.ArrayList;
import java.util.List;

public class Summator {
    private final List<Data> listValues = new ArrayList<>();
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues.add(data);
        if (listValues.size() % 6_600_000 == 0) {
            listValues.clear();
        }

        int dataValue = data.getValue();
        int valuesSize = listValues.size();

        sum += dataValue;
        sumLastThreeValues = dataValue + prevValue + prevPrevValue;
        prevPrevValue = prevValue;
        prevValue = dataValue;

        for (int idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (dataValue + 1) - sum);
            someValue = Math.abs(someValue) + valuesSize;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}
