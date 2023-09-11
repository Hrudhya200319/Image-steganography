import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LSBImageEncodingDecoding {
    public static void encodeLSBImage(String baseImagePath, String secretImagePath, String outputPath) throws IOException {
        // Open the base image
        BufferedImage baseImage = ImageIO.read(new File(baseImagePath));

        // Open the secret image
        BufferedImage secretImage = ImageIO.read(new File(secretImagePath));

        // Ensure both images have the same dimensions
        int width = baseImage.getWidth();
        int height = baseImage.getHeight();
        secretImage = resizeImage(secretImage, width, height);

        // Create a copy of the base image to modify
        BufferedImage encodedImage = deepCopy(baseImage);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int baseRGB = baseImage.getRGB(x, y);
                int secretRGB = secretImage.getRGB(x, y);

                int encodedRGB = (baseRGB & 0xFFFFFFFE) | (secretRGB & 0x00000001);

                encodedImage.setRGB(x, y, encodedRGB);
            }
        }

        // Save the encoded image
        File outputImageFile = new File(outputPath);
        ImageIO.write(encodedImage, "png", outputImageFile);
        System.out.println("LSB image insertion complete.");
    }

    public static void decodeLSBImage(String encodedImagePath, String outputPath) throws IOException {
        // Open the encoded image
        BufferedImage encodedImage = ImageIO.read(new File(encodedImagePath));

        // Create a new image to store the extracted secret
        BufferedImage decodedImage = new BufferedImage(encodedImage.getWidth(), encodedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < encodedImage.getHeight(); y++) {
            for (int x = 0; x < encodedImage.getWidth(); x++) {
                int encodedRGB = encodedImage.getRGB(x, y);
                int decodedRGB = encodedRGB & 0x00000001;

                // Scale the decoded pixel to 0-255
                decodedRGB *= 255;

                decodedImage.setRGB(x, y, (decodedRGB << 16) | (decodedRGB << 8) | decodedRGB);
            }
        }

        // Save the decoded image
        File outputImageFile = new File(outputPath);
        ImageIO.write(decodedImage, "png", outputImageFile);
        System.out.println("LSB image extraction complete.");
    }

    // Utility function to resize an image to the specified width and height
    private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        resizedImage.createGraphics().drawImage(image, 0, 0, width, height, null);
        return resizedImage;
    }

    // Utility function to create a deep copy of a BufferedImage
    private static BufferedImage deepCopy(BufferedImage original) {
        ColorModel colorModel = original.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = original.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
}
