package com.canalbrewing.myabcdata.resultsetmapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.canalbrewing.myabcdata.resultsetmapper.annotation.DbColumn;
import com.canalbrewing.myabcdata.resultsetmapper.exceptions.ResultSetMapperException;

public class ResultSetMapper {

    public <T> List<T> mapResult(ResultSet rs, Class<T> clazz) throws SQLException {

        List<T> list = new ArrayList<>();

        List<Field> sqlFields = getAvailableFields(rs.getMetaData(), clazz.getDeclaredFields());

        try {
            while (rs.next()) {

                Constructor<T> cons = clazz.getConstructor();
                T object = cons.newInstance();

                for (Field field : sqlFields) {
                    String columnName = field.getAnnotation(DbColumn.class).name();
                    Object columnValue = rs.getObject(columnName);

                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), object.getClass());
                    pd.getWriteMethod().invoke(object, columnValue);
                }

                list.add(clazz.cast(object));
            }
        } catch (SQLException sqe) {
            throw sqe;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResultSetMapperException(ResultSetMapperException.MAPPING_ERROR, ex.getMessage());
        }

        return list;
    }

    public <T> T mapOneResult(ResultSet rs, Class<T> clazz) throws SQLException {

        T object = null;

        List<Field> sqlFields = getAvailableFields(rs.getMetaData(), clazz.getDeclaredFields());

        try {

            while (rs.next()) {

                Constructor<T> cons = clazz.getConstructor();
                object = cons.newInstance();

                for (Field field : sqlFields) {
                    String columnName = field.getAnnotation(DbColumn.class).name();
                    Object columnValue = rs.getObject(columnName);

                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), object.getClass());
                    pd.getWriteMethod().invoke(object, columnValue);
                }

                return clazz.cast(object);
            }
        } catch (SQLException sqe) {
            throw sqe;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResultSetMapperException(ResultSetMapperException.MAPPING_ERROR, ex.getMessage());
        }

        return null;
    }

    // Match the returned columns with the available annotated fields.
    // It's possible that not all annotated fields are included in this ResultSet.
    private List<Field> getAvailableFields(ResultSetMetaData metadata, Field[] fields) throws SQLException {
        List<Field> sqlFields = new ArrayList<>();

        int columnCount = metadata.getColumnCount();
        for (int idx = 1; idx <= columnCount; idx++) {

            String columnLabel = metadata.getColumnLabel(idx);
            for (Field field : fields) {
                if (field.isAnnotationPresent(DbColumn.class)
                        && field.getAnnotation(DbColumn.class).name().equals(columnLabel)) {
                    sqlFields.add(field);
                }
            }
        }

        return sqlFields;
    }

}
