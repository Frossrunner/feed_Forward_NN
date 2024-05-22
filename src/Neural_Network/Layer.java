package Neural_Network;

import java.util.Arrays;

public class Layer {
    float[] Inputs;
    float[] Net_Outputs;
    float[] Outputs;
    float[] Deltas;
    int size;

    public Layer (int Size, int prev_Size){
        this.size = Size;
        this.Inputs = new float[prev_Size];
        this.Net_Outputs = new float[Size];
        this.Outputs = new float[Size];
        this.Deltas = new float[Size];
    }

    public int getSize() {
        return size;
    }

    public float[] getInputs() { return Inputs; }
    public void setInputs(float[] inputs) { this.Inputs = inputs; }

    public float[] getNetOutputs() { return Net_Outputs; }
    public void setNetOutputs(float[] netOutputs) { this.Net_Outputs = netOutputs; }

    public float[] getOutputs() { return Outputs; }
    public void setOutputs(float[] outputs) { this.Outputs = outputs; }

    public float[] getDeltas() { return Deltas; }
    public void setDeltas(float[] deltas) { this.Deltas = deltas; }



    public void reset (){
        Arrays.fill(Deltas, 0);
        Arrays.fill(Net_Outputs, 0);
        Arrays.fill(Inputs, 0);
        Arrays.fill(Outputs, 0);
    }
}
