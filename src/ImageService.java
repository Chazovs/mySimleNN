import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class ImageService {
    private final int filesCount; //количество файлов
    public final int eduLastImageIndex;
    private File[] imagesFiles; //массив файлов для обучения
    public static final int pixelCount = 784; //784 - это ширина картинки умноженная на высоту т.е. 28 на 28
    private static final float eduImagesPercent = 0.3f; //30% выборки - картинки для обучения

    public ImageService(String filePath) {
        this.imagesFiles = new File(filePath).listFiles();
        filesCount = Objects.requireNonNull(this.imagesFiles).length;

        Collections.shuffle(Arrays.asList(this.imagesFiles));

        this.eduLastImageIndex = (int) (filesCount*eduImagesPercent);
    }

    public void reshuffleImages() {
        Collections.shuffle(Arrays.asList(this.imagesFiles));
    }

    /**
     * Возвращает векторизированное представление изображения
     */
    public ImageEntity getImageEntity(Integer imageKey) throws IOException {
        File file = this.imagesFiles[imageKey];
        BufferedImage image = ImageIO.read(file);

        double[] imageVector = new double[this.pixelCount];

        int x = 0;
        int y = 0;

        for (int i = 1; i < this.pixelCount; i++) {
            imageVector[i - 1] = (image.getRGB(x, y) & 0xff)/ 255f;

            x++;

            if (x == image.getWidth()) {
                x = 0;
                y++;
            }
        }

        //ожидаемое значение
        int value = Integer.parseInt(file.getName().charAt(10) + "");

        return new ImageEntity(value, imageVector, imageKey);
    }
}
