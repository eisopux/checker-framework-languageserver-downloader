package org.checkerframework.languageserver;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import net.lingala.zip4j.ZipFile;

public class CheckerFrameworkDownloader extends BaseDownloader {
    public CheckerFrameworkDownloader(String org, String repo, File folder) {
        super(org, repo, folder);
    }

    private File getUnzipped() throws IOException, URISyntaxException {
        File dest = getDestination();
        String cfzip = dest.getAbsolutePath();
        return Paths.get(FilenameUtils.getFullPath(cfzip), FilenameUtils.getBaseName(cfzip))
                .toFile();
    }

    @Override
    public File download() throws IOException, URISyntaxException {
        File unzipped = getUnzipped();
        if (!unzipped.exists()) {
            File dest = doDownload();
            String cfzip = dest.getAbsolutePath();
            new ZipFile(cfzip).extractAll(folder.getAbsolutePath());
        }
        return unzipped;
    }
}
