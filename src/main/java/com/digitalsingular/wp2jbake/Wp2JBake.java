package com.digitalsingular.wp2jbake;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class Wp2JBake {

    public Wp2JBake(String origin, String destination) {
        if (StringUtils.isEmpty(origin) || !existsOrigin(origin)) {
            throw new IllegalArgumentException("Origin is not a valid file");
        }
        if (StringUtils.isEmpty(destination) || isWritable(destination)) {
            throw new IllegalArgumentException("Destination is not a valid folder");
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

    private boolean existsOrigin(String origin) {
        File originFile = new File(origin);
        String path = originFile.getAbsolutePath();
        return originFile.exists();
    }
}
