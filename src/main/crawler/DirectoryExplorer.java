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
import java.util.concurrent.Executors;

/**
 * The explore find all files under a given path (with excluding paths).
 * It puts the list of found files into a queue, to be consumed by file readers.
 */
public class DirectoryExplorer extends StatefulStoppableRunner implements Runnable {

    SCLogger LOG = SCLogger.getInstance();

    /**
     * lock of super.mState
     */
    private Object mStateLock = new Object();

    private ExecutorService mExecuterService;

    /**
     * Paths for explore
     */
    private String mExploreRootPath;
    private String[] mExcludedPaths;

    /**
     * Discovered file paths
     */
    private BlockingQueue<String> mDiscoveredFilePathsQueue;

    /**
     * Statistics
     */
    private long mCountFile = 0;
    private long mCountDir = 0;
    private long mStartTimestamp = 0;
    private long mEndTimestamp = 0;

    public long getFileCount() {
        return mCountFile;
    }

    public long getDirCount() {
        return mCountDir;
    }

    public long timespent() {
        return mEndTimestamp - mStartTimestamp;
    }

    //region Instance

    /**
     * Set up a directory explore. It will find all files under the rootPath and
     * given the file paths to the file reader(s) to do actual content crawling.
     * @param rootPath the path to explore
     * @param excludedPaths the paths that the explore won't explore
     */
    public DirectoryExplorer(String rootPath, String[] excludedPaths, BlockingQueue<String> outputPathQueue) {
        mExploreRootPath = rootPath;
        mExcludedPaths = excludedPaths;

        mDiscoveredFilePathsQueue = outputPathQueue;

        mExecuterService = Executors.newSingleThreadExecutor();
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

            super.changeStateToStopped();
        }
        mExecuterService.shutdown();
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
        doExploreNIO(mExploreRootPath, mExcludedPaths);
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

                // check whether to stop
                synchronized (mStateLock) {
                    if (getState() == State.STOPPED) {
                        return FileVisitResult.TERMINATE;
                    } else {
                        return super.visitFile(file, attrs);
                    }
                }
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

                // check whether to stop
                synchronized (mStateLock) {
                    if (getState() == State.STOPPED) {
                        return FileVisitResult.TERMINATE;
                    } else {
                        return super.preVisitDirectory(dir, attrs);
                    }
                }
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
        mDiscoveredFilePathsQueue.offer(file.getAbsolutePath()); // fail if no space is available

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
