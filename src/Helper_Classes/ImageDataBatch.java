package Helper_Classes;

import java.util.ArrayList;
import java.util.List;

public class ImageDataBatch {
    private final List<ImageData> batch;
    public ImageDataBatch (List<ImageData> batch){
        this.batch = batch;
    }
    public List<ImageData> getBatch() {
        return batch;
    }
    public static List<ImageDataBatch> split (List<ImageData> trainingData, int batchSize){
        List<ImageDataBatch> batches = new ArrayList<>();
        for (int i = 0; i < trainingData.size(); i += batchSize) {
            int end = Math.min(i + batchSize, trainingData.size());
            ImageDataBatch batch = new ImageDataBatch(trainingData.subList(i, end));
            batches.add(batch);
        }
        return batches;
    }

}
