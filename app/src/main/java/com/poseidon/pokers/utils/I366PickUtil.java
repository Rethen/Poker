package com.poseidon.pokers.utils;



import com.elvishew.xlog.XLog;
import com.poseidon.pokers.BuildConfig;
import com.poseidon.pokers.domain.entity.socket.BaseCommData;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class I366PickUtil {
    /**
     * 1个字节流类型
     */
    public static final int TYPE_INT_1 = 0;

    /**
     * 4个字节流类型
     **/
    public static final int TYPE_INT_4 = 1;

    /**
     * UTF-16  字节流转码
     */
    public static final int TYPE_STRING_UTF16 = 2;

    /**
     * ascii unicode 字节流转码
     */
    public static final int TYPE_STRING_UNICODE = 3;

    /**
     * int 数组
     */
    public static final int TYPE_INT_4_ARRAY = 5;
    /**
     * int 1 数组
     */

    public static final int TYPE_INT_1_ARRAY = 6;

    /**
     * 一个bute itemid < 50
     */
    public static final int TYPE_BYTE = 4;

    public static final String UTF_16 = "utf-16";

    public static final String UNICODE = "UNICODE";

    /**
     * 最大支持的包
     */
    private static final int MAX_CP_SIZE = 1024 * 8;

    /**
     * 返回包内容开始位置
     */
    private static final int FEE_DATA_BT_LENGTH = 30;

    /**
     * 解析最多的参数个数
     */
    public static final int MAX_FOR = 80;
    /**
     * 协议中协议内容开始的位置
     */
    private static final int START_LOCATION = 20;

    private static int byteArrayToInt(byte[] byteArrayData, int offset) {
        return (byteArrayData[offset] & 0xff) << 24
                | (byteArrayData[offset + 1] & 0xff) << 16
                | (byteArrayData[offset + 2] & 0xff) << 8
                | (byteArrayData[offset + 3] & 0xff);
    }

    /**
     * @param bytes    data input raw data
     * @param startPos start parse
     * @param field    parser field
     * @param instance parser instance
     *
     * @return data size to skip
     *
     * @throws IllegalAccessException
     */
    private static int pickField(byte[] bytes, int startPos, Field field, Object instance)
            throws IllegalAccessException {
        int startPosOrg = startPos;
        int dataSize = 0;
        Class<?> type = field.getType();
        if (type == byte.class) {
            field.setByte(instance, bytes[++startPos]);
        } else if (type == int.class) {
            try {
                dataSize = Functions.byteArrayToShortInt(bytes, startPos + 1);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
            startPos += 2;
            int typeInt4 = byteArrayToInt(bytes, startPos + 1);
            startPos += 4;
            field.setInt(instance, typeInt4);
        } else if (type == String.class) {
            try {
                dataSize = Functions.byteArrayToShortInt(bytes, startPos + 1);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
            startPos += 2;
            String typeStringUntf16 = null;
            try {
                if (dataSize != 0) {
                    typeStringUntf16 = new String(bytes, startPos + 1, dataSize, UTF_16);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            field.set(instance, typeStringUntf16);
            startPos += dataSize;
        } else if (type == byte[].class) {
            try {
                dataSize = Functions.byteArrayToShortInt(bytes, startPos + 1);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
            startPos += 2;
            byte[] byteArray = new byte[dataSize];
            try {
                if (dataSize != 0) {
                    for (int i2 = 0; i2 < byteArray.length; i2++) {
                        byteArray[i2] = bytes[startPos + i2 + 1];
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.set(instance, byteArray);
            startPos += dataSize;
        } else if (type == int[].class) {
            try {
                dataSize = Functions.byteArrayToShortInt(bytes, startPos + 1);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
            startPos += 2;
            int[] intArray = new int[dataSize / 4];
            try {
                if (dataSize != 0) {
                    for (int i2 = 0; i2 < intArray.length; i2++) {
                        int se = byteArrayToInt(bytes, startPos + i2 * 4 + 1);
                        intArray[i2] = se;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
            field.set(instance, intArray);
            startPos += dataSize;
        }
        return startPos - startPosOrg;
    }

    /**
     * pick all fields by the processor
     *
     * @param bytes     raw bytes
     * @param processor processor to parse data
     *
     * @return the parsed object, null if fail
     */
    public static BaseCommData pickAll(byte[] bytes, BaseCommData.BaseCommClassInfoCache processor) {
        BaseCommData retData;
        try {
            retData = processor.targetCls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int startLocation = START_LOCATION;
        int dataSize;
        int[] filledKeys = new int[processor.fieldNodeArray.size()];
        int filledSize = 0;

        // start parse
        for (int i = 0; i < MAX_FOR; i++) {
            // itemId: 1 bit
            if ((startLocation + 2) > bytes.length) {
                break;
            }
            boolean foundItem = false;
            int itemId = bytes[++startLocation] & 0xff;
            BaseCommData.CommFieldNode fieldNode = processor.fieldNodeArray.get(itemId);
            if (fieldNode != null) {
                try {
                    int skipSize = pickField(bytes, startLocation, fieldNode.field, retData);
                    if (skipSize < 0) {
                        return null;
                    }
                    foundItem = true;
                    startLocation += skipSize;
                    // put keys
                    filledKeys[filledSize++] = itemId;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            // no matching j[0] == itemId
            // just skip the value, in case the server updated with more data column
            if (!foundItem) {
                if (50 <= itemId && itemId < 128) {
                    ++startLocation;
                } else if (itemId >= 128) {
                    dataSize = Functions.byteArrayToShortInt(bytes, startLocation + 1);
                    startLocation += 2 + dataSize;
                }
            }
        }

        // check all required field filled
        Arrays.sort(filledKeys, 0, filledSize);
        if (!processor.containsAllRequiredKeys(filledKeys, filledSize)) {
            return null;
        }
        return retData;
    }

    /**
     * pack the request message to byte array
     *
     * @param int2        2-dimension array, format as：{{1,2,3} , {2,3,4}} the first number:itemID the second number:
     *                    value
     *                    the third number：type
     * @param requestCode protocol number for this message
     * @param userId      the user's id, for most cases, don't fill it with 0
     *
     * @return the byte array corresponding with the int2's info and protocol header
     */
    public static byte[] packAll(Object[][] int2, int requestCode, int userId) {
        XLog.i("PokerSender", "send request:" + requestCode);
        byte[] bt = packHead(userId);
        // Request Code (derived from client request)
        bt[Config.RP_REQUEST_CODE_HIGH] = (byte) (requestCode >> 8);
        bt[Config.RP_REQUEST_CODE_LOW] = (byte) (requestCode);
        int feeDataBtLength = FEE_DATA_BT_LENGTH;
        bt[feeDataBtLength] = (byte) (int2.length);
        int itemId;
        byte[] feePageDataBt;
        for (Object[] objl : int2) {
            itemId = (Integer) objl[0];
            bt[++feeDataBtLength] = (byte) itemId;
            int type = (Integer) objl[2];
            switch (type) {
                case I366PickUtil.TYPE_BYTE:
                    // nothing following
                    break;

                case I366PickUtil.TYPE_INT_1:
                    // followed with one byte
                    byte i_ = 0;
                    if (objl[1] instanceof Number) {
                        i_ = ((Number) objl[1]).byteValue();
                    }
                    bt[++feeDataBtLength] = i_;
                    break;

                case I366PickUtil.TYPE_INT_4:
                    // followed with 4 bytes
                    if (itemId >= 128) {
                        bt[++feeDataBtLength] = (byte) (4 >> 8);
                        bt[++feeDataBtLength] = (byte) (4);
                    }
                    feePageDataBt = Functions.intToByteArray((Integer) objl[1]);
                    System.arraycopy(feePageDataBt, 0, bt, feeDataBtLength + 1, feePageDataBt.length);
                    feeDataBtLength = feeDataBtLength + 4;
                    break;

                case I366PickUtil.TYPE_STRING_UNICODE:
                    // followed with 2 bytes(length), and the other bytes with unicode
                    feePageDataBt = Functions.StringToBytesUNICODE((String) objl[1]);
                    bt[++feeDataBtLength] = (byte) (feePageDataBt.length >> 8);
                    bt[++feeDataBtLength] = (byte) (feePageDataBt.length);
                    System.arraycopy(feePageDataBt, 0, bt, feeDataBtLength + 1, feePageDataBt.length);
                    feeDataBtLength = feeDataBtLength + feePageDataBt.length;
                    break;
                case I366PickUtil.TYPE_STRING_UTF16:
                    // followed with 2 bytes(length), and the other bytes with utf-16
                    feePageDataBt = Functions.StringToBytes((String) objl[1]);
                    bt[++feeDataBtLength] = (byte) (feePageDataBt.length >> 8);
                    bt[++feeDataBtLength] = (byte) (feePageDataBt.length);
                    System.arraycopy(feePageDataBt, 0, bt, feeDataBtLength + 1, feePageDataBt.length);
                    feeDataBtLength = feeDataBtLength + feePageDataBt.length;
                    break;
                case I366PickUtil.TYPE_INT_1_ARRAY:
                    // followed with 2 bytes(array length), and the n bytes
                    Object arr = objl[1];
                    feePageDataBt = new byte[getArrayLength(arr)];
                    for (int i3 = 0; i3 < getArrayLength(arr); i3++) {
                        int a = getArrayItem(arr, i3);
                        feePageDataBt[i3] = (byte) a;
                    }

                    bt[++feeDataBtLength] = (byte) (feePageDataBt.length >> 8);
                    bt[++feeDataBtLength] = (byte) (feePageDataBt.length);
                    System.arraycopy(feePageDataBt, 0, bt, feeDataBtLength + 1, feePageDataBt.length);
                    feeDataBtLength = feeDataBtLength + feePageDataBt.length;
                    break;
                case I366PickUtil.TYPE_INT_4_ARRAY:
                    // followed with 2 bytes(array length), and 4 * n bytes
                    Object arrObj = objl[1];
                    int length = getArrayLength(arrObj);
                    feePageDataBt = new byte[length * 4];
                    for (int i3 = 0; i3 < length; i3++) {
                        int i4 = i3 * 4;
                        byte[] bs = Functions.intToByteArray(getArrayItem(arrObj, i3));
                        feePageDataBt[i4] = bs[0];
                        feePageDataBt[i4 + 1] = bs[1];
                        feePageDataBt[i4 + 2] = bs[2];
                        feePageDataBt[i4 + 3] = bs[3];
                    }

                    bt[++feeDataBtLength] = (byte) (feePageDataBt.length >> 8);
                    bt[++feeDataBtLength] = (byte) (feePageDataBt.length);
                    System.arraycopy(feePageDataBt, 0, bt, feeDataBtLength + 1, feePageDataBt.length);
                    feeDataBtLength = feeDataBtLength + feePageDataBt.length;
                    break;
                default:
                    break;
            }
        }
        ++feeDataBtLength;

        // set total length
        bt[Config.RP_SIZE_HIGH] = (byte) (feeDataBtLength >> 8);
        bt[Config.RP_SIZE_LOW] = (byte) (feeDataBtLength);

        // trunk the unused data
        byte[] bt2 = new byte[feeDataBtLength];
        System.arraycopy(bt, 0, bt2, 0, feeDataBtLength);
        return bt2;
    }

    /**
     * get the arr's length
     * @param arr arr object, supports {Integer[], int[], byte[]}
     * @return the length of the object, throws {@code IllegalArgumentException} if type mismatch
     */
    public static int getArrayLength(Object arr) {
        if (arr instanceof Integer[]) {
            return ((Integer[]) arr).length;
        } else if (arr instanceof int[]) {
            return ((int[]) arr).length;
        } else if (arr instanceof byte[]) {
            return ((byte[]) arr).length;
        } else {
            throw new IllegalArgumentException("Array has incompatible type: " + arr.getClass());
        }
    }

    /**
     * get the element in the array object
     * @param arr an array object, supports {Integer[], int[], byte[]}
     * @param index index for the item
     * @return the value of arr[index]
     */
    public static int getArrayItem(Object arr, int index) {
        if (arr instanceof Integer[]) {
            return ((Integer[]) arr)[index];
        } else if (arr instanceof int[]) {
            return ((int[]) arr)[index];
        } else if (arr instanceof byte[]) {
            return ((byte[]) arr)[index];
        } else {
            throw new IllegalArgumentException("Array has incompatible type: " + arr.getClass());
        }
    }

    /**
     * pack the protocol header with a large byte array
     * @param userId user's id
     * @return a byte array with header, and length is more than header.
     */
    public static byte[] packHead(int userId) {
        byte[] bt = new byte[MAX_CP_SIZE];
        bt[0] = Config.HEADER_INDICATER_0;
        bt[1] = Config.HEADER_INDICATER_1;
        bt[2] = Config.HEADER_INDICATER_2;
        bt[3] = Config.HEADER_INDICATER_3;

        bt[Config.RP_PROTOCOL_VERSION] = Config.PROTOCOL_VERSION;
        byte[] userIDByte = Functions.intToByteArray(userId);
        bt[Config.RP_USER_ID_1] = userIDByte[0];
        bt[Config.RP_USER_ID_2] = userIDByte[1];
        bt[Config.RP_USER_ID_3] = userIDByte[2];
        bt[Config.RP_USER_ID_4] = userIDByte[3];
        bt[Config.RP_LANGUAGE_ID] = Config.LANGUAGE_ID;
        bt[Config.RP_CLIENT_PLATFORM] = Config.CLIENT_PLATFORM;
        bt[Config.RP_CLIENT_BUILD_NUMBER] = (byte) BuildConfig.VERSION_CODE;

        byte[] customId = Functions.shortToByteArray(Config.CUSTOM_ID_1);
        if (!Env.isAbroadVersion()) {
            customId = Functions.shortToByteArray(Config.CUSTOM_ID_2);
        }
        bt[Config.RP_CUSTOM_ID_1] = customId[0];
        bt[Config.RP_CUSTOM_ID_2] = customId[1];

        byte[] porductId = Functions.shortToByteArray(Config.PRODUCT_ID);
        bt[Config.RP_PRODUCT_ID_HIGH] = porductId[0];
        bt[Config.RP_PRODUCT_ID_LOW] = porductId[1];

        return bt;
    }

}
