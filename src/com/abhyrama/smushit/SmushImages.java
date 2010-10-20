/*
 * A command line interface to yahoo!'s smush.it lossless image compression utility - http://www.smushit.com/ysmush.it/
 * http://bitbucket.org/abhirama/smushit
 *
 * Copyright 2010, Abhirama
 * http://abhirama.wordpress.com/
 * You can use this software the way you want as long as you keep this notice.
 */
package com.abhyrama.smushit;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SmushImages {
  protected final String rootDirectory;
  protected final Set<String> acceptedFileExtensions;
  protected boolean verbose;
  protected FileFilter imageFileFilter;
  protected FileFilter directoryFilter;
  protected boolean dryRun;

  protected List<SmushItResultVo> smushItResultVos = new LinkedList<SmushItResultVo>();

  public SmushImages(String rootDirectory, Set<String> acceptedFileExtensions) {
    this.rootDirectory = rootDirectory;
    this.acceptedFileExtensions = acceptedFileExtensions;
  }

  public FileFilter getImageFileFilter() {
    return imageFileFilter;
  }

  public void setImageFileFilter(FileFilter imageFileFilter) {
    this.imageFileFilter = imageFileFilter;
  }

  public FileFilter getDirectoryFilter() {
    return directoryFilter;
  }

  public void setDirectoryFilter(FileFilter directoryFilter) {
    this.directoryFilter = directoryFilter;
  }

  public boolean isVerbose() {
    return verbose;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  public List<SmushItResultVo> smush() throws IOException {
    this.smushHelper(new File(this.rootDirectory));
    return this.smushItResultVos;
  }

  public boolean isDryRun() {
    return dryRun;
  }

  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  protected void smushHelper(File directory) throws IOException {
    if (this.verbose) {
      System.out.println("Smushing files in directory - " + directory.toString());
    }

    File[] images = directory.listFiles(this.imageFileFilter);

    if (images.length > 0) {
      SmushIt smushIt = new SmushIt();
      smushIt.setVerbose(this.verbose);

      smushIt.addFiles(this.arrayToList(images));
      List<SmushItResultVo> smushItResultVos = smushIt.smush();

      this.smushItResultVos.addAll(smushItResultVos);

      if (!this.dryRun) {
        this.replaceWithSmushedImages(directory, smushItResultVos);
      }
    }

    File[] directories = directory.listFiles(this.directoryFilter);

    if (directories.length > 0) {
      for (File ddirectory : directories) {
        this.smushHelper(ddirectory);
      }
    }
  }

  protected List<String> arrayToList(File[] files) {
    List<String> fileNames = new LinkedList<String>();
    for (File file : files) {
      fileNames.add(file.toString());
    }

    return fileNames;
  }

  protected void replaceWithSmushedImages(File directory, List<SmushItResultVo> smushItResultVos) throws IOException {
    ImageDownloader imageDownloader = new ImageDownloader(directory.toString());

    imageDownloader.setVerbose(this.verbose);

    //local copy is made because we do not want to tamper the original list
    List<SmushItResultVo> smushedImages = new LinkedList<SmushItResultVo>();

    for (SmushItResultVo smushItResultVo : smushItResultVos) {
      if (smushItResultVo.getSmushedImageUrl() != null) {
        smushedImages.add(smushItResultVo);
      }
    }

    imageDownloader.download(smushedImages);
  }

  public static final String IMAGE_DIR_COMMAND_LINE_OPTION = "imageDir";
  public static final String VERBOSE_COMMAND_LINE_OPTION = "verbose";
  public static final String DRY_RUN_COMMAND_LINE_OPTION = "dryRun";
  public static final String IMAGE_EXTENSION_COMMAND_LINE_OPTION = "imgExtensions";

  public static final String COMMAND_LINE_OPTION_TRUE = "true";
  public static final String COMMAND_LINE_OPTION_FALSE = "false";

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      printUsageInstructions();
      System.exit(0);
    }

    Map<String, String> options = processCommandLineArguments(args);

    if (!(options.containsKey(IMAGE_DIR_COMMAND_LINE_OPTION))) {
      displayErrorAndExit("\nError:Please specify the directory containing the images to be smushed");
    }

    boolean verbose = false;
    if (options.containsKey(VERBOSE_COMMAND_LINE_OPTION)) {
      String verboseOptionValue = options.get(VERBOSE_COMMAND_LINE_OPTION);

      if (!(COMMAND_LINE_OPTION_TRUE.equals(verboseOptionValue) || COMMAND_LINE_OPTION_FALSE.equals(verboseOptionValue))) {
        displayErrorAndExit("\nError:Verbose option value should be either true or false");
      }

      verbose = Boolean.valueOf(verboseOptionValue);
    }

    boolean dryRun = false;
    if (options.containsKey(DRY_RUN_COMMAND_LINE_OPTION)) {
      String dryRunOptionValue = options.get(DRY_RUN_COMMAND_LINE_OPTION);

      if (!(COMMAND_LINE_OPTION_TRUE.equals(dryRunOptionValue) || COMMAND_LINE_OPTION_FALSE.equals(dryRunOptionValue))) {
        displayErrorAndExit("\nError:Dry run option value should be either true or false");
      }

      dryRun = Boolean.valueOf(dryRunOptionValue);
    }

    String rootDirectory = options.get(IMAGE_DIR_COMMAND_LINE_OPTION);
    if (!(new File(rootDirectory).exists())) {
      displayErrorAndExit("\nError:Specified directory does not exist");
    }

    String passedImageExtensions = options.get(IMAGE_EXTENSION_COMMAND_LINE_OPTION);
    Set<String> validFiles = new HashSet<String>();
    if (passedImageExtensions != null && !passedImageExtensions.equals("")) {
      validFiles = getPassedImageExtensions(passedImageExtensions);
    } else {
      validFiles.add("gif");
      validFiles.add("png");
      validFiles.add("jpg");
      validFiles.add("jpeg");
    }

    SmushImages smushImages = new SmushImages(options.get(IMAGE_DIR_COMMAND_LINE_OPTION), validFiles);
    smushImages.setVerbose(verbose);
    smushImages.setDryRun(dryRun);
    smushImages.setImageFileFilter(new MyFileFilter(validFiles, SmushIt.MAX_FILE_SIZE));
    smushImages.setDirectoryFilter(new DirectoryFilter());
    List<SmushItResultVo> smushItResultVos = smushImages.smush();

    SmushStats smushStats = new SmushStats(smushItResultVos);
    SmushStatsVo smushStatsVo = smushStats.getSmushStats();

    System.out.println("\n-------------------------------------");
    System.out.println("Total images uploaded - " + smushStatsVo.getTotalUploadedImagesCount());
    System.out.println("Total images smushed - " + smushStatsVo.getSmushedImagesCount());
    System.out.println("Total uploaded images size - " + smushStatsVo.getTotalUploadedImagesSize() + " bytes");
    System.out.println("Total smushed image size - " + smushStatsVo.getTotalSmushedImagesSize() + " bytes");
    System.out.println("Percentage saving - " + smushStatsVo.calculatePercentageSaving() + "%");
    System.out.println("-------------------------------------");
  }

  protected static Set<String> getPassedImageExtensions(String arg) {
    String[] imageExtensions = arg.split(",");
    Set<String> passedImageExtensions = new HashSet<String>(imageExtensions.length);

    for (String imageExtension : imageExtensions) {
      passedImageExtensions.add(imageExtension.trim());
    }

    return passedImageExtensions;
  }

  private static class CommandOptionVo {
    String option;
    String value;
  }

  public static Map<String, String> processCommandLineArguments(String[] args) {
    Set<String> acceptedCommandLineOptions = new HashSet<String>();
    acceptedCommandLineOptions.add(IMAGE_DIR_COMMAND_LINE_OPTION);
    acceptedCommandLineOptions.add(VERBOSE_COMMAND_LINE_OPTION);
    acceptedCommandLineOptions.add(IMAGE_EXTENSION_COMMAND_LINE_OPTION);
    acceptedCommandLineOptions.add(DRY_RUN_COMMAND_LINE_OPTION);

    Map<String, String> options = new HashMap<String, String>(args.length);

    for (String arg : args) {
      CommandOptionVo commandOptionVo = convertToCommandOptionVo(arg);
      if (acceptedCommandLineOptions.contains(commandOptionVo.option)) {
        options.put(commandOptionVo.option, commandOptionVo.value);
      } else {
        displayErrorAndExit("\nError:You passed an option which the program does not recognize");
      }
    }

    return options;
  }

  protected static void displayErrorAndExit(String errorMessage) {
    System.out.println(errorMessage);
    printUsageInstructions();
    System.exit(0);
  }

  protected static void printUsageInstructions() {
    String usage = "\nUsage: java -jar smushit.jar [options]" +
        "\n\n" +
        "Compulsory Option" +
        "\n" +
        " -imageDir                       Directory of the images to be smushed" +
        "\n\n" +
        "Optional Option" +
        "\n" +
        " -verbose <true|false>           Display informational messages\n" +
        " -dryRun <true|false>            Whether smushed images should be downloaded or not\n" +
        " -imgExtensions                  Comma separated image extension. If none provided assumes png, gif, jpg/jpeg" +
        "\n\nExample usage: " +
        "\njava -jar smushit.jar -imageDir=/foo -verbose=true -imgExtensions=gif,png,jpeg";

    System.out.println(usage);
  }

  protected static CommandOptionVo convertToCommandOptionVo(String arg) {
    String stringPattern = "^-(.*?)=(.*?)$"; //corresponds to a pattern of the format -rootDirectory=C://foo

    Pattern pattern = Pattern.compile(stringPattern);
    Matcher matcher = pattern.matcher(arg);

    CommandOptionVo commandOptionVo = null;
    if (matcher.find()) {
      commandOptionVo = new CommandOptionVo();
      commandOptionVo.option = matcher.group(1);
      commandOptionVo.value = matcher.group(2);
    } else {
      displayErrorAndExit("\nError:The way you specified options is not correct");
    }

    return commandOptionVo;
  }
}
