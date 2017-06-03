package main.crawler;

import main.log.SCLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * The explore find all files under a given path (with excluding paths).
 * It puts the list of found files into a queue, to be consumed by file readers.
 */
public class DirectoryExplorer extends StatefulStoppableRunner implements Runnable {

    /**
     * lock of super.mState
     */
    private Object mStateLock = new Object();

    /**
     * Queue containing paths to found files
     */
    private BlockingQueue<String> mExploredFileQueue;
    private ExecutorService mExecuterService;

    /**
     * Controlling messages in queue
     */
    public static final String STOP_EXPLORER = "STOP_EXPLORER";

    /**
     * Paths for explore
     */
    private String mExploreRootPath;
    private String[] mExcludedPaths;

    /**
     * Statistics
     */
    private long mCountFile = 0;
    private long mCountDir = 0;
    private long mStartTimestamp = 0;
    private long mEndTimestamp = 0;




    //region Instance

    public DirectoryExplorer(String rootPath, String[] excludedPaths, BlockingQueue<String> filePathsQueue) {
        mExploreRootPath = rootPath;
        mExcludedPaths = excludedPaths;
        mExploredFileQueue = filePathsQueue;
    }
    //endregion


    //region Controls

    public void start() {
        synchronized (mStateLock) {
            if (!canStart()) {
                return;
            }



            mExecuterService.execute(this);

            super.changeStateToRunning();
        }
    }

    @Override
    public void stop() {
        synchronized (mStateLock) {
            if (!canStop()) {
                return;
            }

            mExploredFileQueue.offer(STOP_EXPLORER);

            super.changeStateToStopped();
        }
    }

    @Override
    void restart() {
        synchronized (mStateLock) {
            if (!canRestart()) {
                return;
            }

            // TODO

            super.changeStateToRunning();
        }
    }
    //endregion


    //region Business logic

    /**
     * The running procedures of the directory explorer
     */
    @Override
    public void run() {
        while (true) {
            try {
                // wait indefinitely if queue is empty
                String filePath = this.mExploredFileQueue.take();

                if (filePath.equals(STOP_EXPLORER)) {
                    // stop run()
                    return;
                } else {
                    // process a file path
                }

                // TODO

            } catch (InterruptedException ie) {
                SCLogger.getInstance().logError(ie.getLocalizedMessage());
            }
        }
    }

    /**
     * Use Java NIO to explore a given path
     * @param filePath the path to explore
     * @param excludeDirPaths the paths under path that to be excluded
     */
    public void doExploreNIO(String filePath, String[] excludeDirPaths) {
        mStartTimestamp = System.currentTimeMillis();

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

                mCountDir ++;
                if (mCountDir % 100 == 0) {
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

        mEndTimestamp = System.currentTimeMillis();
    }

    private void visitFile_NIO(File file) {
        mCountFile ++;
        if (mCountFile % 1000 == 0) {
            System.out.print(".");
        }
        if (mCountFile % 10000 == 0) {
            System.out.println("Find " + mCountFile + " files.");
        }
    }
    //endregion
}
