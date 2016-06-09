package com.digitalsingular.wp2jbake;


import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class MdTranslator {

    public static final String POST_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TAG_DELIMITER = ",";
    public static final String START_PRE = "<pre[^>]*>";
    public static final String START_PRETTYPRINT = "\n```prettyprint linenums\n";
    public static final String END_PRE = "</pre>";
    public static final String END_PRETTYPRINT = "\n```\n";
    public static final String START_H1 = "<h1>";
    public static final String START_H5 = "\n<h5>";
    public static final String END_H1 = "</h1>";
    public static final String END_H5 = "</h5>\n";
    public static final String START_H2 = "<h2>";
    public static final String START_H6 = "\n<h6>";
    public static final String END_H2 = "</h2>";
    public static final String END_H6 = "</h6>\n";
    public static final String START_H3 = "<h3>";
    public static final String END_H3 = "</h3>";
    public static final String START_WORDPRESS_CAPTION = "\\[caption[^\\]]*\\]";
    public static final String END_WORDPRESS_CAPTION = "\\[/caption\\]";


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
        translatedContent = deleteWordpressImages(translatedContent);
        translatedContent = addNewLinesToBlockElements(translatedContent);
        return translatedContent;
    }

    private String addNewLinesToBlockElements(String translatedContent) {
        String blockElementStart = "<pre[^>]*>";
        return translatedContent;
    }

    private String deleteWordpressImages(String contentToTranslate) {
        String contentWithoutStartingTag = contentToTranslate.replaceAll(START_WORDPRESS_CAPTION, StringUtils.EMPTY);
        String contentWithoutEndingTag = contentWithoutStartingTag.replaceAll(END_WORDPRESS_CAPTION, StringUtils.EMPTY);
        return contentWithoutEndingTag;
    }

    private String translatePre(String contentToTranslate) {
        String contentWithoutStartingPre = contentToTranslate.replaceAll(START_PRE, START_PRETTYPRINT);
        String contentWithoutEndingPre = contentWithoutStartingPre.replaceAll(END_PRE, END_PRETTYPRINT);
        return contentWithoutEndingPre;
    }

    private String translateHeadings(String contentToTranslate) {
        String contentWithoutStartingh1 = contentToTranslate.replace(START_H1, START_H5).replace(START_H1.toUpperCase(), START_H5);
        String contentWithoutEndingh1 = contentWithoutStartingh1.replace(END_H1, END_H5).replace(END_H1.toUpperCase(), END_H5);
        String contentWithoutStartingh2 = contentWithoutEndingh1.replace(START_H2, START_H6).replace(START_H2.toUpperCase(), START_H6);
        String contentWithoutEndingh2 = contentWithoutStartingh2.replace(END_H2, END_H6).replace(END_H2.toUpperCase(), END_H6);
        String contentWithoutStartingh3 = contentWithoutEndingh2.replace(START_H3, START_H6).replace(START_H3.toUpperCase(), START_H6);
        String contentWithoutEndingh3 = contentWithoutStartingh3.replace(END_H3, END_H6).replace(END_H3.toUpperCase(), END_H6);
        return contentWithoutEndingh3;
    }
}
