package seoul.democracy.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
        return (localDateTime == null ? null : from(localDateTime.atZone(systemDefault()).toInstant()));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date date) {
        return (date == null ? null : ofInstant(date.toInstant(), systemDefault()));
    }
}
