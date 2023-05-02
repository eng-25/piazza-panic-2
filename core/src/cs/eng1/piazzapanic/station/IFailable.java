package cs.eng1.piazzapanic.station;

/**
 * An interface for stations which should have failable preparation steps
 */
interface IFailable {
    /**
     * Starts the fail timer
     */
    void startFailTimer();

    /**
     * Stops the fail timer
     */
    void stopFailTimer();

    /**
     * Updates the fail timer
     *
     * @param delta time in seconds since last call
     */
    void tickFailTimer(float delta);

    /**
     * Returns the fail timer as a percentage of max fail time, useful for rendering calculations
     *
     * @return float percentage of current fail time out of max fail time
     */
    float getFailPct();

    /**
     * @return Whether the current preparation step has failed. True if so; false otherwise.
     */
    default boolean hasFailed() {
        return getFailPct() >= 1;
    }

}
