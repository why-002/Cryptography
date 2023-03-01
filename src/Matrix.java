public class Matrix {
    short[][] matrix;
    short rows;
    short columns;
    public Matrix(short rows, short columns, short[] byteString){
        matrix = new short[rows][columns];
        this.rows = rows;
        this.columns = columns;
        int positionCount = 0;

        //If the number of bytes in the ByteString < 16, fills in the missing bytes with the number of bites missing.
            //e.x. if 5 bytes are missing, fills the missing 5 bytes with 5
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

    public Matrix(){
        this(4,4, new short[16]);
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

    public void rotateColumn(short col){
        short finalValue = matrix[rows-1][col];
        for (int rowIndex= rows - 1; rowIndex > 0;rowIndex--){
            matrix[rowIndex][col] = matrix[rowIndex-1][col];
        }
        matrix[0][col] = finalValue;
    }

    public void addToColumn(int col, short[] vector){
        for (int rowIndex = 0; rowIndex < rows; rowIndex++){
            matrix[rowIndex][col] ^= vector[rowIndex];
        }
    }

    public short[] vectorMultiply(short[] vector){
        short[] finalVector = new short[rows];
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            short tempVal = 0;
            for (int colIndex = 0; colIndex < columns; colIndex++) {
                tempVal += finiteFieldMultiply(matrix[rowIndex][colIndex],vector[colIndex]);
            }
            finalVector[rowIndex] = tempVal;
        }
        return finalVector;
    }

    private short finiteFieldMultiply(short a, short b){
        int total = 0;
        int greaterThan255 = 0;
        for (int i=0;i<8;i++){
            if ((a & 1) == 1){
                total ^= b;
            }

            greaterThan255 = b & 0x80;
            a >>= 1;
            b <<= 1;

            if (greaterThan255 != 0){
                b ^= 0x11b;
            }
        }
        return (short)total;
    }
    public short[] getColumn(int col){
        short[] columnArray = new short[columns];
        for (int rowIndex=0; rowIndex < columns; rowIndex++){
            columnArray[rowIndex] = matrix[rowIndex][col];
        }
        return columnArray;
    }

    public void replaceColumn(int col, short[] replacement){
        for (int rowIndex = 0; rowIndex < rows; rowIndex++){
            matrix[rowIndex][col] = replacement[rowIndex];
        }
    }

    public void replaceRow(int row, short[] replacement){
        for (int columnIndex = 0; columnIndex < columns; columnIndex++){
            matrix[row] = replacement;
        }
    }

    public void replaceValue(int row, int column, short value){
        matrix[row][column] = value;
    }

    public String toString(){
        StringBuilder returnString = new StringBuilder();
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

