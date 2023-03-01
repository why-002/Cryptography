public class aesECBDriver {

    public static void main(String[] args) {
        short[][] test = new short[4][4];
        short[] testMessage = new short[1];
        aesECB a = new aesECB(test, testMessage);
        a.generateKeySchedule();
        short[][] f = a.rotateRows(test);
        for (int j=0; j < 4; j++){
            for (int i=0; i < 4; i++){
                System.out.println(f[i][j]);
            }
        }
        System.out.println(Integer.toHexString(a.subBytes((short)5)));
        System.out.println(Integer.toHexString(a.unSubBytes((short)0x63)));
    }

}
