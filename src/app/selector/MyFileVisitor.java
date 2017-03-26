package app.selector;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class MyFileVisitor implements FileVisitor<Path> {

  private final ImageSelectorController controller;

  MyFileVisitor(ImageSelectorController controller) {
    this.controller = controller;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path dir, BasicFileAttributes attrs) throws IOException {
    controller.setFilePath(dir.getFileName().toString());
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path dir, IOException e) throws IOException {
    return FileVisitResult.CONTINUE;
  }

}
