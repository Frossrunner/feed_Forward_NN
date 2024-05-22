package Helper_Classes;

import java.util.List;

public class ImageData {

    private Image image;
    private int label;

    public ImageData (Image image, int label){
        this.image = image;
        this.label = label;
    }

    public Image getImage() {
        return image;
    }
    public int getLabel() {
        return label;
    }
}
