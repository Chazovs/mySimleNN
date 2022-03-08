import java.util.Arrays;
import java.util.function.UnaryOperator;

public class NeuralNetwork {
    /**
     * желаемая тоность
     */
    private double learningRate;

    /**
     * функция активации
     */
    private final UnaryOperator<Double> activation;

    private float[] weights;
    private float[] result;

    public NeuralNetwork(double learningRate) {
        this.learningRate = learningRate;
        this.activation = Config.sigmoid;

        initWeights();
    }

    //заполняем веса случайными значениями
    private void initWeights() {
        this.weights = new float[Config.getSynapseCount()];

        for (int i = 0; i < this.weights.length; i++) {
            this.weights[i] = (float) Math.random();
        }
    }

        /**
         * Прямой прогон
         * На вход получает векторизированную картинку. На выходе выдает вектор вероятностей
         */
    public void feedForward(ImageEntity imageEntity, BatchResult batchResult) throws Exception {
        int currentLayer = 0; //счетчик слоев
        int startNeuronId = 0; //id текущего нейрона в массиве нейронов
        int endNeuronId =  Config.layersConfig[0]; //id конечного первого нейрона в массиве нейронов следующего слоя
        int neuronWeightsId = 0; //порядковый номер связи у текущего нейрона
        int idLastNeuronInThisLayer = Config.layersConfig[0] - 1; // id последнего нейрона в текущем слое
        double[] neurons = createNeurons(imageEntity.imageVector);

        //перебираем все веса
        for (float weight : this.weights) {
            Double signal = activation.apply(neurons[startNeuronId]);
            neurons[endNeuronId] += weight * signal;
            neuronWeightsId++;

            //если мы уже прошли все нейроны в следующем слое, то в текущем слое  переключаемся на следующий нейрон
            if (neuronWeightsId > Config.layersConfig[currentLayer + 1] - 1) {
                startNeuronId++;
                neuronWeightsId = 0;
                endNeuronId = endNeuronId - Config.layersConfig[currentLayer+1]+1;//откатываемся на первый нейрон в конечном слое
            } else {
                endNeuronId++;
            }

            if (startNeuronId > idLastNeuronInThisLayer) {
                currentLayer++;
                idLastNeuronInThisLayer += Config.layersConfig[currentLayer];
                endNeuronId = endNeuronId + Config.layersConfig[currentLayer];
            }
        }

        double[] lastLayerNeurons = Arrays.copyOfRange(
                neurons,
                neurons.length - Config.layersConfig[Config.layersConfig.length - 1],
                neurons.length
        );

        setForwardResult(lastLayerNeurons, batchResult, imageEntity.value);
    }

    private void setForwardResult(
            double[] lastLayerNeurons,
            BatchResult batchResult,
            int expectedValueId
    ) {
        this.result = new float[lastLayerNeurons.length];

         //это значение выходящее за пределы допустимых выходных значений на единицу
        //символизирует ответ "не знаю"
        int maxValueId = -1;
        float maxValue = -1;

        for (int i = 0; i < lastLayerNeurons.length; i++) {
            result[i] = activation.apply(lastLayerNeurons[i]).floatValue();

            if (i == 0) {
                maxValueId = i;
                maxValue = Math.abs(result[i]);
            }

            if (maxValue < result[i]) {
                maxValueId = i;
                maxValue = Math.abs(result[i]);
            }

            //например expectedValueId = 5, значит в 5ом элементе мы ожидали 1, а в остальных 0
            float expectedValue = i == expectedValueId ? 1 : 0;
            batchResult.errorsValue += Math.abs(expectedValue - result[i]);
        }

        if (maxValueId == expectedValueId){
            batchResult.rightCount++;
        }
    }

    private double[] createNeurons(double[] imageVector) {
        double[] neurons = new double[Config.getNeuronsCount()];

        System.arraycopy(imageVector, 0, neurons, 0, imageVector.length);

        return neurons;
    }

    /**
     * обратное распространение ошибки
     */
    public void backpropagation(double[] targets) {

    }
}
