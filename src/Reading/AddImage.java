package Reading;

import Helper_Classes.Image;
import Helper_Classes.ImageData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AddImage {
    public static List<ImageData> addImages (List<ImageData> trainingImages){
        List<ImageData> trainingImagesUpdated = new ArrayList<>();
        for (ImageData image: trainingImages){
            trainingImagesUpdated.add(image);
            Random random = new Random();
            int a = random.nextInt(6);
            switch(a) {
                case 1 -> {
                    ImageData imageLeft = imageRotate(image, -20);
                    trainingImagesUpdated.add(imageLeft);
                }
                case 2 -> {
                    ImageData imageThin = imageThin(image);
                    trainingImagesUpdated.add(imageThin);
                }
                case 3 -> {
                    ImageData imageThick = imageThick(image);
                    trainingImagesUpdated.add(imageThick);
                }
                case 4 -> {
                    ImageData imageRight = imageRotate(image, 20);
                    trainingImagesUpdated.add(imageRight);
                }
                case 5 -> {
                    ImageData imageSquishTop = imageSquish(image, 1);
                    trainingImagesUpdated.add(imageSquishTop);
                }
                case 0 -> {
                    ImageData imageSquishBottom = imageSquish(image, 0);
                    trainingImagesUpdated.add(imageSquishBottom);
                }
            }
        }
        return trainingImagesUpdated;
    }

    private static ImageData imageSquish(ImageData image, int r) {
        int width = 28;
        int height = 28;
        int[] imageValues = new int[width * height];
        int[] oldValues = image.getImage().getPixelValues();
        Arrays.fill(imageValues, 0); // Initialize array to handle empty spots due to squishing

        int centerX = width / 2;
        int centerY = height / 2;

        for (int i = 0; i < width * height; i++) {
            int x = i % width;
            int y = i / width;
            int newX = x;
            int newY = y;

            switch (r) {
                case 0: // Squish vertically towards the center
                    newY = centerY + (int) ((y - centerY) * 0.8);
                    break;
                case 1: // Squish horizontally towards the center
                    newX = centerX + (int) ((x - centerX) * 0.8);
                    break;
                case 2: // Stretch vertically away from the center
                    newY = centerY + (int) ((y - centerY) * 1.2);
                    break;
                case 3: // Stretch horizontally away from the center
                    newX = centerX + (int) ((x - centerX) * 1.2);
                    break;
            }

            // Clamp newX and newY to ensure they are within the image boundaries
            if (newX < 0) newX = 0;
            if (newX >= width) newX = width - 1;
            if (newY < 0) newY = 0;
            if (newY >= height) newY = height - 1;

            imageValues[newY * width + newX] = oldValues[y * width + x];
        }

        Image newImage = new Image(width, height, imageValues);
        return new ImageData(newImage, image.getLabel());
    }



    public static ImageData imageRotate(ImageData image, double deg) {
        int width = 28;
        int height = 28;
        int[] imageValues = new int[width * height];
        int[] oldValues = image.getImage().getPixelValues();
        Arrays.fill(imageValues, 0);

        double radians = -Math.toRadians(deg);  // Negative for inverse rotation

        int centerX = width / 2;
        int centerY = height / 2;

        for (int newY = 0; newY < height; newY++) {
            for (int newX = 0; newX < width; newX++) {
                // Calculate the original coordinates using the inverse of the rotation matrix
                int translatedX = newX - centerX;
                int translatedY = newY - centerY;

                double originalX = translatedX * Math.cos(radians) - translatedY * Math.sin(radians) + centerX;
                double originalY = translatedX * Math.sin(radians) + translatedY * Math.cos(radians) + centerY;

                // Perform bilinear interpolation
                int x1 = (int) Math.floor(originalX);
                int y1 = (int) Math.floor(originalY);
                int x2 = x1 + 1;
                int y2 = y1 + 1;

                double xInterp = originalX - x1;
                double yInterp = originalY - y1;

                int val11 = (x1 >= 0 && x1 < width && y1 >= 0 && y1 < height) ? oldValues[y1 * width + x1] : 0;
                int val12 = (x1 >= 0 && x1 < width && y2 >= 0 && y2 < height) ? oldValues[y2 * width + x1] : 0;
                int val21 = (x2 >= 0 && x2 < width && y1 >= 0 && y1 < height) ? oldValues[y1 * width + x2] : 0;
                int val22 = (x2 >= 0 && x2 < width && y2 >= 0 && y2 < height) ? oldValues[y2 * width + x2] : 0;

                double value = (1 - yInterp) * ((1 - xInterp) * val11 + xInterp * val21) + yInterp * ((1 - xInterp) * val12 + xInterp * val22);
                imageValues[newY * width + newX] = (int) value;
            }
        }

        Image newImage = new Image(width, height, imageValues);
        return new ImageData(newImage, image.getLabel());
    }
    public static ImageData imageThin(ImageData image) {
        int[] imageValues = new int[28*28];
        int[] oldValues = image.getImage().getPixelValues();
        for (int i = 0; i < 28*28; i++){
            int newValue = Math.min(Math.max(oldValues[i]-200, 0)*4, 255);
            imageValues[i] = newValue;
        }
        Image newImage = new Image(28, 28, imageValues);
        return new ImageData(newImage, image.getLabel());
    }
    public static ImageData imageThick(ImageData image) {
        int[] imageValues = new int[28*28];
        int[] oldValues = image.getImage().getPixelValues();
        for (int i = 0; i < 28*28; i++){
            int newValue = 0;
            if (oldValues[i] != 0) {
                newValue = Math.min(oldValues[i] + 200, 255);
            }
            imageValues[i] = newValue;
        }
        Image newImage = new Image(28, 28, imageValues);
        return new ImageData(newImage, image.getLabel());
    }
    public static ImageData imageRandom (){
        int[] imageValues = new int[28*28];
        for (int i = 0; i < 28*28; i++){
            Random rand = new Random();
            int value = rand.nextInt(256);
            imageValues[i] = value;
        }
        Image image = new Image(28,28,imageValues);
        return new ImageData(image, 10);
    }
}
