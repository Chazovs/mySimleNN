public class BatchResult {
    public int rightCount;
    public double errorsCount;

    public BatchResult() {
        this.rightCount = 0;
        this.errorsCount = 0;
    }

    public void reset() {
        this.rightCount = 0;
        this.errorsCount = 0;
    }
}
