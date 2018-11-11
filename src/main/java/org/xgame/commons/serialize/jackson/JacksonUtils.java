package org.xgame.commons.serialize.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @Name: JacksonUtils.class
 * @Description: //
 * @Create: DerekWu on 2018/10/17 8:46
 * @Version: V1.0
 */
public class JacksonUtils {

    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            synchronized (JacksonUtils.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                    //        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                    //        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                    // 允许序列化空的POJO类
                    // （否则会抛出异常）
                    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                    // 把java.util.Date, Calendar输出为数字（时间戳）
                    objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    // 为NULL不参与序列化
                    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                }
            }
        }
        return objectMapper;
    }

    public static void print(Object object) throws Exception {
        String objectStr = JacksonUtils.getObjectMapper().writeValueAsString(object);
        System.out.println(objectStr);
    }

    public static String valueAsString(Object object) throws Exception {
        String objectStr = JacksonUtils.getObjectMapper().writeValueAsString(object);
        return objectStr;
    }

}
