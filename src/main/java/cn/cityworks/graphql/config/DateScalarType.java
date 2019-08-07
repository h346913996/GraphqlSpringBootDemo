package cn.cityworks.graphql.config;

import cn.cityworks.graphql.utils.DateUtil;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateScalarType extends GraphQLScalarType {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public DateScalarType() {
        super("Date", "Date value", new Coercing<Date, String>() {
            @Override
            public String serialize(Object o) {
                Date date = (Date) o;
                return DateUtil.format(date, DATE_FORMAT);
            }

            @Override
            public Date parseValue(Object o) {
                String value = String.valueOf(o);
                if ("null".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
                    return null;
                }
                return DateUtil.parseDate(value, DATE_FORMAT);
            }

            @Override
            public Date parseLiteral(Object o) {
                String value = String.valueOf(o);
                if ("null".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
                    return null;
                }
                return DateUtil.parseDate(value, DATE_FORMAT);
            }
        });
    }

}
