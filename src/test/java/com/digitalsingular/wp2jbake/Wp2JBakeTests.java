package com.digitalsingular.wp2jbake;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;


public class Wp2JBakeTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Wp2JBake sut;

    @Test
    public void buildWithoutParameters() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Origin");
        sut = new Wp2JBake(null, null);
    }

    @Test
    public void buildWithoutOrigin() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Origin");
        sut = new Wp2JBake(null, "foo");

    }

    @Test
    public void buildWithoutDestination() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Destination");
        sut = new Wp2JBake("pom.xml", null);

    }

    @Test
    public void buildWithEmptyOrigin() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Origin");
        sut = new Wp2JBake("", "");
    }

    @Test
    public void buildWithEmptyDestination() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Destination");
        sut = new Wp2JBake("pom.xml", "");

    }

    @Test
    public void buildWithInvalidOrigin() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Origin");
        sut = new Wp2JBake("foo", "");
    }

    @Test
    public void buildWithNonWritableDestination() {
        File destination = new File("destination");
        destination.mkdir();
        destination.deleteOnExit();
        destination.setReadOnly();
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Destination");
        sut = new Wp2JBake("pom.xml", destination.getAbsolutePath());
    }

    @Test
    public void buildWithNonWritableDestinationParent() {
        File destinationParent = new File("destinationParent");
        destinationParent.mkdir();
        destinationParent.deleteOnExit();
        destinationParent.setReadOnly();
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Destination");
        sut = new Wp2JBake("pom.xml", destinationParent.getAbsolutePath() + File.separator + "destination");
    }

    @Test
    public void buildWithValidParameters() {
        sut = new Wp2JBake("pom.xml", "destination");
        File destination = new File("destination");
        destination.delete();
    }
}
