import java.io.IOException;

public class main {
    public static void main(String[] args) {
        String baseImagePath = "image1.jpg"; // Replace with your base image path
        String secretImagePath = "image2.jpg"; // Replace with your secret image path
        String encodedImagePath = "encoded_image_with_secret.png"; // Output path for encoded image
        String decodedImagePath = "decoded_hidden_image.png"; // Output path for decoded image

        // Encode the secret image into the base image
        try {
            LSBImageEncodingDecoding.encodeLSBImage(baseImagePath, secretImagePath, encodedImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Decode the hidden image from the encoded image
        try {
            LSBImageEncodingDecoding.decodeLSBImage(encodedImagePath, decodedImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
