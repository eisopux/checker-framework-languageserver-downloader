package org.checkerframework.languageserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseDownloader {
    protected final String org;
    protected final String repo;
    protected final File folder;

    protected BaseDownloader(String org, String repo, File folder) {
        this.org = org;
        this.repo = repo;
        this.folder = folder;
    }

    public File download() throws IOException {
        if (!exists()) {
            return doDownload();
        }
        return getDestination();
    }

    public boolean exists() throws IOException {
        return getDestination().exists();
    }

    public URL getLatestGitHubReleaseURL() throws MalformedURLException {
        return new URL("https://api.github.com/repos/" + org + "/" + repo + "/releases/latest");
    }

    public JsonElement getLatestGitHubRelease() throws IOException {
        String json = IOUtils.toString(getLatestGitHubReleaseURL(), StandardCharsets.UTF_8.name());
        return JsonParser.parseString(json);
    }

    public URL getLatestGitHubReleaseAsset() throws IOException {
        JsonElement root = getLatestGitHubRelease();
        JsonArray assets = root.getAsJsonObject().getAsJsonArray("assets");
        return new URL(assets.get(0).getAsJsonObject().get("browser_download_url").getAsString());
    }

    // returns the File object corresponding to a file if it's successful downloaded
    public File getDestination() throws IOException {
        Path dest =
                Paths.get(
                        folder.getAbsolutePath(),
                        FilenameUtils.getName(getLatestGitHubReleaseAsset().getPath()));
        return dest.toFile();
    }

    protected File doDownload() throws IOException {
        URL assetURL = getLatestGitHubReleaseAsset();
        File dest = getDestination();
        System.out.printf("Downloading from %s to %s\n", assetURL.toString(), dest.toString());
        FileUtils.copyURLToFile(assetURL, dest);
        return dest;
    }
}
