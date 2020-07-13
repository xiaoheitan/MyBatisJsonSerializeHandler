/*
 *    Copyright(c)2002-2019, 成都房猫有限公司
 *    项目名称:fm-framework
 *    文件名称:JsonSerializeHandler.java
 *    Date:2020/5/6 下午3:37
 *    Author:JingFei
 */

package com.fm.pp.dal.sql.mappers.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public abstract class JsonSerializeHandler<T> extends BaseTypeHandler<T> {
    private static final ObjectMapper MAPPER = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    private JavaType javaType;
    @SuppressWarnings("unchecked")
    JsonSerializeHandler() {
        Type type = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (type instanceof Class) {
            Class<T> tClass = (Class<T>) type;
            javaType = MAPPER.getTypeFactory().constructParametricType(tClass, new Class[0]);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            javaType = this.generateJavaType(parameterizedType);
        } else {
            throw new UnsupportedOperationException("不支持该类型");
        }
    }

    private JavaType generateJavaType(ParameterizedType parameterizedType) {
        Type[] typeList = parameterizedType.getActualTypeArguments();
        JavaType[] javaTypeList = new JavaType[typeList.length];

        for (int i = 0; i < typeList.length; i++) {
            Type type = typeList[i];
            if (type instanceof Class) {
                javaTypeList[i] = MAPPER.getTypeFactory().constructParametricType((Class) type, new Class[0]);
            } else if (type instanceof ParameterizedType) {
                javaTypeList[i] = this.generateJavaType((ParameterizedType) type);
            } else {
                throw new UnsupportedOperationException("不支持该类型");
            }
        }

        return MAPPER.getTypeFactory().constructParametricType((Class) parameterizedType.getRawType(), javaTypeList);
    }



    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        if (t != null) {
            try {
                preparedStatement.setString(i, MAPPER.writeValueAsString(t));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        try {
            String value = resultSet.getString(s);
            if (value != null && !Objects.equals(value,"")) {
                return MAPPER.readValue(resultSet.getString(s), javaType);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("反序列化出错:");
        }
        return null;
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        try {
            return  MAPPER.readValue(resultSet.getString(i), javaType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("反序列化出错:");
        }
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        try {
            return  MAPPER.readValue(callableStatement.getString(i), javaType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("反序列化出错:");
        }
    }
}
