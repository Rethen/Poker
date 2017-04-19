package com.poseidon.pokers.utils;


public class Functions {
    public Functions() { }

    public static int byteArrayToInt(byte[] byteArrayData, int offset){
        return  (byteArrayData[offset]     & 0xff) << 24 |
                (byteArrayData[offset + 1] & 0xff) << 16 |
                (byteArrayData[offset + 2] & 0xff) << 8  |
                (byteArrayData[offset + 3] & 0xff);
    }
    public static int byteArrayToShortInt(byte[] byteArrayData, int offset){
        return (0     & 0xff) << 24 |
               (0     & 0xff) << 16 |
               (byteArrayData[offset] & 0xff) << 8  |
               (byteArrayData[offset + 1] & 0xff);
    }
    public static byte[] shortToByteArray(int intData){
        byte[] result = new byte[2];
        result[0] = (byte)((intData & 0xFF00) >> 8 );
        result[1] = (byte)(intData & 0xFF);
        return result;
    }
    public static byte[] intToByteArray(int intData){
        byte[] result = new byte[4];
        result[0] = (byte)((intData & 0xFF000000) >> 24 );
        result[1] = (byte)((intData & 0xFF0000) >> 16 );
        result[2] = (byte)((intData & 0xFF00) >> 8 );
        result[3] = (byte)(intData & 0xFF);
        return result;
    }
    public static void setIntToByteArray(byte[] bt, int offset, int intData){
        byte[] result = new byte[4];
        result[0] = (byte)((intData & 0xFF000000) >> 24 );
        result[1] = (byte)((intData & 0xFF0000) >> 16 );
        result[2] = (byte)((intData & 0xFF00) >> 8 );
        result[3] = (byte)(intData & 0xFF);

        for(int i=0; i<result.length; i++) {
            bt[offset + i] = result[i];
        }
    }
    public static void setShortToByteArray(byte[] bt, int offset, int intData){
        byte[] result = new byte[2];
        result[0] = (byte)((intData & 0xFF00) >> 8 );
        result[1] = (byte)(intData & 0xFF);

        for(int i=0; i<result.length; i++) {
            bt[offset + i] = result[i];
        }
    }
    public static boolean isValidNumber(String str) {
        if (str == null) return false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int ascii = (int) c;
            if (ascii < 255) {
                if (ascii >= 48 && ascii <= 57) {
                    //is a number
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * ascii字节流
     * @param ss
     * @return
     */
    public static byte[] StringToBytesUNICODE(String ss){
        if(ss==null)
            return null;
        byte[] ssBuffer = new byte[2];
        try{
            ssBuffer = ss.getBytes("UNICODE");
        }catch(Exception e){
        	e.printStackTrace();
            //汉字的---错
            ssBuffer[0] = -107;
            ssBuffer[1] = 25;
        }
        return ssBuffer;
    }  
    
    public static byte[] StringToBytes(String ss){
        if(ss==null)
            return null;

        byte[] ssBuffer = new byte[2];
        try{ 
        	/*byte[] tttt=ss.getBytes("gb2132");
        	for(int i=0;i<tttt.length;i++){
        		System.out.print(tttt[i]);
            }*/
            ssBuffer = ss.getBytes("utf-16");//"ISO-10646-UCS-2"
            
        }catch(Exception e){
        	e.printStackTrace();
            //汉字的---错
            ssBuffer[0] = -107;
            ssBuffer[1] = 25;
        }
        return ssBuffer;
    }

    public static byte[] StringToBytesUtf8(String ss){
        if(ss==null)
            return null;

        byte[] ssBuffer = new byte[2];
        try{
            ssBuffer = ss.getBytes("UTF-8");
        }catch(Exception e){
        	e.printStackTrace();
            //汉字的---错
            ssBuffer[0] = -107;
            ssBuffer[1] = 25;
        }
        return ssBuffer;
    }

    public static String BytesToString(byte[] ssBuffer){
        if(ssBuffer==null)
            return null;
        int ssLen = ssBuffer.length/2;
        char[] cc = new char[ssLen];

        for(int i = 0;i<ssLen;i++) {
            cc[i] = (char)(((ssBuffer[i*2] & 0xff) << 8) | (ssBuffer[i*2+1] & 0xff));
        }
        return new String(cc);
    }

    public static String BytesToStringUtf8(byte[] ssBuffer){

        if(ssBuffer==null)
            return null;

        byte[] bbt = new byte[ssBuffer.length*2];

        for(int i = 0; i<ssBuffer.length;i++){
            bbt[i*2] = ssBuffer[i];
            bbt[i*2 + 1] = (byte)0;
        }


        int ssLen = bbt.length/2;
        char[] cc = new char[ssLen];

        for(int i = 0;i<ssLen;i++) {
            cc[i] = (char)(((bbt[i*2] & 0xff) << 8) | (bbt[i*2+1] & 0xff));
        }
        return new String(cc);
    }

}
