import java.io.IOException;

public class main {
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.
        try {
            printArray(squareToAngular(ImageToPixels.getPixelsArray("pacman128.jpg")));
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
            System.out.println("},");
        }
        System.out.print("}");
    }

    private static int[][] squareToAngular(int[][] a) {
        int NUM_OF_LEDS = 10;
        int MAX_DEGREE = 360;
        int[][] angularResult = new int[MAX_DEGREE][NUM_OF_LEDS];

        for (int degree = 0; degree < 360; degree++) {
            double baseX = Math.cos(Math.toRadians(degree)) * (double) a.length / 2 / (double) NUM_OF_LEDS;
            double baseY = Math.sin(Math.toRadians(degree)) * (double) a.length / 2 / (double) NUM_OF_LEDS;
//            System.out.print(baseX);
//            System.out.print(", ");
//            System.out.println(baseY);
            for (int i = 0; i < NUM_OF_LEDS; i++) {
                int x = (int) (Math.floor(baseX * i) + a.length / 2);
                int y = (int) (Math.floor(baseY * i) + a.length / 2);
//                System.out.print(x);
//                System.out.print(", ");
//                System.out.println(y);
                if(degree < 180)
                    angularResult[degree][i] = degree % 180 * 255 / 180;
                if(degree >= 180)
                    angularResult[degree][i] = 255 - (degree % 180 * 255 / 180);
            }
        }

        return angularResult;
    }
}
