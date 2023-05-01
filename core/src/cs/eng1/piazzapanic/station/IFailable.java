package cs.eng1.piazzapanic.station;

public interface IFailable {
    void startFailTimer();

    void stopFailTimer();

    void tickFailTimer(float delta);

    float getFailPct();

    default boolean hasFailed() {
        return getFailPct() >= 1;
    }

}
