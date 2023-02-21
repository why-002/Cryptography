public class MatrixDriver {
    public static void main(String[] args) {
        int[] one = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        Matrix m = new Matrix(4, 4, one);
        //m.shiftMatrix();
        m.printMatrix();
    }
}
