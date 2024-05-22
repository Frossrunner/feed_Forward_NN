package Reading;

import Helper_Classes.Image;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Helper_Classes.ImageData;

public class Reading {

    public static List<ImageData> concatenateImagesAndLabels(String imagesFilePath, String labelsFilePath, int numImages) {
        List<ImageData> imageData = new ArrayList<>();

        try {
            DataInputStream imageStream = new DataInputStream(new FileInputStream(imagesFilePath));
            DataInputStream labelStream = new DataInputStream(new FileInputStream(labelsFilePath));

            // Read header info for images
            int imageMagicNumber = imageStream.readInt();
            int imageNumImages = imageStream.readInt();
            int imageNumRows = imageStream.readInt();
            int imageNumCols = imageStream.readInt();

            // Read header info for labels
            int labelMagicNumber = labelStream.readInt();
            int labelNumLabels = labelStream.readInt();
            System.out.println("read header");
            // Validate header info
            if (imageNumImages != labelNumLabels) {
                throw new IllegalArgumentException("Number of images and labels do not match.");
            }

            // Determine the actual number of images to read
            int actualNumImages = Math.min(numImages, imageNumImages);

            for (int i = 0; i < actualNumImages; i++) {
                // Read image data
                int[] imageDataArray = new int[imageNumCols * imageNumRows];
                for (int j = 0; j < imageDataArray.length; j++) {
                    int pixelValue = imageStream.readUnsignedByte();
                    imageDataArray[j] = pixelValue;
                }

                // Read label data
                int label = labelStream.readUnsignedByte();

                // Create ImageData object and add to the list
                Image image = new Image(imageNumRows, imageNumCols, imageDataArray);
                ImageData data = new ImageData(image, label);
                imageData.add(data);
            }

            imageStream.close();
            labelStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading images or labels: " + e.getMessage());
        }
        System.out.println("read");
        return imageData;
    }


    public static float[][] readBias (float[][] bias){
        float[][] biasReturn = bias;
        try (FileInputStream fis = new FileInputStream("WB_folder/bias784_800_10.bin");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            biasReturn = (float[][]) ois.readObject(); // Read the object and cast it to float[][][]
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return biasReturn;
    }
    public static float[][][] readWeights (float[][][] weights){
        float[][][] weightsReturn = weights;
        try (FileInputStream fis = new FileInputStream("WB_folder/weights784_800_10.bin");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            weightsReturn = (float[][][]) ois.readObject(); // Read the object and cast it to float[][][]
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return weightsReturn;
    }


}
