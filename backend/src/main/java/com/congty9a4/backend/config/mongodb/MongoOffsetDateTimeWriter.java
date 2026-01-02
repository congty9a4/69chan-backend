package com.congty9a4.backend.config.mongodb;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.util.Date;

// Purpose : Convert an OffsetDateTime object to a MongoDB Document
public class MongoOffsetDateTimeWriter implements Converter<OffsetDateTime, Document> {

    public static final String DATE_FIELD = "dateTime";
    public static final String OFFSET_FIELD = "offset";

    @Override
    public Document convert(final OffsetDateTime source) {
        final Document document = new Document();
        document.put(DATE_FIELD, Date.from(source.toInstant()));
        document.put(OFFSET_FIELD, source.getOffset().getId());
        return document;
    }

}
