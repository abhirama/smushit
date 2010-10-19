/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 18, 2010
 * Time: 10:23:46 PM
 * To change this template use File | Settings | File Templates.
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

  public SmushImages(String rootDirectory, Set<String> acceptedFileExtensions) {
    this.rootDirectory = rootDirectory;
    this.acceptedFileExtensions = acceptedFileExtensions;
  }

  protected final FileFilter myFileFilter = new MyFileFilter(this.acceptedFileExtensions, SmushIt.MAX_FILE_SIZE);
  protected final FileFilter directoryFilter = new DirectoryFilter();

  public void smush() throws IOException {
    this.smushHelper(new File(this.rootDirectory));
  }

  protected void smushHelper(File directory) throws IOException {
    File[] images = directory.listFiles(this.myFileFilter);

    if (images.length > 0) {
      SmushIt smushIt = new SmushIt();
      smushIt.addFiles(this.arrayToList(images));
      List<SmushItResultVo> smushItResultVos = smushIt.smush();

      this.replaceWithSmushedImages(directory, smushItResultVos);
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

    //local copy is made because we do not want to tamper the original list
    List<SmushItResultVo> smushedImages = new LinkedList<SmushItResultVo>();

    for (SmushItResultVo smushItResultVo : smushItResultVos) {
      if (smushItResultVo.getSmushedImageUrl() != null) {
        smushedImages.add(smushItResultVo);
      }
    }

    imageDownloader.download(smushedImages);
  }

  public static void main(String[] args) throws IOException {
    Map<String, String> options = processCommandLineArguments(args);

    Set<String> validFiles = new HashSet<String>();
    validFiles.add("gif");
    validFiles.add("png");
    validFiles.add("jpg");
    validFiles.add("jpeg");

    SmushImages smushImages = new SmushImages(options.get("rootDir"), validFiles);
    smushImages.smush();
  }

  private static class CommandOptionVo {
    String option;
    String value;
  }

  public static Map<String, String> processCommandLineArguments(String[] args) {
    Set<String> acceptedCommandLineOptions = new HashSet<String>();
    acceptedCommandLineOptions.add("rootDir");

    Map<String, String> options = new HashMap<String, String>(args.length);

    for (String arg : args) {
      CommandOptionVo commandOptionVo = convertToCommandOptionVo(arg);
      if (acceptedCommandLineOptions.contains(commandOptionVo.option)) {
        options.put(commandOptionVo.option, commandOptionVo.value);
      }
    }

    return options;
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
    }

    return commandOptionVo;
  }
}
