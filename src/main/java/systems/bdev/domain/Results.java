package systems.bdev.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Results {
    private static final String LINE_BREAK = "\n";

    private final List<Row> rows = new ArrayList<>();

    public void addRow(int instance, double actual, double prediction) {
        rows.add(new Row(instance, actual, prediction));
    }

    public long getTotalCorrectPredictions() {
        return rows.stream().filter(Row::doesMatch).count();
    }

    public double getAccuracy() {
        return getTotalCorrectPredictions() / (double) rows.size() * 100;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Row row : rows) {
            sb.append(row.toString());
            sb.append(LINE_BREAK);
        }
        sb.append("Total Correct Predictions: ");
        sb.append(getTotalCorrectPredictions());
        sb.append(LINE_BREAK);
        sb.append("Accuracy: ");
        sb.append(getAccuracy());
        sb.append("%");
        return sb.toString();
    }

    @RequiredArgsConstructor
    public static class Row {
        private final int instance;
        private final double actual;
        private final double prediction;

        public boolean doesMatch() {
            return Math.round(actual) == Math.round(prediction);
        }

        @Override
        public String toString() {
            return String.format("Instance %d: Actual = %.2f, Predicted = %.2f, Match = %b", instance, actual, prediction, doesMatch());
        }
    }
}
