package org.checkerframework.languageserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.File;

public class DownloaderMain {
    private static final String OPT_DESTINATION = "dest";
    private static final String OPT_CHECKERFRAMEWORK_ORG = "checkerframework_org";
    private static final String OPT_CHECKERFRAMEWORK_REPO = "checkerframework_repo";
    private static final String OPT_LANGUAGESERVER_ORG = "languageserver_org";
    private static final String OPT_LANGUAGESERVER_REPO = "languageserver_repo";

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(getOptions(), args);

        final String dir = cmd.getOptionValue(OPT_DESTINATION);
        final String cf_org, cf_repo, ls_org, ls_repo;

        if (cmd.hasOption(OPT_CHECKERFRAMEWORK_ORG)) {
            cf_org = cmd.getOptionValue(OPT_CHECKERFRAMEWORK_ORG);
        } else {
            cf_org = "eisop";
        }
        if (cmd.hasOption(OPT_CHECKERFRAMEWORK_REPO)) {
            cf_repo = cmd.getOptionValue(OPT_CHECKERFRAMEWORK_REPO);
        } else {
            cf_repo = "checker-framework";
        }
        if (cmd.hasOption(OPT_LANGUAGESERVER_ORG)) {
            ls_org = cmd.getOptionValue(OPT_LANGUAGESERVER_ORG);
        } else {
            ls_org = "eisopux";
        }
        if (cmd.hasOption(OPT_LANGUAGESERVER_REPO)) {
            ls_repo = cmd.getOptionValue(OPT_LANGUAGESERVER_REPO);
        } else {
            ls_repo = "checker-framework-languageserver";
        }

        File folder = new File(dir);
        if (!folder.isDirectory()) {
            System.err.println("The path provided is not a folder: " + dir);
        }

        /*
        Note that other tools, e.g. the VS Code CF plugin, depend on this output.
        Any changes here should be documented in the README.
        */
        BaseDownloader d = new LanguageServerDownloader(ls_org, ls_repo, folder);
        File f = d.download();
        System.out.println("Got language server: " + f.getAbsolutePath());

        d = new CheckerFrameworkDownloader(cf_org, cf_repo, folder);
        f = d.download();
        System.out.println("Got Checker Framework: " + f.getAbsolutePath());
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addRequiredOption(
                OPT_DESTINATION, OPT_DESTINATION, true, "directory to place downloaded files");
        options.addOption(
                OPT_CHECKERFRAMEWORK_ORG,
                OPT_CHECKERFRAMEWORK_ORG,
                true,
                "the organization of Checker Framework to download; default to eisop");
        options.addOption(
                OPT_CHECKERFRAMEWORK_REPO,
                OPT_CHECKERFRAMEWORK_REPO,
                true,
                "the repository of Checker Framework to download; default to checker-framework");
        options.addOption(
                OPT_LANGUAGESERVER_ORG,
                OPT_LANGUAGESERVER_ORG,
                true,
                "the organization of language server to download; default to eisopux");
        options.addOption(
                OPT_LANGUAGESERVER_REPO,
                OPT_LANGUAGESERVER_REPO,
                true,
                "the repository of language server to download; default to checker-framework-languageserver");
        return options;
    }
}
