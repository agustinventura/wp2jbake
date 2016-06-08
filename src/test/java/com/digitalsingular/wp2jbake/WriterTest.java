package com.digitalsingular.wp2jbake;

import org.junit.After;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class WriterTest {
    public static final String DESTINATION = "src/test/destination";

    @After
    public void setUp() throws IOException {
        cleanDestination();
    }

    protected void cleanDestination() throws IOException {
        if (Files.exists(Paths.get(DESTINATION))) {
            Files.walkFileTree(Paths.get(DESTINATION), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}
