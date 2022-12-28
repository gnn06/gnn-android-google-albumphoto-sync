package gnn.com.googlealbumdownloadappnougat.auth;

/**
 * require can be  chained
 */
public abstract class Require extends Exec {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private final Exec exec;

    Require(Exec exec) {
        this.exec = exec;
    }

    /**
     * called to check if requirement is meet
     */
    abstract boolean check();

    /**
     * called if requirement is not meet, exec is not called
     */
    abstract void require();

    /**
     * called is requirement is not initialy meet
     */
    abstract void postRequireSuccess();

    /**
     * called is requirement is not initialy meet,call resumeRequirement to resume exec after
     * request of requirement was run (success or not)
     */
    abstract void postRequireFailure();

    public void exec() {
        if (!check()) {
            require();
        } else {
            if (exec != null) {
                exec.exec();
            }
        }
    }

    /**
     * have to be called after require
     */
    public void resumeRequirement(int result) {
        if (result == SUCCESS) {
            postRequireSuccess();
            if (exec != null) {
                exec.exec();
            }
        } else {
            postRequireFailure();
        }
    }

    public Require getNextRequire() {
        return exec instanceof Require ? (Require) exec : null;
    }
}
