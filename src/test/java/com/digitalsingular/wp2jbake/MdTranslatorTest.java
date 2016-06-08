package com.digitalsingular.wp2jbake;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MdTranslatorTest {

    public static final String TAG_1 = "tag 1";
    public static final String TAG_2 = "tag 2";
    public static final String TEST_DATE = "2016-01-01";
    public static final String TEST_CONTENT = "content";
    public static final String TEST_PRE_CONTENT = "<pre lang=\"java\"> contenido java </pre>";
    public static final String TEST_PRE_RESULT = "\n```prettyprint\n contenido java \n```\n";
    private static final String TEST_HEADING_CONTENT = "<h1> Titulo </h1>";
    private static final String TEST_HEADING_RESULT = "<h5> Titulo </h5>";

    private MdTranslator sut = new MdTranslator();

    @Test
    public void translateDate() throws Exception {
        String dateAsString = TEST_DATE;
        SimpleDateFormat formatter = new SimpleDateFormat(MdTranslator.POST_DATE_FORMAT);
        Date date = formatter.parse(dateAsString);
        String translatedDate = sut.translateDate(date);
        assertThat(translatedDate, is(dateAsString));
    }

    @Test
    public void translateTags() {
        List<String> tags = new ArrayList<>(2);
        tags.add(TAG_1);
        tags.add(TAG_2);
        String translatedTags = sut.translateTags(tags);
        assertThat(translatedTags, is(TAG_1 + MdTranslator.TAG_DELIMITER + TAG_2));
    }

    @Test
    public void translateContent() {
        String translatedContent = sut.translateContent(TEST_CONTENT);
        assertThat(translatedContent, is(TEST_CONTENT));
    }

    @Test
    public void translateContentWithPre() {
        String translatedContent = sut.translateContent(TEST_PRE_CONTENT);
        assertThat(translatedContent, is(TEST_PRE_RESULT));
    }

    @Test
    public void translateContentWithHeading() {
        String translatedContent = sut.translateContent(TEST_HEADING_CONTENT);
        assertThat(translatedContent, is(TEST_HEADING_RESULT));
    }
}