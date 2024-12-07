package systems.bdev;

import lombok.extern.slf4j.Slf4j;
import systems.bdev.domain.Results;
import systems.bdev.tools.DataSourceLoader;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

@Slf4j
public class Main {
    private static final String RED_WINE_URL = "https://archive.ics.uci.edu/ml/machine-learning-databases/wine-quality/winequality-red.csv";
    private static final String WHITE_WINE_URL = "https://archive.ics.uci.edu/ml/machine-learning-databases/wine-quality/winequality-white.csv";

    private static final double TRAIN_SET_PROPORTION = 0.9;
    private static final int CLASS_INDEX = 11;
    private static final double LEARNING_RATE = 0.25;
    private static final double MOMENTUM = 0.2;
    private static final int TRAINING_TIME = 10000;
    private static final String HIDDEN_LAYERS_CONFIG = "5";

    public static void main(String[] args) {
        Instances redWineData = loadData(RED_WINE_URL);
        redWineData.setClassIndex(CLASS_INDEX);
        log.info("Results on red wine dataset:\n{}", runModel(redWineData));
        Instances whiteWineData = loadData(WHITE_WINE_URL);
        whiteWineData.setClassIndex(CLASS_INDEX);
        log.info("Results on white wine dataset:\n{}", runModel(whiteWineData));
    }

    private static Results runModel(Instances data) {
        try {
            Results results = new Results();

            int trainSize = (int) Math.round(data.numInstances() * TRAIN_SET_PROPORTION);
            int testSize = data.numInstances() - trainSize;
            Instances trainData = new Instances(data, 0, trainSize);
            Instances testData = new Instances(data, trainSize, testSize);

            MultilayerPerceptron mlp = new MultilayerPerceptron();
            mlp.setLearningRate(LEARNING_RATE);
            mlp.setMomentum(MOMENTUM);
            mlp.setTrainingTime(TRAINING_TIME);
            mlp.setHiddenLayers(HIDDEN_LAYERS_CONFIG);
            mlp.buildClassifier(trainData);

            for (int i = 0; i < testData.numInstances(); i++) {
                results.addRow(i, testData.instance(i).classValue(), mlp.classifyInstance(testData.instance(i)));
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Instances loadData(String url) {
        try {
            DataSourceLoader loader = new DataSourceLoader();
            return loader.loadData(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}