package demetra.ui;

import demetra.timeseries.Ts;
import demetra.timeseries.TsCollection;
import demetra.timeseries.TsInformationType;
import demetra.timeseries.TsMoniker;
import ec.util.various.swing.OnAnyThread;
import ec.util.various.swing.OnEDT;
import java.util.stream.Collector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.openide.util.WeakListeners;

/**
 *
 */
public interface NextTsManager {

    @OnAnyThread
    @NonNull
    Ts getTs(@NonNull TsMoniker moniker, @NonNull TsInformationType type);

    @OnAnyThread
    @NonNull
    TsCollection getTsCollection(@NonNull TsMoniker moniker, @NonNull TsInformationType type);

    @OnEDT
    default void addWeakListener(@NonNull TsListener listener) {
        addListener(WeakListeners.create(TsListener.class, listener, this));
    }

    @OnEDT
    void addListener(@NonNull TsListener listener);

    @OnEDT
    void removeListener(@NonNull TsListener listener);

    @NonNull
    static Collector<Ts, ?, TsCollection> getTsCollector() {
        return Collector.<Ts, TsCollection.Builder, TsCollection>of(
                TsCollection::builder,
                TsCollection.Builder::data,
                (l, r) -> l.data(r.getData()),
                TsCollection.Builder::build);
    }

}
