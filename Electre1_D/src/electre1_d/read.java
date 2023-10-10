package electre1_d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class read {

    public static Object[][] convertToObjects(double[][] matrix) {
        Object[][] result = new Object[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            result[i] = new Object[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        return result;
    }

    public static Object[][] convertIntToObjects(int[][] matrix) {
        Object[][] result = new Object[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            result[i] = new Object[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        return result;
    }

    public static double[][] readCSVFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int rowCount = 0;
            int columnCount = 0;

            // Count the number of rows and columns in the CSV file
            while ((line = reader.readLine()) != null) {
                rowCount++;
                String[] values = line.split(",");
                columnCount = Math.max(columnCount, values.length);
            }

            // Create a two-dimensional array to store the CSV data
            double[][] data = new double[rowCount][columnCount];

            // Reset the reader to read from the beginning of the file
            reader.close();
            reader = new BufferedReader(new FileReader(filePath));

            int row = 0;

            // Read and store the CSV data into the array
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                for (int col = 0; col < values.length; col++) {
                    data[row][col] = Double.parseDouble(values[col]);
                }
                row++;
            }

            reader.close();

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double[] parseDoubleArray(String str) {
        String[] values = str.split(",");
        double[] array = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Double.parseDouble(values[i]);
        }
        return array;
    }

    public static double[][] parseDoubleMatrix(String str) {
        String[] rows = str.split(";");
        double[][] matrix = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            matrix[i] = parseDoubleArray(rows[i]);
        }
        return matrix;
    }

/*    public static String[] columns(String[] matrix, int length) {
        for (int j = 0; j < length; j++) {
            matrix[j] = "Column " + (j + 1);
        }
        return matrix;
    }
*/
}
