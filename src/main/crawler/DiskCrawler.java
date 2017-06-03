package main.crawler;

/**
 * Created on 5/28/17.
 */
public class DiskCrawler {

    /*
        Global properties
     */
    public static int crawlerCount = 0;

    /*
        Properties
     */
    private int mID;
    private String mName;

    private String mCrawlRootPath;
    private String[] mExcludedPaths;

    /*
        Statistics
     */
//    private long mCountFile = 0;
//    private long mCountDir = 0;
//    private long mStartTimestamp = 0;
//    private long mStopTimestamp = 0;


    /*
        Instance
     */
    private DiskCrawler() {}

    public static DiskCrawler makeCrawler(String name) {
        DiskCrawler crawler = new DiskCrawler();

        synchronized (DiskCrawler.class) {
            crawlerCount ++;
        }
        crawler.mID = crawlerCount;
        crawler.mName = name;
        return crawler;
    }

    /*
        Functions
     */

    public void setExplorePath(String path, String[] excludingPaths) {
        mCrawlRootPath = path;
        mExcludedPaths = excludingPaths;
    }






}
