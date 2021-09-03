/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.nbdemetra.x13.ui;

import demetra.bridge.TsConverter;
import demetra.desktop.design.SwingComponent;
import demetra.timeseries.TsCollection;
import demetra.desktop.components.JTsChart;
import demetra.desktop.components.parts.HasTsCollection.TsUpdateMode;
import demetra.desktop.util.NbComponents;
import ec.satoolkit.DecompositionMode;
import ec.satoolkit.x11.X11Results;
import ec.tss.documents.DocumentManager;
import ec.tss.html.implementation.HtmlX13Summary;
import ec.tss.sa.documents.X13Document;
import ec.tss.tsproviders.utils.MultiLineNameUtil;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.modelling.ModellingDictionary;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.ui.Disposables;
import ec.ui.interfaces.IDisposable;
import ec.ui.view.JSIView;
import ec.ui.view.tsprocessing.TsViewToolkit;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

/**
 * @author Kristof Bayens
 */
@SwingComponent
public final class JX13Summary extends JComponent implements IDisposable {

    private final Box document_;
    private final JTsChart chart_;
    private final JSIView siPanel_;
    private X13Document doc_;

    public JX13Summary() {
        setLayout(new BorderLayout());

        this.chart_ = new JTsChart();
        chart_.setTsUpdateMode(TsUpdateMode.None);
        this.siPanel_ = new JSIView();

        JSplitPane split1 = NbComponents.newJSplitPane(JSplitPane.HORIZONTAL_SPLIT, chart_, siPanel_);
        split1.setDividerLocation(0.6);
        split1.setResizeWeight(.5);

        this.document_ = Box.createHorizontalBox();

        JSplitPane split2 = NbComponents.newJSplitPane(JSplitPane.VERTICAL_SPLIT, document_, split1);
        split2.setDividerLocation(0.5);
        split2.setResizeWeight(.5);

        add(split2, BorderLayout.CENTER);
    }

    public void set(X13Document doc) {
        this.doc_ = doc;
        if (doc == null) {
            return;
        }
        CompositeResults results = doc.getResults();
        if (results == null) {
            return;
        }

        HtmlX13Summary summary = new HtmlX13Summary(MultiLineNameUtil.join(doc.getInput().getName()), results, null);
        Disposables.disposeAndRemoveAll(document_).add(TsViewToolkit.getHtmlViewer(summary));

        chart_.setTsCollection(
                Stream.of(
                        getMainSeries(ModellingDictionary.Y),
                        getMainSeries(ModellingDictionary.T),
                        getMainSeries(ModellingDictionary.SA)
                ).collect(TsCollection.toTsCollection())
        );

        X11Results x11 = doc.getDecompositionPart();
        if (x11 != null) {
            TsData si = results.getData("d8", TsData.class);
            TsData seas = results.getData("d10", TsData.class);

            if (x11.getSeriesDecomposition().getMode() == DecompositionMode.LogAdditive) {
                si = si.exp();
            }

            siPanel_.setSiData(seas, si);
        } else {
            siPanel_.reset();
        }
    }

    private demetra.timeseries.Ts getMainSeries(String str) {
        return TsConverter.toTs(DocumentManager.instance.getTs(doc_, str));
    }

    @Override
    public void dispose() {
        doc_ = null;
        Disposables.disposeAndRemoveAll(document_);
    }
}
