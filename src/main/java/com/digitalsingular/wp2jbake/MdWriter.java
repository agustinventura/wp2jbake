package com.digitalsingular.wp2jbake;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;

public class MdWriter {

    public static final String TEMPLATE = "src/main/resources/template.md";
    public static final String TITLE = "$title$";
    public static final String DATE = "$date$";
    public static final String TAGS = "$tags$";
    public static final String CONTENT = "$content$";
    public static final String POST_EXTENSION = ".md";
    public static final String DATE_TITLE_SEPARATOR = "-";

    private String template;

    private String destinationFolder;

    private MdTranslator translator = new MdTranslator();

    public MdWriter(String destinationFolder) {
        if (StringUtils.isEmpty(destinationFolder) || !isWritable(destinationFolder)) {
            throw new IllegalArgumentException("Destination is not a valid folder");
        } else {
            readTemplate();
            this.destinationFolder = destinationFolder;
        }
    }

    private void readTemplate() {
        try {
            template = new String(Files.readAllBytes(Paths.get(TEMPLATE)));
        } catch (IOException e) {
            throw new IllegalStateException("Could not read post template template.md: " + e.getMessage());
        }
    }

    private boolean isWritable(String destination) {
        Path destinationPath = Paths.get(destination);
        if (Files.exists(destinationPath)) {
            return Files.isWritable(destinationPath);
        } else {
            return Files.isWritable(destinationPath.getParent());
        }
    }

    public File write(Post post) {
        validatePost(post);
        Path destinationPath = getDestinationPath(post);
        createDestinationPath(destinationPath);
        String postMarkdown = getPostMarkdown(post);
        try {
            Files.write(destinationPath, postMarkdown.getBytes()
                    , StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new IllegalStateException("Error writing file " + destinationPath.toString() + ": " + e.getLocalizedMessage());
        }
        return destinationPath.toFile();
    }

    private void validatePost(Post post) {
        if (StringUtils.isEmpty(post.getTitle()) || post.getPublishingDate() == null || StringUtils.isEmpty(post.getContent())) {
            throw new IllegalArgumentException();
        }
    }

    private String getPostMarkdown(Post post) {
        String postMarkdown = template.replace(TITLE, post.getTitle());
        postMarkdown = postMarkdown.replace(DATE, translator.translateDate(post.getPublishingDate()));
        postMarkdown = postMarkdown.replace(TAGS, translator.translateTags(post.getTags()));
        postMarkdown = postMarkdown.replace(CONTENT, translator.translateContent(post.getContent()));
        return postMarkdown;
    }

    private void createDestinationPath(Path destinationPath) {
        try {
            if (!Files.exists(destinationPath.getParent())) {
                Files.createDirectories(destinationPath.getParent());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error creating destination path " + destinationPath + ": " + e.getMessage());
        }
    }

    private Path getDestinationPath(Post post) {
        Calendar publishedCalendar = getPublishedCalendar(post);
        Path destinationPath = Paths.get(destinationFolder, Integer.toString(publishedCalendar.get(Calendar.YEAR)),
                Integer.toString(publishedCalendar.get(Calendar.MONTH)), Integer.toString(publishedCalendar.get(Calendar.DAY_OF_MONTH)) +
                        DATE_TITLE_SEPARATOR + post.getTitle() + POST_EXTENSION);
        return destinationPath;
    }

    private Calendar getPublishedCalendar(Post post) {
        Calendar publishedCalendar = Calendar.getInstance();
        publishedCalendar.setTime(post.getPublishingDate());
        return publishedCalendar;
    }
}
