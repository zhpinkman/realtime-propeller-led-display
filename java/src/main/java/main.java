import java.io.IOException;

public class main {
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.
        try {
            printArray(ImageToPixels.getPixelsArray("pacman128.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printArray(int[][] a) {
        System.out.println(a.length);
        System.out.print("{");
        for (int[] row : a) {
            System.out.print("{");
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if (i != row.length - 1)
                    System.out.print(", ");
            }
            System.out.print("},");
        }
        System.out.print("}");
    }
}
