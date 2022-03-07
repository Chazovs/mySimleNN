import java.io.IOException;

public class Researcher {
    private final ImageService imageService;
    private final NeuralNetwork neuralNetwork;

    public Researcher(ImageService imageService, NeuralNetwork neuralNetwork) {
        this.imageService = imageService;
        this.neuralNetwork = neuralNetwork;
    }

    public void startResearch() {
        learnNN();
        /*testNN();*/
    }

    private void learnNN() {
        BatchResult batchResult = new BatchResult();

        for (int i = 1; i < Config.epochs; i++) {
           runBatch(batchResult);
           System.out.println("epoch: " + i + ". correct: " + batchResult.rightCount + ". error: " + batchResult.errorsCount);
           imageService.reshuffleImages();
           batchResult.reset();
        }
    }

    private void runBatch(BatchResult batchResul) {
        for (int i = 0; i < imageService.eduLastImageIndex; i++) {
            try {
                ImageEntity imageEntity = imageService.getImageEntity(i);
                //Ожидаемый результат
                double[] expect = getTargetResult(imageEntity.value);

                //Реальный результат
                float[] received = this.neuralNetwork.feedForward(imageEntity, batchResul);

                // если предсказание верно, то меняем счетчик

                if (isPredictionCorrect(imageEntity.value, received)){
                    right++;
                };

                totalErrorSum += getError(received, imageEntity.value);

                neuralNetwork.backpropagation(expect);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private double getError(float[] received, int value) {
        double result = 0;

        for (int k = 0; k < Config.exitLayerSize; k++) {
            int output = k == value ? 1 : 0;
            //TODO непонятно почему квадрат разницы
            result += Math.pow(received[k] - output, 2); //вычисляем квадрат разности ошибок
        }

        return result;
    }

    /**
     * Выясняет, верно ли предсказание
     */
    private boolean isPredictionCorrect(int expectResult, float[] received) {
        double maxDigitWeight = -1;
        int maxDigit = 11; //11 будет ответом "Не знаю"

        for (int k = 0; k < 10; k++) {
            if (received[k] > maxDigitWeight) {
                maxDigitWeight = received[k];
                maxDigit = k;
            }
        }

        return maxDigit == expectResult;
    }

    /**
     * Возвращает ожидаемую картину
     */
    private double[] getTargetResult(int realValue) {
        double[] targets = new double[10];
        targets[realValue] = 1;

        return targets;
    }

    private static void testNN() {

    }
}
