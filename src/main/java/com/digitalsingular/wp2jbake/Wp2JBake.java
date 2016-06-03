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
import java.util.HashSet;
import java.util.Set;

public class Wp2JBake {

    private String origin;

    private MdWriter mdWriter;

    public Wp2JBake(String origin, String destination) {
        if (StringUtils.isEmpty(origin) || !existsOrigin(origin)) {
            throw new IllegalArgumentException("Origin is not a valid file");
        } else {
            this.origin = origin;
        }
        this.mdWriter = new MdWriter(destination);
    }

    public Set<File> generateJBakeMarkdown() {
        HashSet<File> exportResult = new HashSet<>();
        XMLEventReader eventReader = getEventReader();
        Post post = null;
        while (eventReader.hasNext()) {
            try {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String name = event.asStartElement().getName().getPrefix() + event.asStartElement().getName().getLocalPart();
                    switch (name) {
                        case "item":
                            post = new Post();
                            break;
                        case "title":
                            if (post != null) {
                                post = post.withTitle(eventReader.nextEvent().asCharacters().getData());
                            }
                            break;
                        case "pubDate":
                            if (post != null) {
                                post = post.withPublishingDate(parsePubDate(eventReader));
                            }
                            break;
                        case "category":
                            if (post != null && isTag(event)) {
                                post = post.withTag(eventReader.nextEvent().asCharacters().getData());
                            }
                            break;
                        case "contentencoded":
                            if (post != null) {
                                post = post.withContent(eventReader.nextEvent().asCharacters().getData());
                            }
                            break;
                        default:
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals("item") && post!=null) {
                        exportResult.add(mdWriter.write(post));
                    }
                }
            } catch (XMLStreamException e) {
                throw new IllegalStateException("Could not read XML: " + e.getMessage());
            }

        }
        return exportResult;
    }

    private boolean isTag(XMLEvent event) {
        return "post_tag".equals(event.asStartElement().getAttributeByName(new QName("domain")).getValue());
    }


    private Date parsePubDate(XMLEventReader eventReader) {
        Date publishingDate = null;
        try {
            String pubDate = eventReader.nextEvent().asCharacters().getData();
            pubDate = pubDate.substring(pubDate.indexOf(",")+2);
            int hourIndex = pubDate.indexOf(":")-3;
            pubDate = pubDate.substring(0, hourIndex);
            pubDate = pubDate.replace("-", "");
            pubDate = pubDate.replace(" ", "-");
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            publishingDate = format.parse(pubDate);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Could not read pubDate: " + e.getMessage());
        } catch (ParseException e) {
            throw new IllegalStateException("Could not parse pubDate: " + e.getMessage());
        }
        return publishingDate;
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

    private boolean existsOrigin(String origin) {
        File originFile = new File(origin);
        String path = originFile.getAbsolutePath();
        return originFile.exists();
    }
}
