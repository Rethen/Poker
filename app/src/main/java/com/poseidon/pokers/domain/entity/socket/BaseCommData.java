package com.poseidon.pokers.domain.entity.socket;

import android.util.SparseArray;


import com.poseidon.pokers.domain.entity.socket.annotation.CommClass;
import com.poseidon.pokers.domain.entity.socket.annotation.CommColumn;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Base class for communication protocal
 * Created by longyi01 on 2016/5/31.
 */
public class BaseCommData {
    private static final String TAG = "BaseCommData";

    /**
     * node for Field, with annotation cache
     */
    public static class CommFieldNode {
        public CommColumn columnAnnotation;
        public Field field;
        public CommFieldNode(CommColumn column, Field field) {
            this.columnAnnotation = column;
            this.field = field;
        }
    }

    /**
     * cache class to accelerating parsing
     * the cache class includes: target class, nodes array, required column set.
     **/
    public static class BaseCommClassInfoCache {
        public Class<? extends BaseCommData> targetCls;
        public SparseArray<CommFieldNode> fieldNodeArray;
        private int[] requiredColumnKeys;

        public BaseCommClassInfoCache(Class<? extends BaseCommData> cls) {
            this.targetCls = cls;
            Field[] fields = cls.getFields();
            fieldNodeArray = new SparseArray<>(fields.length);
            int[] tmpColumns = new int[fields.length];
            int curSize = 0;
            for (Field field : fields) {
                CommColumn annotation = field.getAnnotation(CommColumn.class);
                if (annotation != null) {
                    fieldNodeArray.put(annotation.value(), new CommFieldNode(annotation, field));
                    if (annotation.required()) {
                        tmpColumns[curSize++] = annotation.value();
                    }
                }
            }
            requiredColumnKeys = new int[curSize];
            System.arraycopy(tmpColumns, 0, requiredColumnKeys, 0, curSize);
            Arrays.sort(requiredColumnKeys);
        }

        /**
         * check whether the columns contains all the required fields
         * @param sortedColumns column array for check, sorted from less to large
         * @param length array length to check
         * @return true if all required fields are included in the {@code sortedColumns}
         */
        public boolean containsAllRequiredKeys(int[] sortedColumns, int length) {
            return isSubArray(sortedColumns, length, requiredColumnKeys, requiredColumnKeys.length);
        }
    }

    /**
     * check whether the subSortedArray's sub sequence is the subset of the sorted array
     * @param sortedArray array includes all number
     * @param length array's valid length
     * @param subSortedArray array includes the sub sequence to check
     * @param subLength the array length of the sub sequence
     * @return true if all the {@code subSortedArray} elements are includes in {@code sortedArray}
     */
    private static boolean isSubArray(int[] sortedArray, int length, int[] subSortedArray, int subLength) {
        if (length > sortedArray.length || subLength > subSortedArray.length) {
            throw new IllegalArgumentException("length mismatch!");
        }

        if (subLength > length) {
            return false;
        }
        int pos = 0;
        for (int i = 0; i < subLength; i++) {
            int curKey = subSortedArray[i];
            while (pos < length && sortedArray[pos] != curKey) {
                pos++;
            }
            if (pos == length) {
                return false;
            }
        }
        return true;
    }

    /**
     * key: class request code
     * value: the info of the {@code BaseCommClass}
     */
    private static SparseArray<BaseCommClassInfoCache> sClsInfoCache;

    private static synchronized void initializeInfoCache() {
        if (sClsInfoCache != null) {
            return;
        }
        Class<?> classes[] = CommClasses.class.getClasses();
        SparseArray<BaseCommClassInfoCache> tmpCache = new SparseArray<>(classes.length);
        for (Class<?> cls : classes) {
            if (BaseCommData.class.isAssignableFrom(cls)) {
                @SuppressWarnings("unchecked")
                Class<? extends BaseCommData> subClass = (Class<? extends BaseCommData>) cls;
                int key = BaseCommData.getClassLinkedMsg(subClass);
                if (key != -1) {
                    tmpCache.put(key, new BaseCommClassInfoCache(subClass));
                }
            } else {
                throw new IllegalArgumentException(
                        String.format("Class[%s] is not implements from BaseCommData", cls.getName()));
            }
        }
        sClsInfoCache = tmpCache;
    }

    /**
     * get the corresponding class to the {@code requestCode}
     * @param requestCode code to request
     * @return corresponding class to the {@code requestCode}, null if not found.
     */
    public static synchronized BaseCommClassInfoCache getCommClassInfoCache(int requestCode) {
        if (sClsInfoCache == null) {
            initializeInfoCache();
        }

        return sClsInfoCache.get(requestCode);
    }

    /**
     * get corresponding communication code for the {@code BaseCommData}
     * @param cls class to request
     * @return corresponding code for this class, -1 if not present.
     */
    public static int getClassLinkedMsg(Class<? extends BaseCommData> cls) {
        CommClass annotation = cls.getAnnotation(CommClass.class);
        return annotation != null ? annotation.value() : -1;
    }
}
