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
package internal.ui.components.parts;

import demetra.timeseries.Ts;
import demetra.ui.NextTsManager;
import demetra.ui.TsEvent;
import demetra.ui.TsListener;
import demetra.ui.beans.PropertyChangeBroadcaster;
import demetra.ui.components.parts.HasTs;

/**
 *
 * @author Philippe Charles
 */
@lombok.RequiredArgsConstructor
public final class HasTsImpl implements HasTs, TsListener {

    @lombok.NonNull
    private final PropertyChangeBroadcaster broadcaster;

    Ts ts = null;

    public HasTsImpl register(NextTsManager manager) {
        manager.addWeakListener(this);
        return this;
    }

    @Override
    public Ts getTs() {
        return ts;
    }

    @Override
    public void setTs(Ts ts) {
        Ts old = this.ts;
        this.ts = ts;
        broadcaster.firePropertyChange(TS_PROPERTY, old, this.ts);
    }

    @Override
    public void tsUpdated(TsEvent event) {
        if (hasTs() && event.getMoniker().equals(ts.getMoniker())) {
            setTs(event.getSource().getTs(ts.getMoniker(), ts.getType()));
        }
    }

    private boolean hasTs() {
        return ts != null;
    }
}
