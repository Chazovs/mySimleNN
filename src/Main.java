import java.io.IOException;
import java.util.function.UnaryOperator;

public class Main {
    private static final int epochs = 1000; //количество эпох

    public static void main(String[] args) {
        ImageService imageService = new ImageService("./train");
        NeuralNetwork neuralNetwork = new NeuralNetwork(0.001);
        Researcher researcher = new Researcher(imageService, neuralNetwork);

        researcher.startResearch();
    }
}
