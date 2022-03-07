public class ImageEntity {
    public int value;  //ожидаемое значение от 0 до 9
    public int tempIndex;
    public double[] imageVector; //векторизированное представление изображения

    public ImageEntity(int value, double[] imageVector, Integer tempIndex) {
        this.value = value;
        this.tempIndex = tempIndex;
        this.imageVector = imageVector;
    }
}
