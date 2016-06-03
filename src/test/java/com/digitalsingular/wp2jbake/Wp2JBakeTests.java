package com.digitalsingular.wp2jbake;


import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;


public class Wp2JBakeTests {

    private Wp2JBake sut;

    @Test(expected = IllegalArgumentException.class)
    public void buildWithoutParameters() {
        sut = new Wp2JBake(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildWithoutOrigin() {
        sut = new Wp2JBake(null, "foo");

    }

    @Test(expected = IllegalArgumentException.class)
    public void buildWithEmptyOrigin() {
        sut = new Wp2JBake("", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildWithInvalidOrigin() {
        sut = new Wp2JBake("foo", "");
    }

    @Test
    public void buildWithValidParameters() {
        sut = new Wp2JBake("src/test/resources/wp-source.xml", "src/test/destination");
    }

    @Test
    public void processEmptyXML() {
        sut = new Wp2JBake("src/test/resources/empty.xml", "src/test/destination");
        Set<File> markdowns = sut.generateJBakeMarkdown();
        assertThat(markdowns, is(empty()));
    }

    @Test(expected = IllegalStateException.class)
    public void processInvalidXML() {
        sut = new Wp2JBake("src/test/resources/invalid.xml", "src/test/destination");
        Set<File> markdowns = sut.generateJBakeMarkdown();
    }

    @Test
    public void processXML() {
        sut = new Wp2JBake("src/test/resources/wp-source.xml", "src/test/destination");
        Set<File> markdowns = sut.generateJBakeMarkdown();
        assertThat(markdowns, is(not(empty())));
        for (File markdown: markdowns) {
            assertThat(markdown.exists(), is(true));
        }
        File destination = new File("destination");
        destination.delete();
    }
}
