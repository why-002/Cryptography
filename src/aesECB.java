public class aesECB {
    private short[][] key;
    private short[] message;

    final private short[] roundKey = {0x1,0x2,0x4,0x8,0x10,0x20,0x40, 0x80, 0x1b, 0x36};

    public short[][][] schedule = new short[11][4][4];

    private short [][] sBox = {
        {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
        {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
        {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
        {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
        {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
        {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
        {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
        {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
        {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
        {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
        {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
        {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
        {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
        {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
        {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
        {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16},
    };

    private short[][] mixColumnsMatrix = {
            {2,3,1,1},{1,2,3,1},{1,1,2,3},{3,1,1,2}
    };

    public aesECB(short[][] key, short[] message){
        this.key = key;
        this.message = message;

    }

    private short subByte(short original){
        short tensPlace = (short) (original / 16);
        short onesPlace = (short) (original % 16);
        return sBox[tensPlace][onesPlace];
    }

    private short unSubByte(short original){
        for (int rowIndex=0; rowIndex < sBox.length; rowIndex++){
            for (int colIndex=0; colIndex < sBox.length; colIndex++){
                if (sBox[rowIndex][colIndex] == original){
                    return (short) (rowIndex << 4 | colIndex);
                }
            }
        }
        return -1;
    }

    private short[] subBytes(short[] bytes){
        short[] newBytes = new short[bytes.length];
        for (int colIndex=0; colIndex < bytes.length;colIndex++){
            newBytes[colIndex] = subByte(bytes[colIndex]);
        }
        return newBytes;
    }

    private short[] unSubBytes(short[] bytes){
        for (int colIndex=0; colIndex < bytes.length;colIndex++){
            bytes[colIndex] = unSubByte(bytes[colIndex]);
        }
        return bytes;
    }
    public void generateKeySchedule(){
        schedule[0] = key;
        for (int round=1; round < 11; round++){
            schedule[round][0] = generateComplexKey(schedule[round-1][3], round);
            for (int col = 1; col < 4; col++){
                schedule[round][col] = addColumns(schedule[round][col-1],schedule[round-1][col]);
            }
        }
    }

    private short[] generateComplexKey(short[] previous, int round){
        short[] newKey = rotArray(previous, 1);
        newKey = subBytes(newKey);
        newKey[0] ^= roundKey[round-1];
        newKey = addColumns(newKey, schedule[round-1][0]);
        return newKey;
    }

    private short[] rotArray(short[] previous, int num){
        short[] newKey = new short[4];
        for (int colIndex = 0; colIndex < 4; colIndex ++){
            newKey[(colIndex + num) % 4] = previous[colIndex];
        }
        return newKey;
    }

    private short[] addIntToColumn(short[] col, int val){
        short[] newCol = new short[col.length];
        for (int i=0;i < 4; i++){
            newCol[i] += val;
        }
        return newCol;
    }

    private short[] addColumns(short[] col1, short[] col2){
        short[] newCol = new short[4];
        for (int row=0; row < 4; row++){
            newCol[row] = (short) ((col1[row] ^ col2[row]) % 256);
        }
        return newCol;
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

    private short[][] rotateRows(short[][] matrix){
        short[][] tempArray = new short[4][4];
        for (int row=0; row<4;row++){
            for (int col=0; col < 4;col++){
                tempArray[(col+row) % 4][row] = matrix[col][row];
            }
        }
        return tempArray;
    }

    public short[] mixColumns(short[] vector){
        // this is completely wrong
        short[] finalVector = new short[4];
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
            short tempVal = 0;
            for (int colIndex = 0; colIndex < 4; colIndex++) {
                tempVal ^= finiteFieldMultiply(mixColumnsMatrix[colIndex][rowIndex],vector[colIndex]);
            }
            finalVector[rowIndex] = tempVal;
        }
        return finalVector;
    }
    public short[] encryptMessage(){
        // Todo turn message into matricies
        generateKeySchedule();
        short[][][] matricies = new short[message.length / 4 + 1][4][4];
        short[] newMessage = new short[(message.length / 4 + 1) * 16];
        for (int index=0; index < message.length; index++){
            newMessage[index] = message[index];
        }
        for (int index=message.length; index < newMessage.length; index++){
            newMessage[index] =  (short)(16 - message.length % 16);
        }





        int count = 0;
        for (int matriceNum = 0; matriceNum < newMessage.length / 16; matriceNum++){
            for (int column=0; column < 4; column++){
                for (int row=0; row < 4; row++){
                    matricies[matriceNum][column][row] = newMessage[count];
                    count++;
                }
            }
        }

        for (int matrice=0; matrice < (newMessage.length / 16); matrice++) {
            // Addition of the first round key
            for (int column = 0; column < 4; column++) {
                matricies[matrice][column] = addColumns(matricies[matrice][column],schedule[0][column]);
            }

            for (int round = 0; round < 9; round++) {
                for (int column = 0; column < 4; column++) {
                    matricies[matrice][column] = subBytes(matricies[matrice][column]);
                }

                matricies[matrice] = rotateRows(matricies[matrice]);

                for (int column = 0; column < 4; column++) {
                    matricies[matrice][column] = mixColumns(matricies[matrice][column]);
                }

                for (int column = 0; column < 4; column++) {
                    matricies[matrice][column] = addColumns(matricies[matrice][column],schedule[round][column]);
                }
                int x = 3;
            }

            for (int column = 0; column < 4; column++) {
                matricies[matrice][column] = subBytes(matricies[matrice][column]);
                matricies[matrice] = rotateRows(matricies[matrice]);
                matricies[matrice][column] = addColumns(matricies[matrice][column],schedule[10][column]);
            }
        }
        // Todo turn back into array

        short[] finalArray = new short[newMessage.length];
        count = 0;
        for (int matriceNum = 0; matriceNum < newMessage.length / 16; matriceNum++){
            for (int column=0; column < 4; column++){
                for (int row=0; row < 4; row++){
                    finalArray[count] = matricies[matriceNum][column][row];
                    count++;
                }
            }
        }
        return finalArray;
    }
}
