import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class main {
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.
        try {
            printArray(ImageToPixels.getPixelsArray("30.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printArray(int[][] a) {
        for (int[] row : a) {
            System.out.println(Arrays.toString(row));
        }
    }
}
