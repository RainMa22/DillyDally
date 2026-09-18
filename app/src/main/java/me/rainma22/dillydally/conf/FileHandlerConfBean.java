package me.rainma22.dillydally.conf;

import me.rainma22.dillydally.abstracts.Bean;

/**
 * FileHandlerConfBean
 */
public class FileHandlerConfBean extends Bean {
    private String directoryPath = ".";

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

}
