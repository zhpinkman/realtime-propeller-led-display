import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;

public class ImageToPixels {
    private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    private static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }

        return result;
    }

    public static int[][] getPixelsArray(String url) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(url);
        BufferedImage bufferedImage = ImageIO.read(is);
//        byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
//        System.out.println(Arrays.deepToString(pixels));
        BufferedImage hugeImage = ImageIO.read(ImageToPixels.class.getResource(url));
        return argbTogray(ImageToPixels.convertTo2DUsingGetRGB(hugeImage));
    }

    private static int[][] argbTogray(int[][] argb) {
        int width = argb[0].length;
        int height = argb.length;
        int[][] grayResult = new int[height][width];
        for (int i = 0; i < argb.length; i++) {
            for (int j = 0; j < argb[0].length; j++) {
                Color myColor = new Color(argb[i][j]);
                grayResult[i][j] = (myColor.getRed() + myColor.getGreen() + myColor.getBlue()) / 3;
            }
        }
        return grayResult;
    }
}
