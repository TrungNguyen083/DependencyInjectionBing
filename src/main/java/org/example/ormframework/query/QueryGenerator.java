package org.example.ormframework.query;

import org.example.ormframework.annotation.Entity;
import org.example.ormframework.annotation.Primary;

import java.lang.reflect.Field;

public class QueryGenerator {
    private QueryGenerator() {
    }

    public static String insertQuery(Object object) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(object.getClass().getSimpleName());
        sb.append(" (");
        for (Field field : object.getClass().getDeclaredFields()) {
            sb.append(field.getName());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES (");
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            sb.append("'");
            sb.append(field.get(object));
            sb.append("'");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    public static String updateQuery(Object object) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(object.getClass().getSimpleName());
        sb.append(" SET ");
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
                if (field.isAnnotationPresent(Primary.class)) {
                    sb.append(field.getName());
                    sb.append("=");
                    sb.append("'");
                    sb.append(field.get(object));
                    sb.append("'");
                    sb.append(" ");
                    sb.append("WHERE");
                    sb.append(" ");
                    sb.append(field.getName());
                    sb.append("=");
                    sb.append("'");
                    sb.append(field.get(object));
                    sb.append("'");
                    sb.append(";");
                } else {
                    sb.append(field.getName());
                    sb.append("=");
                    sb.append("'");
                    sb.append(field.get(object));
                    sb.append("'");
                    sb.append(",");
                }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String deleteQuery(Object object) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(object.getClass().getSimpleName());
        sb.append(" WHERE ");
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
                if (field.isAnnotationPresent(Primary.class)) {
                    sb.append(field.getName());
                    sb.append("=");
                    sb.append("'");
                    sb.append(field.get(object));
                    sb.append("'");
                    sb.append(";");
                }
        }
        return sb.toString();
    }

    public static String selectAllQuery(Class<?> clazz) {
        Entity entity = clazz.getAnnotation(Entity.class);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(entity.name());
        sb.append(";");
        return sb.toString();
    }

    public static <T, ID> String selectByIdQuery(Class<T> entityClass, ID id) {
        var entity = entityClass.getAnnotation(Entity.class);
        var fields = entityClass.getDeclaredFields();
        var primaryField = "";
        for (var field : fields) {
            if (field.isAnnotationPresent(Primary.class)) {
                primaryField = field.getName();
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(entity.name());
        sb.append(" WHERE ");
        sb.append(primaryField);
        sb.append("=");
        sb.append("'");
        sb.append(id);
        sb.append("'");
        sb.append(";");
        return sb.toString();
    }
}
