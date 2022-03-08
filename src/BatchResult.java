public class BatchResult {
    public int rightCount;
     //показатель ошибки, который рассчитывается как разница между ожидаемой координатой в векторе
    //и модулем реальной координаты результата
     public double errorsValue;

    public BatchResult() {
        this.rightCount = 0;
        this.errorsValue = 0;
    }

    public void reset() {
        this.rightCount = 0;
        this.errorsValue = 0;
    }
}
