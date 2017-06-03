package main.crawler;

/**
 * A runner with 4 states:
 *  initialized (just created),
 *  running,
 *  paused,
 *  stopped
 */
abstract public class StatefulStoppableRunner {

    /*
        Properties
     */

    private State mState = State.INITIALIZED;

    /**
     * The Runner's current state
     * @return current state
     */
    public State getState() {
        return mState;
    }

    protected void changeStateToRunning() {
        mState = State.RUNNING;
    }
    protected void changeStateToStopped() {
        mState = State.STOPPED;
    }

    /*
        Functions
     */

    /**
     * Start the runner, change state to RUNNING (from INITIALIZED)
     */
    abstract void start();
    boolean canStart() {
        return mState == State.INITIALIZED;
    }

    /**
     * Stop the runner, change the state to STOPPED (from INITIALIZED, RUNNING)
     */
    abstract void stop();
    boolean canStop() {
        return (mState == State.INITIALIZED || mState == State.RUNNING);
    }

    /**
     * Restart the stopped runner, change the state to RUNNING (from STOPPED)
     */
    abstract void restart();
    boolean canRestart() {
        return mState == State.STOPPED;
    }

    enum State {
        INITIALIZED,
        RUNNING,
        STOPPED
    }
}
