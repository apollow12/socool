package main.crawler;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;

/**
 * Use NIO to explore directory
 */
public class DirExplorer {

    /*
     * using new IO
     */
    private long count_file_nio = 0;
    private long count_dir_nio = 0;
    private long startTime_nio = 0;
    private long endTime_nio = 0;

    public DirExplorer() {

    }

    public long getCount_file() {
        return count_file_nio;
    }

    public long getCount_dir() {
        return count_dir_nio;
    }

    public long timespent() {
        return endTime_nio - startTime_nio;
    }

    /**
     *
     * @param filePath the path to explore
     * @param excludeDirPaths the paths under path that to be excluded
     */
    public void startExploreNIO(String filePath, String[] excludeDirPaths) {
        startTime_nio = System.currentTimeMillis();

        System.out.println("Visit dir: " + filePath);

        final HashSet<String> excludedDirSet = new HashSet<String>();
        for (String excluded : excludeDirPaths) {
            excludedDirSet.add(excluded);
            System.out.println("Exclude dir: " + excluded);
        }

        SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

            /**
             * Run when visiting a file
             * @param file
             * @param attrs
             * @return
             * @throws IOException
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                File curFile = file.toFile();

                visitFile_NIO(curFile);
                return super.visitFile(file, attrs);
            }

            /**
             * Run this when visiting a dir
             * @param dir
             * @param attrs
             * @return
             * @throws IOException
             */
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

//                System.out.println(attrs.lastModifiedTime());

                if (excludedDirSet.contains(dir.toString())) {
                    System.out.println("===== Skip dir: " + dir.toString());
                    return FileVisitResult.SKIP_SUBTREE;
                }

                count_dir_nio ++;
                if (count_dir_nio % 100 == 0) {
                    System.out.println("Cur dir: " + dir.toString());
                }

                return super.preVisitDirectory(dir, attrs);
            }
        };

        Path path = Paths.get(filePath);
        try {
            java.nio.file.Files.walkFileTree(path, visitor);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        endTime_nio = System.currentTimeMillis();
    }

    private void visitFile_NIO(File file) {
        count_file_nio ++;
        if (count_file_nio % 1000 == 0) {
            System.out.print(".");
        }
        if (count_file_nio % 10000 == 0) {
            System.out.println("Find " + count_file_nio + " files.");
        }
    }
}
