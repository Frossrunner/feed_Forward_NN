import Helper_Classes.Image;
import Helper_Classes.ImageData;
import Helper_Classes.ImageDataBatch;
import Neural_Network.Neural_Network;
import Reading.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Helper_Classes.ImageDataBatch.split;
import static Reading.AddImage.addImages;
import static Reading.AddImage.imageRandom;
import static Reading.Reading.*;

public class Main {
    public static int num_Images = 6;
    public static int batch_Size = 10;

    public static void main(String[] args) {

        List<ImageData> imageData = concatenateImagesAndLabels(args[0], args[1], num_Images);
        List<ImageData> imageDataTest = concatenateImagesAndLabels(args[2], args[3], 10000);
        System.out.println("read all");
        List<ImageData> imageDataPlus = addImages(imageData);
        Collections.shuffle(imageDataPlus);
        System.out.println("added altered images");

        System.out.println("totalNumber of 0-9 is "+ imageDataPlus.size());


        // top results : 98.57%
        // best results with input {784, 800, 10}, 0.1 learn, 5.0 lambda, 10 batchSize, epochs 30
        List<ImageDataBatch> split = split(imageDataPlus, 10);
        int[] input = {784, 800, 10};
        Neural_Network NN = new Neural_Network(input, 0.1f,5.0f, 10, 30);

        float[][][] weights = NN.getWeights();
        float[][] bias = NN.getBias();
        weights = readWeights(weights);
        bias = readBias(bias);

        NN.setWeights(weights);
        NN.setBias(bias);
        NN.test(imageDataTest);
//        NN.train(split,imageDataTest);

//        try (FileOutputStream fosWeights = new FileOutputStream("weights.bin");
//             ObjectOutputStream oosWeights = new ObjectOutputStream(fosWeights);
//             FileOutputStream fosBias = new FileOutputStream("bias.bin");
//             ObjectOutputStream oosBias = new ObjectOutputStream(fosBias)) {
//            oosWeights.writeObject(weights);
//            oosBias.writeObject(bias);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}