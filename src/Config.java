import java.util.function.UnaryOperator;

public class Config {
    public  final static UnaryOperator<Double> sigmoid =  x -> 1 / (1 + Math.exp(-x));
    public  final static UnaryOperator<Double> dsigmoid = y -> y * (1 - y);

    /**
     * Количество эпох
     */
    public  final static int epochs = 1000;

    /**
     * Количество наблюдений в батче
     */
    public  final static int batchSize = 100;

    /**
     *  гиперболический тангенс
     */
    public  final static UnaryOperator<Double> tanh = Math::tanh;
    public  final static UnaryOperator<Double> relu = x -> x > 0 ? x : 0;

    /**
     *  возвращает случайный Y на гиперболическом тангенсе
     */
    public static double getRandomTanh() {
        return Math.random() * 2.0 - 1.0;
    }

    /**
     * нейронов в ыходном слое
     */
    public static final int exitLayerSize = 10;

    /**
     * параметры нейронки: слои и количество нейронов
     */
    public static final int[] hiddenLayers = new int[]{5, 5};

    public static final int[] networkConfig = new int[]{128, 5, 5, 10};

    public static int getSynapseCount() {
        int count = 0;

        for (int i = 0; i < networkConfig.length - 1; i++) {
            count += networkConfig[i] * networkConfig[i+1];
        }

        return count;
    }

    public static int getNeuronsCount() {
        int count = 0;

        for (int j : networkConfig) {
            count += j;
        }

        return count;
    }
}
