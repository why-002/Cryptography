public class Matrix {
    byte[][] matrix;
    byte rows;
    byte columns;
    public Matrix(byte rows, byte columns, byte[] byteString){
        matrix = new byte[rows][columns];
        this.rows = rows;
        this.columns = columns;
        int positionCount = 0;

        if (byteString.length < 16){
            byte byteFillerAmount = (byte) ((byte) 16-byteString.length);
            byte[] newByteString = new byte[16];
            for (byte fillerIndex = 0; fillerIndex < newByteString.length; fillerIndex++){
                if (fillerIndex < byteString.length){
                    newByteString[fillerIndex] = byteString[fillerIndex];
                }

                else{
                    newByteString[fillerIndex] = byteFillerAmount;
                }
            }
            byteString = newByteString;
        }

        for (int i=0;i<rows;i++){
            for (int j=0;j<columns;j++){
                matrix[j][i] = byteString[positionCount];
                positionCount++;
            }
        }
    }

    public void shiftMatrixRows(){
        byte[][] newMatrix = new byte[rows][columns];
        for (int i=0;i<rows;i++){
            for (int j=0;j<columns;j++){
                int transcribedLocation = (i + j) % rows;
                newMatrix[i][transcribedLocation] = matrix[i][j];
            }
        }
        matrix = newMatrix;
    }

    public void printMatrix(){
        for (int i=0;i<rows;i++){
            for (int j = 0; j < columns; j++) {
                System.out.print(Integer.toHexString(matrix[i][j]));
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

