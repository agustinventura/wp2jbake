package com.digitalsingular.wp2jbake;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class MdTranslator {

    public static final String POST_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TAG_DELIMITER = ",";
    public static final String START_PRE = "<pre[^>]*>";
    public static final String START_PRETTYPRINT = "\n```prettyprint\n";
    public static final String END_PRE = "</pre>";
    public static final String END_PRETTYPRINT = "\n```\n";
    public static final String START_H1 = "<h1>";


    public String translateDate(Date dateToTranslate) {
        DateFormat formatter = new SimpleDateFormat(POST_DATE_FORMAT);
        return formatter.format(dateToTranslate);
    }

    public String translateTags(Collection<String> tagsToTranslate) {
        return tagsToTranslate.stream().map(Object::toString).collect(Collectors.joining(TAG_DELIMITER));
    }

    public String translateContent(String contentToTranslate) {
        String translatedContent = translatePre(contentToTranslate);
        translatedContent = translateHeadings(translatedContent);
        return translatedContent;
    }

    private String translatePre(String contentToTranslate) {
        String contentWithoutStartingPre = contentToTranslate.replaceAll(START_PRE, START_PRETTYPRINT);
        String contentWithoutEndingPre = contentWithoutStartingPre.replaceAll(END_PRE, END_PRETTYPRINT);
        return contentWithoutEndingPre;
    }

    private String translateHeadings(String contentToTranslate) {
        String contentWithoutStartingh1 = contentToTranslate.replace(START_H1, "<h5>");
        String contentWithoutEndingh1 = contentWithoutStartingh1.replace("</h1>", "</h5>");
        String contentWithoutStartingh2 = contentWithoutEndingh1.replace("<h2>", "<h6>");
        String contentWithoutEndingh2 = contentWithoutStartingh2.replace("</h2>", "</h6>");
        String contentWithoutStartingh3 = contentWithoutEndingh2.replace("<h3>", "<h7>");
        String contentWithoutEndingh3 = contentWithoutStartingh3.replace("</h3>", "</h7>");
        return contentWithoutEndingh3;
    }
}
