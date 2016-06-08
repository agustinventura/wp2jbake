package com.digitalsingular.wp2jbake;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MdWriterTest extends WriterTest {

    public static final String POST_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TEST_POST_CONTENT = "content";
    public static final String TEST_POST_TITLE = "title";
    public static final Date TEST_POST_DATE = new Date();
    public static final String POST = "post";
    public static final String EMPTY_TAGS = "";
    public static final String PUBLISHED = "published";
    public static final String METADATA_SEPARATOR = "~~~~~~";

    private MdWriter sut;

    @Test(expected = IllegalArgumentException.class)
    public void writerWithoutDestination() {
        sut = new MdWriter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writerWithEmptyDestination() {
        sut = new MdWriter("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void writerWithNonWritableDestination() {
        File destination = new File("DESTINATION");
        destination.mkdir();
        destination.deleteOnExit();
        destination.setReadOnly();
        sut = new MdWriter(destination.getAbsolutePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void writerWithNonWritableDestinationParent() {
        File destinationParent = new File("destinationParent");
        destinationParent.mkdir();
        destinationParent.deleteOnExit();
        destinationParent.setReadOnly();
        sut = new MdWriter(destinationParent.getAbsolutePath() + File.separator + "DESTINATION");
    }

    @Test
    public void writerWithValidDestination() {
        sut = new MdWriter(DESTINATION);
    }

    @Test(expected =  IllegalArgumentException.class)
    public void writeEmptyPost() {
        sut = new MdWriter(DESTINATION);
        sut.write(new Post());
        new File(DESTINATION).delete();
    }

    @Test
    public void writePost() throws IOException {
        sut = new MdWriter(DESTINATION);
        Post post = new Post().withContent(TEST_POST_CONTENT).withTitle(TEST_POST_TITLE).withPublishingDate(TEST_POST_DATE);
        File postFile = sut.write(post);
        assertThat(postFile, notNullValue());
        List<String> lines = Files.readAllLines(Paths.get(postFile.getPath()));
        assertThat(getValue(lines.get(0)), is(post.getTitle()));
        assertThat(getValue(lines.get(1)), is(getPostDate(post)));
        assertThat(getValue(lines.get(2)), is(POST));
        assertThat(getValue(lines.get(3)), is(EMPTY_TAGS));
        assertThat(getValue(lines.get(4)), is(PUBLISHED));
        assertThat(getValue(lines.get(5)), is(METADATA_SEPARATOR));
        assertThat(lines.get(6), is(post.getContent()));
        cleanDestination();
    }

    private String getPostDate(Post post) {
        DateFormat formatter = new SimpleDateFormat(POST_DATE_FORMAT);
        return formatter.format(post.getPublishingDate());
    }

    private String getValue(String line) {
        int valueStart = line.indexOf("=")+1;
        return line.substring(valueStart);
    }

}