package com.digitalsingular.wp2jbake;

import org.apache.commons.lang3.StringUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WpReader {

    public static final String ITEM = "item";
    public static final String TITLE = "title";
    public static final String PUB_DATE = "pubDate";
    public static final String CATEGORY = "category";
    public static final String CONTENT = "contentencoded";
    public static final String POST_TAG = "post_tag";
    public static final String DOMAIN = "domain";
    private String origin;

    public WpReader(String origin) {
        if (StringUtils.isEmpty(origin) || !existsOrigin(origin)) {
            throw new IllegalArgumentException("Origin is not a valid file");
        } else {
            this.origin = origin;
        }
    }

    private boolean existsOrigin(String origin) {
        File originFile = new File(origin);
        return originFile.exists();
    }

    public void readPosts(Wp2JBake wp2JBake) {
        XMLEventReader eventReader = getEventReader();
        try {
            readXML(wp2JBake, eventReader);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Error reading XML " + origin + ": " + e.getMessage());
        }
    }

    private void readXML(Wp2JBake wp2JBake, XMLEventReader eventReader) throws XMLStreamException {
        while (eventReader.hasNext()) {
            readElement(wp2JBake, eventReader);
        }
    }

    private void readElement(Wp2JBake wp2JBake, XMLEventReader eventReader) throws XMLStreamException {
        XMLEvent event = eventReader.nextEvent();
        if (isPostStart(event)) {
            Post post = readPost(eventReader);
            wp2JBake.postRead(post);
        }
    }

    private Post readPost(XMLEventReader eventReader) throws XMLStreamException {
        Post exportedPost = new Post();
        boolean postRead = false;
        while (!postRead && eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                exportedPost = loadAttribute(event, eventReader, exportedPost);
            } else if (isPostEnd(event)) {
                postRead = true;
            }
        }
        return exportedPost;
    }

    private boolean isPostEnd(XMLEvent event) {
        return event.isEndElement() && ITEM.equals(event.asEndElement().getName().getPrefix() + event.asEndElement().getName().getLocalPart());
    }

    private Post loadAttribute(XMLEvent event, XMLEventReader eventReader, Post post) {
        String name = getEventFullName(event);
        try {
            post = loadAttribute(event, eventReader, post, name);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Error parsing " + name + ": " + e.getMessage());
        }
        return post;
    }

    private Post loadAttribute(XMLEvent event, XMLEventReader eventReader, Post post, String name) throws XMLStreamException {
        switch (name) {
            case TITLE:
                post = loadTitle(eventReader, post);
                break;
            case PUB_DATE:
                post = loadPublishingDate(eventReader, post);
                break;
            case CATEGORY:
                if (isTag(event)) {
                    post = loadCategory(eventReader, post);
                }
                break;
            case CONTENT:
                post = loadContent(eventReader, post);
                break;
            default:
                break;
        }
        return post;
    }

    private Post loadContent(XMLEventReader eventReader, Post post) throws XMLStreamException {
        return post.withContent(eventReader.nextEvent().asCharacters().getData());
    }

    private Post loadCategory(XMLEventReader eventReader, Post post) throws XMLStreamException {
        return post.withTag(eventReader.nextEvent().asCharacters().getData());
    }

    private Post loadPublishingDate(XMLEventReader eventReader, Post post) throws XMLStreamException {
        return post.withPublishingDate(parsePubDate(eventReader));
    }

    private Post loadTitle(XMLEventReader eventReader, Post post) throws XMLStreamException {
        return post.withTitle(eventReader.nextEvent().asCharacters().getData());
    }

    private boolean isTag(XMLEvent event) {
        return POST_TAG.equals(event.asStartElement().getAttributeByName(new QName(DOMAIN)).getValue());
    }


    private Date parsePubDate(XMLEventReader eventReader) throws XMLStreamException {
        Date publishingDate = null;
        try {
            String pubDate = eventReader.nextEvent().asCharacters().getData();
            pubDate = extractDate(pubDate);
            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
            publishingDate = format.parse(pubDate);
        } catch (ParseException e) {
            throw new IllegalStateException("Could not parse pubDate: " + e.getMessage());
        }
        return publishingDate;
    }

    private String extractDate(String pubDate) {
        //Date is supplied as this: Wed, 30 Nov -0001 00:00:00 +0000 (RFC822 presumably), we need to extract just the date
        pubDate = pubDate.substring(pubDate.indexOf(",")+2);
        int hourIndex = pubDate.indexOf(":")-3;
        pubDate = pubDate.substring(0, hourIndex);
        return pubDate;
    }

    private boolean isPostStart(XMLEvent event) {
        return event.isStartElement() && ITEM.equals(getEventFullName(event));
    }

    private String getEventFullName(XMLEvent event) {
        return event.asStartElement().getName().getPrefix() + event.asStartElement().getName().getLocalPart();
    }

    private XMLEventReader getEventReader() {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = null;
        XMLEventReader eventReader = null;
        try {
            in = new FileInputStream(origin);
            eventReader = inputFactory.createXMLEventReader(in);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not find origin file: " + e.getMessage());
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Could not read origin file: " + e.getMessage());
        }
        return eventReader;
    }
}
