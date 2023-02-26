public class Matrix {
    short[][] matrix;
    short rows;
    short columns;
    public Matrix(short rows, short columns, short[] byteString){
        matrix = new short[rows][columns];
        this.rows = rows;
        this.columns = columns;
        int positionCount = 0;

        if (byteString.length < 16){
            short byteFillerValue = (short) (16-byteString.length);
            short[] newByteString = new short[16];
            for (short fillerIndex = 0; fillerIndex < newByteString.length; fillerIndex++){
                if (fillerIndex < byteString.length){
                    newByteString[fillerIndex] = byteString[fillerIndex];
                }

                else{
                    newByteString[fillerIndex] = byteFillerValue;
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
        short[][] newMatrix = new short[rows][columns];
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
                returnString.append(Integer.toHexString(matrix[i][j]));
                returnString.append(" ");

                if (matrix[i][j] < 16){
                    returnString.append(" ");
                }
            }
            returnString.append("\n");
        }
        return returnString.toString();
    }
}

