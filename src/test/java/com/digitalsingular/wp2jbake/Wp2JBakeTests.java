package com.digitalsingular.wp2jbake;


import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;


public class Wp2JBakeTests extends WriterTest {

    public static final String POSTS_SOURCE = "src/test/resources/wp-source.xml";
    public static final String EMPTY_POSTS_SOURCE = "src/test/resources/empty.xml";
    public static final String INVALID_POSTS_SOURCE = "src/test/resources/invalid.xml";
    public static final String FOO = "foo";
    private Wp2JBake sut;

    @Test(expected = IllegalArgumentException.class)
    public void buildWithoutParameters() {
        sut = new Wp2JBake(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildWithoutOrigin() {
        sut = new Wp2JBake(null, FOO);

    }

    @Test(expected = IllegalArgumentException.class)
    public void buildWithEmptyOrigin() {
        sut = new Wp2JBake(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildWithInvalidOrigin() {
        sut = new Wp2JBake(FOO, StringUtils.EMPTY);
    }

    @Test
    public void buildWithValidParameters() {
        sut = new Wp2JBake(POSTS_SOURCE, DESTINATION);
    }

    @Test(expected = IllegalStateException.class)
    public void processEmptyXML() {
        sut = new Wp2JBake(EMPTY_POSTS_SOURCE, DESTINATION);
        Set<File> markdowns = sut.generateJBakeMarkdown();
    }

    @Test(expected = IllegalStateException.class)
    public void processInvalidXML() {
        sut = new Wp2JBake(INVALID_POSTS_SOURCE, DESTINATION);
        Set<File> markdowns = sut.generateJBakeMarkdown();
    }

    @Test
    public void processXML() {
        sut = new Wp2JBake(POSTS_SOURCE, DESTINATION);
        Set<File> markdowns = sut.generateJBakeMarkdown();
        assertThat(markdowns.size(), is(5));
        assertThat(markdowns, is(not(empty())));
        for (File markdown: markdowns) {
            assertThat(markdown.exists(), is(true));
        }
    }
}
