package com.digitalsingular.wp2jbake;

import org.junit.Test;

import java.io.File;

public class MdWriterTest {

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
        File destination = new File("destination");
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
        sut = new MdWriter(destinationParent.getAbsolutePath() + File.separator + "destination");
    }
}