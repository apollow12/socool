package main.crawler.crawledfile;

/**
 * Stores the information of a file discovered by Crawler
 * InfoL
 * 1. Meta data:
 *      file path
 *      file name
 *      ext name
 *      size
 *      author
 *      creation/access/modification time
 *      isSymbolicFile
 *      isVisible
 *      type (text, source code, pic, doc etc.)
 *
 * 2. file content (or cache?)
 *      Store content: plain text <= 5MB (make it configurable?)
 */
public class CrawledFile {

    String path;
    String name;
    String extName;
    float sizeInBytes;
    String author;
    long creationTime;
    long lastAccessTime;
    long modificationTime;
    boolean isSymbolicFile;
    boolean isVisible;
    // type

    String content;
}
