package Reading;

import Helper_Classes.Image;

import javax.swing.*;
import java.awt.*;

public class ImageVisualizer extends JFrame {
    private final Image image;
    private final int imageWidth;
    private final int imageHeight;

    public ImageVisualizer(Image image) {
        this.image = image;
        this.imageWidth = image.getNum_Columns();
        this.imageHeight = image.getNum_Rows();

        setTitle("MNIST Image Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g);
            }
        };
        imagePanel.setPreferredSize(new Dimension(imageWidth * 10, imageHeight * 10)); // Scale up for visibility

        add(imagePanel);
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void drawImage(Graphics g) {
        int[] imageData = image.getPixelValues();

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int pixelValue = imageData[y*imageWidth + x];

                g.setColor(new Color(pixelValue, pixelValue, pixelValue));
                g.fillRect(x * 10, y * 10, 10, 10);
            }
        }
    }
}

