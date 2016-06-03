package com.digitalsingular.wp2jbake;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class MdWriter {

    private File destinationFolder;

    public MdWriter(String destination) {
        if (StringUtils.isEmpty(destination) || !isWritable(destination)) {
            throw new IllegalArgumentException("Destination is not a valid folder");
        } else {
            destinationFolder = new File(destination);
        }
    }

    private boolean isWritable(String destination) {
        File destinationFolder = new File(destination);
        if (destinationFolder.exists()) {
            return destinationFolder.canWrite();
        } else {
            return isWritableDestinationParent(destinationFolder);
        }
    }

    private boolean isWritableDestinationParent(File destinationFolder) {
        File destinationParent = getDestinationParent(destinationFolder);
        return destinationParent.canWrite();
    }

    private File getDestinationParent(File destinationFolder) {
        String parentPath = destinationFolder.getParent();
        if (parentPath == null) {
            parentPath = "";
        }
        return new File(parentPath);
    }

    public File write(Post post) {
        return null;
    }
}
