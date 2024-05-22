package Math_Functions;

public class Math_Functions {
//    public static float sigmoid (float x){
//        if (x > 10){return 1;}
//
//        else if (x < -10) {return 0;}
//
//        else {return (float) (1/(1+Math.exp(-x)));}
//    }
//    public static float transferDerivative(float Output){
//        return sigmoid(Output) * (1.0f - sigmoid(Output));
//    }
//
    public static float sigmoid(float x) {
            return (float) (1 / (1 + Math.exp(-x)));
    }
    public static float sigDerivative(float x) {
        return sigmoid(x)*(1.0f-sigmoid(x));

    }
    public static float relu(float x){
        if (x < 0){
            return 0;
        }
        else {
            return x;
        }
    }
    public static float reluDiv (float x){
        if ( x < 0){
            return 0;
        }
        else {
            return 1;
        }
    }
    public static float softMax (float[] Inputs, int i){
        float sum = 0;
        for (float z_j : Inputs){
            sum += (float) Math.exp(z_j);
        }
        return Inputs[i]/sum;
    }
    public static float quadratic_cost_div (float a, float y){
        return a-y;
    }
}
