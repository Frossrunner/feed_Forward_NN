package Helper_Classes;

import Math_Functions.Math_Functions;



public class Image {
    private final int num_Columns;
    private final int num_Rows;
    private final int[] pixelValues;
    public Image (int num_Columns, int num_Rows, int[] pixelValues){
        this.num_Columns = num_Columns;
        this.num_Rows = num_Rows;
        this.pixelValues = new int[num_Rows*num_Columns];
        System.arraycopy(pixelValues, 0, this.pixelValues, 0, pixelValues.length);
    }
    public int[] getPixelValues() {
        return this.pixelValues;
    }
    public float[] getPixelValues2() {
        float[] returnValues = new float[pixelValues.length];
        for (int i = 0; i < pixelValues.length; i++){
            returnValues[i] = pixelValues[i];
        }
       return returnValues;
    }
    public float[] getPixelValuesDecimal(){
        float[] pixelValuesDecimal = new float[num_Rows*num_Columns];
        for (int i = 0; i < num_Rows*num_Columns; i++){
            pixelValuesDecimal[i] = (float) (pixelValues[i])/255;
        }
        return pixelValuesDecimal;
    }

    public int getNum_Columns() {
        return num_Columns;
    }
    public int getNum_Rows() {
        return num_Rows;
    }
}
