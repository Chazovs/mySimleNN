public class Layer {
    private final int nextSize;
    public int size;
    public double[] neurons;
    public double[] biases;
    public double[][] weights;

    public Layer(int size, int nextSize) {
        this.size = size;
        this.nextSize = nextSize;
        neurons = new double[size];
        biases = new double[size];
        weights = new double[size][nextSize];

        //если следующий слой есть, то создаем случайные веса для всех связей (сеть полносвязная)
        if (nextSize > 0) {
            setRandomWeights();
        }
    }

    /**
     * устанавливает случайные значения весов  связей для нейронов и байеса (смещения)
     */
    private void setRandomWeights() {
        //перебираем все нейроны в текущес слое
        for (int i = 0; i < this.size; i++) {

            biases[i]= Config.getRandomTanh();

            //перебираем все нейроны в следующем слое слое
            for (int j = 0; j < nextSize; j++) {
                weights[i][j] = Config.getRandomTanh();
            }
        }
    }
}
