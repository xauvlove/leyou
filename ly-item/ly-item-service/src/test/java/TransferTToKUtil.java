import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TransferTToKUtil<T, K> {

    /**
     *
     * @param t 原始类对象
     * @param kClass 目标类
     * @return
     * @throws Exception
     */
    public K transferTToK2(T t, Class<K> kClass) {
        K instance = null;
        try {
            Class tClass = t.getClass();
            Field[] tFields = tClass.getDeclaredFields();
            Field[] kFields = kClass.getDeclaredFields();
            instance = kClass.getConstructor().newInstance();
            for (Field tField : tFields) {
                for (Field kField : kFields) {
                    if (tField.getName().equals(kField.getName())) {
                        String fieldName = tField.getName();
                        Field instanceField = instance.getClass().getDeclaredField(fieldName);
                        instanceField.setAccessible(true);
                        tField.setAccessible(true);
                        Object o = tField.get(t);
                        instanceField.set(instance, o);
                        break;
                    }
                }
            }
            return instance;
        } catch (Exception e) {
            log.error("TransferTToKUtil transferTToK", e);
            System.out.println("*******************error**********************");
        }
        return instance;
    }

    public K transferTToK(T t, Class<K> kClass) throws Exception {
        K instance = null;
        Class tClass = t.getClass();
        Field[] tFields = tClass.getDeclaredFields();
        Field[] kFields = kClass.getDeclaredFields();
        instance = kClass.getConstructor().newInstance();

        Map<String, Object> fieldNameValueMap = Lists.newArrayList(tFields)
                .stream().collect(Collectors.toMap(
                        Field::getName, field -> {
                            try {
                                field.setAccessible(true);
                                return field.get(t);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }, (oldVal, newVal) -> oldVal));
        for(Field kField: kFields) {
            kField.setAccessible(true);
            kField.set(instance, fieldNameValueMap.get(kField.getName()));
        }
        return instance;
    }
}
