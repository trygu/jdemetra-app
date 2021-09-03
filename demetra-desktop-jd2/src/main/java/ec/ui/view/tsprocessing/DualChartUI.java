/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.ui.view.tsprocessing;

import demetra.bridge.TsConverter;
import demetra.timeseries.TsCollection;
import demetra.desktop.components.parts.HasTsCollection.TsUpdateMode;
import ec.tss.documents.DocumentManager;
import ec.tss.documents.TsDocument;
import demetra.desktop.components.JTsChart;
import java.util.stream.Stream;

/**
 *
 * @author Jean Palate
 */
public class DualChartUI<D extends TsDocument<?, ?>> extends PooledItemUI<IProcDocumentView<D>, String[][], JTsChart> {

    public DualChartUI() {
        super(JTsChart.class);
    }

    @Override
    protected void init(JTsChart c, IProcDocumentView<D> host, String[][] information) {
        String[] hnames = information[0], lnames = information[1];
        TsCollection col = Stream.concat(hnames != null ? Stream.of(hnames) : Stream.empty(), lnames != null ? Stream.of(lnames) : Stream.empty())
                .map(name -> TsConverter.toTs(DocumentManager.instance.getTs(host.getDocument(), name)))
                .collect(TsCollection.toTsCollection());
        c.setDualChart(true);
        c.setTsCollection(col);
        c.setTsUpdateMode(TsUpdateMode.None);
        int i = 0;
        c.getDualDispatcher().clearSelection();
        if (hnames != null) {
            for (; i < hnames.length; ++i) {
            }
        }
        if (lnames != null) {
            for (int j = 0; j < lnames.length; ++j, ++i) {
                c.getDualDispatcher().setSelectionInterval(i, i);
            }
        }
    }
}
