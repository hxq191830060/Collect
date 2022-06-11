package org.promise.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年02月18日 20:15
 * @description:
 */
public class JsonUtil {
    private static ObjectMapper objectMapper=createObjectMapper ();

    public static ObjectMapper createObjectMapper(){
        ObjectMapper objectMapper=new ObjectMapper (  );
        objectMapper.setVisibility (PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility (PropertyAccessor.FIELD,JsonAutoDetect.Visibility.ANY);

        objectMapper.setSerializationInclusion (JsonInclude.Include.NON_NULL);
        objectMapper.configure (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.configure (SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        return objectMapper;
    }

    private JsonUtil(){

    }


    public static <T> T fromJson(String json,Class<T>beanClass){
        if(StringUtils.isBlank (json)){
            return null;
        }
        try {
            return objectMapper.readValue (json,beanClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( (String.format ("from json error! json:%s, %s, msg:%s",json,beanClass,e.getMessage ())) );
        }
    }

    public static <T> T fromJson( String json, TypeReference<T> reference ){
        try {
            return objectMapper.readValue (json,reference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( (String.format ("from json error! json:%s, typeReference:%s, msg:%s",json,reference,e.getMessage ())) );
        }
    }

    public static <T> T fromJson(byte[]json,Class<T>beanClass){
        try {
            return objectMapper.readValue (json,beanClass);
        } catch (IOException e) {
            throw new RuntimeException ( (String.format ("from json error! json:%s, %s, msg:%s",json,beanClass,e.getMessage ())) );
        }
    }


    public static <T> T fromJson( InputStream is, Class<T> beanClass ){
        try {
            return objectMapper.readValue (is,beanClass);
        } catch (IOException e) {
            throw new RuntimeException ( (String.format ("from json inputStream error! %s, msg:%s",beanClass,e.getMessage ())) );
        }
    }

    public static <T> T fromJson( byte[]json, TypeReference<T> reference){
        try {
            return objectMapper.readValue (json,reference);
        } catch (IOException e) {
            throw new RuntimeException ( (String.format ("from json error! json:%s, %s, msg:%s",json,reference,e.getMessage ())) );
        }
    }


    public static String toJson(Object bean){
        if(bean==null){
            return null;
        }
        try {
            return objectMapper.writeValueAsString (bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( (String.format ("toJson error! bean:%s msg:%s",bean,e.getMessage ())) );
        }
    }

    public static byte[] toJsonAsBytes(Object bean){
        if(bean==null){
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes (bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( (String.format ("toJsonAsBytes error! bean:%s msg:%s",bean,e.getMessage ())) );
        }
    }

    public static List fromJsonToList( String json, Class<?>...T){
        if(StringUtils.isBlank (json)){
            return Collections.emptyList ( );
        }
        try {
            JavaType javaType=objectMapper.getTypeFactory ().constructParametricType (List.class,T);
            return objectMapper.readValue (json,javaType);
        }catch (IOException e){
            throw new RuntimeException ( (String.format ("from json error! json:%s, %s, msg:%s",json,T,e.getMessage ())) );
        }

    }

}
