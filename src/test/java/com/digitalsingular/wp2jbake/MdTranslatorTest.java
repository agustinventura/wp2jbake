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
    public static final String TEST_PRE_CONTENT = "<pre lang=\"java\"> java content </pre>";
    public static final String TEST_PRE_RESULT = "\n```prettyprint linenums\n java content \n```\n";
    public static final String TEST_HEADING_CONTENT = "<h1> Title </h1><H2> Subtitle </H2>";
    public static final String TEST_HEADING_RESULT = "\n<h5> Title </h5>\n\n<h6> Subtitle </h6>\n";
    public static final String TEST_WORDPRESS_TAGS_CONTENT = "[caption id='image'] image [/caption]";
    public static final String TEST_WORDPRESS_TAGS_RESULT = " image ";

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

    @Test
    public void translateContentWithWordpressTags() {
        String translatedContent = sut.translateContent(TEST_WORDPRESS_TAGS_CONTENT);
        assertThat(translatedContent, is(TEST_WORDPRESS_TAGS_RESULT));
    }

    @Test
    public void translateContentWithBlockElements() {
        String translatedContent = sut.translateContent("<ul><li></li></ul><div><p></p></div>");
        assertThat(translatedContent, is("\n<ul><li></li></ul>\n\n<div>\n<p></p>\n</div>\n"));
    }
}