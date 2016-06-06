package com.digitalsingular.wp2jbake;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WpReaderTest {

    private WpReader sut;

    @Mock
    private Wp2JBake observer;

    @Test(expected = IllegalArgumentException.class)
    public void readerWithoutOrigin() {
        sut = new WpReader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readerWithEmptyOrigin() {
        sut = new WpReader("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildWithInvalidOrigin() {
        sut = new WpReader("foo");
    }

    @Test(expected = IllegalStateException.class)
    public void readEmptyXML() {
        sut = new WpReader("src/test/resources/empty.xml");
        sut.readPosts(observer);
    }

    @Test(expected = IllegalStateException.class)
    public void readInvalidXML() {
        sut = new WpReader("src/test/resources/invalid.xml");
        sut.readPosts(observer);
    }

    @Test
    public void readValidXML() {
        sut = new WpReader("src/test/resources/wp-source.xml");
        ArgumentCaptor<Post> postCapturer = ArgumentCaptor.forClass(Post.class);
        sut.readPosts(observer);
        verify(observer, times(7)).postRead(postCapturer.capture());
    }
}