package com.digitalsingular.wp2jbake;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Wp2JBake {

    private WpReader wpReader;

    private MdWriter mdWriter;

    private HashSet<File> exportResult;

    public Wp2JBake(String origin, String destination) {
        this.wpReader = new WpReader(origin);
        this.mdWriter = new MdWriter(destination);
    }

    public Set<File> generateJBakeMarkdown() {
        exportResult = new HashSet<>();
        wpReader.readPosts(this);
        return exportResult;
    }

    public void postRead(Post post) {
        exportResult.add(mdWriter.write(post));
    }
}
