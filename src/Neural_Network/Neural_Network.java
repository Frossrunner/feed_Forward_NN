package Neural_Network;

import Helper_Classes.Image;
import Helper_Classes.ImageData;
import Helper_Classes.ImageDataBatch;
import Reading.ImageVisualizer;


import java.util.List;

import static Math_Functions.Math_Functions.*;

public class Neural_Network {

    private Layer[] layers;
    private float[][][] weights;
    private float[][][] weightUpdates;
    private float[][] bias;
    private float[][] biasUpdates;
    private int[] stats;
    private float learn;
    private float lmda;
    private int batchSize;
    private int epochs;

    public Neural_Network(int[] sizes, float learn, float lmda, int batchSize, int epochs){
        this.stats=sizes;
        this.learn=learn;
        this.lmda = lmda;
        this.batchSize=batchSize;
        this.epochs=epochs;

        this.weights = new float[stats.length-1][][];
        this.weightUpdates = new float[stats.length-1][][];
        this.bias = new float[stats.length-1][];
        this.biasUpdates = new float[stats.length-1][];

        this.layers=new Layer[stats.length-1];

        for (int l = 0; l < stats.length-1; l ++){
            layers[l] = new Layer(stats[l + 1],stats[l]);
            weights[l] = new float[stats[l + 1]][stats[l]];
            weightUpdates[l] = new float[stats[l + 1]][stats[l]];
            bias[l] = new float[stats[l + 1]];
            biasUpdates[l] = new float[stats[l + 1]];
            for (int n = 0; n < stats[l+1]; n++){
                bias[l][n] = (float) (Math.random()-0.5f);
                biasUpdates[l][n] = 0;
                for (int w = 0; w < stats[l]; w++){
                    weights[l][n][w] = (float) (Math.random()-0.5f);
                    weightUpdates[l][n][w] = 0;
                }
            }
        }
    }
    public int test (List<ImageData> testExamples){
        int correct = 0;
        int incorrect = 0;
        int incorrectIndex = 0;
        for (int i = 0; i < testExamples.size(); i++){
            ImageData image = testExamples.get(i);
            float[] outputs = forward_Propagate(image.getImage().getPixelValuesDecimal());
            boolean answer = getAnswer(outputs, image.getLabel());
            if (answer){
                correct+=1;
            }
            else{
                incorrect+=1;
//                new ImageVisualizer(testExamples.get(i).getImage()).setVisible(true);
            }
        }
        float percentage = (float) (correct * 100) / (incorrect+correct);
        System.out.println("NN has a success rate of " + percentage+"%");
        return incorrectIndex;
    }
    public void train (List<ImageDataBatch> Training_batches, List<ImageData> testExamples){
        System.out.println("training");
        for (int i = 0 ; i < epochs; i++) {
            System.out.println("training epoch "+i);
            for (ImageDataBatch batch : Training_batches) {
                for (ImageData image : batch.getBatch()) {
                    trainExample(image);
                }
                updateNetwork();
            }
            int in = test(testExamples);
            if (i == 30){
                ImageVisualizer IV = new ImageVisualizer(testExamples.get(in).getImage());
                IV.setVisible(true);
            }
        }
    }
    public void trainExample (ImageData image){
            float[] outputs = forward_Propagate(image.getImage().getPixelValuesDecimal());
            float[] expected = expected(image.getLabel());
            backPropagate(expected);
            resetLayers();

    }


    public float[] forward_Propagate ( float[] inputs){
        float[] in = inputs;
        float[] outputsFinal = new float[stats[stats.length-1]];
        for (int l = 0; l < stats.length-1; l ++){
            Layer layer = layers[l];
            float[] outputs = new float[stats[l+1]];
            float[] netOutputs = new float[stats[l+1]];
            float[][] WeightsLayer = weights[l];
            layer.setInputs(in);
            for (int i = 0; i < outputs.length; i++){
                float sum = 0.0f;
                for (int j = 0; j < WeightsLayer[i].length; j++) {
                    sum += (WeightsLayer[i][j] * in[j]);
                }
                if (l == stats.length-2) {
                    netOutputs[i] = sum;
                    outputs[i] = sigmoid(sum);
                }
                else {
                    netOutputs[i] = sum;
                    outputs[i] = sigmoid(sum);
                }
            }
            layer.setNetOutputs(netOutputs);
            layer.setOutputs(outputs);
            in = outputs;

            if (l == stats.length-2){
                outputsFinal = outputs;
            }
        }


        return outputsFinal;
    }
    public void backPropagate (float[] expected){
        Layer p_layer = null;
        float totalLoss = 0;
        for (int l = stats.length-2; l >= 0; l --){
            Layer layer = layers[l];
            float[] outputs = layer.getOutputs();
            float[] netOut = layer.getNetOutputs();
            float[] inputs = layer.getInputs();
            float[] deltas = new float[layer.getSize()];

            if (l == stats.length-2){
                for (int n = 0; n < layer.getSize(); n ++){
                    float delta = sigmoid(netOut[n]) - expected[n];
//                            quadratic_cost_div(outputs[n],expected[n])*sigDerivative(netOut[n]);
                    deltas[n] = delta;
                    totalLoss += delta;
                    biasUpdates[l][n] += delta;
                    for (int w = 0; w < stats[l]; w++){
                        weightUpdates[l][n][w] += delta*inputs[w] + lmda*weights[l][n][w]/50000;
                    }
                }
            }
            else{
                for (int n = 0; n < layer.getSize(); n ++){
                    float sum = 0;
                    for (int pn = 0; pn < p_layer.getSize(); pn ++){
                        sum += p_layer.getDeltas()[pn]*weights[l+1][pn][n];
                    }
                    float delta = sum*sigDerivative(netOut[n]);
                    deltas[n] = delta;
                    totalLoss += delta;
                    biasUpdates[l][n] += delta;
                    for (int w = 0; w < stats[l]; w++){
                        weightUpdates[l][n][w] += delta*inputs[w] + lmda*weights[l][n][w]/50000;
                    }
                }
            }
            layer.setDeltas(deltas);
            p_layer = layer;
        }
//        System.out.println("total error "+totalLoss);
    }
    public void updateNetwork (){
        for (int l = 0; l < stats.length-1; l ++){
            for (int n = 0; n < stats[l+1]; n++){
                bias[l][n] -= (learn/batchSize)*biasUpdates[l][n];
                biasUpdates[l][n] = 0;
                for (int w = 0; w < stats[l]; w++){
                    weights[l][n][w] -= (learn/batchSize)*weightUpdates[l][n][w];
                    weightUpdates[l][n][w] = 0;
                }
            }
        }
    }
    public boolean getAnswer(float[] nnO, int label) {
        float Highest = 0;
        int Answer = 0;
        for (int i = 0; i < stats[stats.length-1]; i++){
            if (nnO[i] > Highest){
                Answer = i;
                Highest = nnO[i];
            }
        }
        return Answer == label;
    }

    public float[] expected (int label){
        float[] expected = new float[stats[stats.length -1]];
        for (int j = 0; j < stats[stats.length -1]; j++){
            if (j == label){expected[j] = 1;}
            else {expected[j] = 0;}
        }
        return expected;
    }
    public void resetLayers (){
        for (Layer layer : layers){
            layer.reset();
        }
    }

    public float[][][] getWeights() {
        return weights;
    }
    public float[][] getBias() {
        return bias;
    }

    public void setBias(float[][] bias) {
        this.bias = bias;
    }
    public void setWeights(float[][][] weights) {
        this.weights = weights;
    }
}
