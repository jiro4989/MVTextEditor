package com.jiro4989.mvte.selector;

import java.nio.file.*;

class ImgPath {
  final String absPath;
  final String fileName;

  ImgPath(Path path) {
    this.absPath = path.toString();
    fileName = path.getFileName().toString();
  }

  @Override
  public String toString() {
    return fileName;
  }
}
