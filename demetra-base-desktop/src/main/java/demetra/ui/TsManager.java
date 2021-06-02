/*
 * Copyright 2018 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package demetra.ui;

import demetra.timeseries.Ts;
import demetra.timeseries.TsCollection;
import demetra.timeseries.TsInformationType;
import demetra.timeseries.TsMoniker;
import demetra.timeseries.TsProvider;
import demetra.tsprovider.DataSource;
import demetra.tsprovider.DataSourceFactory;
import demetra.tsprovider.DataSourceListener;
import demetra.tsprovider.DataSourceProvider;
import ec.util.various.swing.OnAnyThread;
import ec.util.various.swing.OnEDT;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import javax.swing.SwingUtilities;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Philippe Charles
 */
@GlobalService
@ServiceProvider(service = TsManager.class)
public class TsManager implements DataSourceFactory, Closeable {

    @NonNull
    public static TsManager getDefault() {
        return Lookup.getDefault().lookup(TsManager.class);
    }

    private final ConcurrentMap<String, TsProvider> providers;
    private final ConcurrentLinkedQueue<TsEvent> events;
    private final List<TsListener> updateListeners;
    private final DataSourceListener listener;
    private final ExecutorService executor;

    public TsManager() {
        this.providers = new ConcurrentHashMap<>();
        this.events = new ConcurrentLinkedQueue<>();
        this.updateListeners = new ArrayList<>();
        this.listener = new DataSourceListenerImpl();
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void close() {
        executor.shutdown();
    }

    @OnAnyThread
    public boolean register(@NonNull TsProvider provider) {
        providers.put(provider.getSource(), provider);
        if (provider instanceof DataSourceProvider) {
            ((DataSourceProvider) provider).addDataSourceListener(listener);
        }
        return true;
    }

    @OnAnyThread
    public boolean unregister(@NonNull TsProvider provider) {
        providers.remove(provider.getSource());
        if (provider instanceof DataSourceProvider) {
            ((DataSourceProvider) provider).removeDataSourceListener(listener);
        }
        return true;
    }

    @OnEDT
    public void addWeakListener(@NonNull TsListener listener) {
        addListener(WeakListeners.create(TsListener.class, listener, this));
    }

    @OnEDT
    public void addListener(@NonNull TsListener listener) {
        updateListeners.add(listener);
    }

    @OnEDT
    public void removeListener(@NonNull TsListener listener) {
        updateListeners.remove(listener);
    }

    @Override
    public Optional<TsProvider> getProvider(String name) {
        return Optional.ofNullable(providers.get(name));
    }

    @OnAnyThread
    public void loadAsync(@NonNull Ts ts, @NonNull TsInformationType info) {
        executor.execute(() -> notify(makeTs(ts.getMoniker(), info).getMoniker()));
    }

    @OnAnyThread
    public void loadAsync(@NonNull TsCollection col, @NonNull TsInformationType info) {
        executor.execute(() -> notify(makeTsCollection(col.getMoniker(), info).getMoniker()));
    }

    @Override
    public Stream<TsProvider> getProviders() {
        return providers.values().stream();
    }

    @OnAnyThread
    private void notify(TsMoniker moniker) {
        events.add(new TsEvent(this, moniker));
        SwingUtilities.invokeLater(this::notifyUpdateListeners);
    }

    @OnEDT
    private void notifyUpdateListeners() {
        TsEvent event;
        while ((event = events.poll()) != null) {
            for (TsListener o : updateListeners) {
                o.tsUpdated(event);
            }
        }
    }

    private final class DataSourceListenerImpl implements DataSourceListener {

        @Override
        public void opened(DataSource ds) {
        }

        @Override
        public void closed(DataSource ds) {
        }

        @OnAnyThread
        @Override
        public void changed(DataSource ds) {
            getProvider(DataSourceProvider.class, ds)
                    .map(provider -> provider.toMoniker(ds))
                    .ifPresent(TsManager.this::notify);
        }

        @Override
        public void allClosed(String string) {
        }
    }
}
