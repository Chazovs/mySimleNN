import java.util.function.UnaryOperator;

public class NeuralNetwork {
    /**
     * желаемая тоность
     */
    private double learningRate;

    /**
     * функция активации
     */
    private UnaryOperator<Double> activation;

    /**
     * Слои с нейронами
     */
    private final Layer[] layers;

    private float[] weights;

    public NeuralNetwork(double learningRate) {
        this.learningRate = learningRate;
        this.activation = Config.sigmoid;

        this.layers = new Layer[Config.hiddenLayers.length + 2]; // 2 дополнительных слоя - это первый и последний слои

        initLayers();
    }

    private void initLayers2() {
        int synapseCount = Config.getSynapseCount();

        this.weights = new float[Config.getSynapseCount()];

        for (int i = 0; i < Config.getSynapseCount(); i++) {
            this.weights[i] = (float) Math.random();
        }
    }


    private void initLayers() {
        int currentLayerIndex = 0;

        //Создаем входной слой
        this.layers[currentLayerIndex] = new Layer(ImageService.pixelCount, Config.hiddenLayers[0]);

        //создаем скрытые слои
        for (int hiddenLayer : Config.hiddenLayers) {
            ++currentLayerIndex;

            //если это последний скрытый слой
            if (currentLayerIndex == Config.hiddenLayers.length) {
                this.layers[currentLayerIndex] = new Layer(hiddenLayer, Config.exitLayerSize);

                break;
            }

            this.layers[currentLayerIndex] = new Layer(
                    hiddenLayer,
                    Config.hiddenLayers[currentLayerIndex]
            );
        }

        //Создаем выходной слой
        this.layers[++currentLayerIndex] = new Layer(Config.exitLayerSize, 0);
    }

    /**
     * Прямой прогон
     * На вход получает векторизированную картинку. На выходе выдает вектор вероятностей
     */
    public float[] feedForward(ImageEntity imageEntity, BatchResult batchResult) throws Exception {
        int currentLayer = 0; //счетчик слоев
        int startNeuronId = 0; //id текущего нейрона в массиве нейронов
        int endNeuronId = Config.networkConfig[0]; //id конечного нейрона в массиве нейронов
        int neuronWeightsId = 0; //порядковый номер связи у текущего нейрона
        int idLastNeuronInNextLayer = Config.networkConfig[0]; // id последнего нейрона в следующем слое

        double[] neurons = createNeurons(imageEntity.imageVector);

        //перебираем все веса
        for (float weight : this.weights) {
            Double signal = activation.apply(neurons[startNeuronId]);
            neurons[endNeuronId] += weight * signal;
            neuronWeightsId++;

            //если мы уже прошли все нейроны в следующем слое. то в текущем слое  переключаемся на  следующий нейрон
            if (neuronWeightsId > Config.networkConfig[currentLayer + 1]) {
                startNeuronId++;
                neuronWeightsId = 0;
                endNeuronId -= Config.networkConfig[currentLayer];//откатываемся на первый нейрон в конечном слое
            } else {
                endNeuronId++;
            }

            //если пройдены все нейроны в ряду, то переключаемся на следующий ряд
            if (startNeuronId > idLastNeuronInNextLayer - 1) {
                currentLayer++;

                //если следующего слоя нет, то подсчитываем выходные значения
                if (currentLayer > Config.networkConfig.length) {
                    return getForwardResult(currentLayer, endNeuronId, neurons, batchResult);
                }

                idLastNeuronInNextLayer += Config.networkConfig[currentLayer];
            }
        }

        throw new Exception();
    }

    private float[] getForwardResult(int currentLayer, int endNeuronId, double[] neurons, BatchResult batchResult) {
        float[] result = new float[Config.networkConfig[currentLayer - 1]];

        for (int i = endNeuronId; i < Config.networkConfig[currentLayer - 1]; i++) {
            result[i] = activation.apply(neurons[endNeuronId]).floatValue();
        }

        return result;
    }

    private double[] createNeurons(double[] imageVector) {
        int neuronsCount = Config.getNeuronsCount();
        double[] neurons = new double[neuronsCount];

        System.arraycopy(imageVector, 0, neurons, 0, imageVector.length);

        return neurons;
    }

    /**
     * обратное распространение ошибки
     */
    public void backpropagation(double[] targets) {

    }
}
