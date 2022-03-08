public class Researcher {
    private final ImageService imageService;
    private final NeuralNetwork neuralNetwork;
    private BatchResult batchResult;

    public Researcher(ImageService imageService, NeuralNetwork neuralNetwork) {
        this.imageService = imageService;
        this.neuralNetwork = neuralNetwork;
    }

    public void startResearch() throws Exception {
        learnNN();
        /*testNN();*/
    }

    private void learnNN() throws Exception {
        this.batchResult = new BatchResult();

        for (int i = 1; i < Config.epochs; i++) {
            runBatch();
            System.out.println("epoch: " + i + ". correct: " + this.batchResult.rightCount + ". error: " + this.batchResult.errorsValue);
            imageService.reshuffleImages();
            this.batchResult.reset();
        }
    }

    private void runBatch() throws Exception {
        for (int i = 0; i < imageService.eduLastImageIndex; i++) {
            /*try {*/
                ImageEntity imageEntity = imageService.getImageEntity(i);
                //прямой прогон
                this.neuralNetwork.feedForward(imageEntity, this.batchResult);

                //обратный прогон
                /* neuralNetwork.backpropagation(getTargetResult(imageEntity.value));*/
            /*} catch (Exception e) {
                System.out.println("runBatch: " + e.getMessage());
            }*/
        }
    }

    private static void testNN() {

    }
}
