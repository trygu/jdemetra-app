/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.nbdemetra.ui.tools;

import demetra.desktop.components.parts.HasTs;
import ec.nbdemetra.ui.ActiveViewManager;
import ec.nbdemetra.ui.IActiveView;
import ec.ui.view.PeriodogramView;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JMenu;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.*;
import org.openide.nodes.Sheet.Set;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//ec.nbdemetra.ui.tools//Periodogram//EN",
        autostore = false)
@TopComponent.Description(preferredID = "PeriodogramTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "ec.nbdemetra.ui.tools.PeriodogramTopComponent")
@ActionReference(path = "Menu/Tools/Spectral analysis", position = 200)
@TopComponent.OpenActionRegistration(displayName = "#CTL_PeriodogramAction")
@Messages({
    "CTL_PeriodogramAction=Periodogram",
    "CTL_PeriodogramTopComponent=Periodogram Window",
    "HINT_PeriodogramTopComponent=This is a Periodogram window"
})
public final class PeriodogramTopComponent extends TopComponent implements HasTs, IActiveView, ExplorerManager.Provider {

    private final PeriodogramView view;
    private final Node node;

    public PeriodogramTopComponent() {
        initComponents();
        view = new PeriodogramView();
        add(view);
        node = new InternalNode();
        setName(Bundle.CTL_PeriodogramTopComponent());
        setToolTipText(Bundle.HINT_PeriodogramTopComponent());
        associateLookup(ExplorerUtils.createLookup(ActiveViewManager.getInstance().getExplorerManager(), getActionMap()));
    }

    @Override
    public void open() {
        super.open();
        Mode mode = WindowManager.getDefault().findMode("output");
        if (mode != null && mode.canDock(this)) {
            mode.dockInto(this);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    @Override
    public void componentActivated() {
        ActiveViewManager.getInstance().set(this);
    }

    @Override
    public void componentDeactivated() {
        ActiveViewManager.getInstance().set(null);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public demetra.timeseries.Ts getTs() {
        return null;
    }

    @Override
    public void setTs(demetra.timeseries.Ts ts) {
        view.setTs(ts);
    }

    @Override
    public boolean fill(JMenu menu) {
        return false;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return ActiveViewManager.getInstance().getExplorerManager();
    }

    @Override
    public boolean hasContextMenu() {
        return false;
    }

    class InternalNode extends AbstractNode {

        @Messages({
            "periodogramTopComponent.internalNode.displayName=Periodogram"
        })
        InternalNode() {
            super(Children.LEAF);
            setDisplayName(Bundle.periodogramTopComponent_internalNode_displayName());
        }

        @Override
        @Messages({
            "periodogramTopComponent.transform.name=Transform",
            "periodogramTopComponent.transform.displayName=Transformation",
            "periodogramTopComponent.log.name=Log",
            "periodogramTopComponent.log.desc=When marked, logarithmic transformation is used.",
            "periodogramTopComponent.differencing.name=Differencing",
            "periodogramTopComponent.differencing.desc=An order of a regular differencing of the series.",
            "periodogramTopComponent.differencingLag.name=Differencing lag",
            "periodogramTopComponent.differencingLag.desc=A number of lags used to take differences. For example, if Differencing lag = 3 then the differencing filter does not apply to the first lag (default) but to the third lag.",
            "periodogramTopComponent.lastYears.name=Last years",
            "periodogramTopComponent.lastYears.desc=A number of years of observations at the end of the time series used to produce the autoregressive spectrum (0=the whole time series is considered.",
            "periodogramTopComponent.fullYears.name=Full years",
            "periodogramTopComponent.fullYears.desc=Use full years (end of series)",
        })
        protected Sheet createSheet() {
            Sheet sheet = super.createSheet();
            Set transform = Sheet.createPropertiesSet();
            transform.setName(Bundle.periodogramTopComponent_transform_name());
            transform.setDisplayName(Bundle.periodogramTopComponent_transform_displayName());
            Property<Boolean> log = new Property(Boolean.class) {
                @Override
                public boolean canRead() {
                    return true;
                }

                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return view.isLogTransformation();
                }

                @Override
                public boolean canWrite() {
                    return true;
                }

                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    view.setLogTransformation((Boolean) t);
                }
            };

            log.setName(Bundle.periodogramTopComponent_log_name());
            log.setShortDescription(Bundle.periodogramTopComponent_log_desc());
            transform.put(log);
            Property<Integer> diff = new Property(Integer.class) {
                @Override
                public boolean canRead() {
                    return true;
                }

                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return view.getDifferencingOrder();
                }

                @Override
                public boolean canWrite() {
                    return true;
                }

                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    view.setDifferencingOrder((Integer) t);
                }
            };

            diff.setName(Bundle.periodogramTopComponent_differencing_name());
            diff.setShortDescription(Bundle.periodogramTopComponent_differencing_desc());
            transform.put(diff);
            Node.Property<Integer> diffLag = new Node.Property(Integer.class) {
                @Override
                public boolean canRead() {
                    return true;
                }

                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return view.getDifferencingLag();
                }

                @Override
                public boolean canWrite() {
                    return true;
                }

                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    view.setDifferencingLag((Integer) t);
                }
            };
            diffLag.setName(Bundle.periodogramTopComponent_differencingLag_name());
            diffLag.setShortDescription(Bundle.periodogramTopComponent_differencingLag_desc());
            transform.put(diffLag);

            Node.Property<Integer> length = new Node.Property(Integer.class) {
                @Override
                public boolean canRead() {
                    return true;
                }

                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return view.getLastYears();
                }

                @Override
                public boolean canWrite() {
                    return true;
                }

                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    view.setLastYears((Integer) t);
                }
            };
            length.setName(Bundle.periodogramTopComponent_lastYears_name());
            length.setShortDescription(Bundle.periodogramTopComponent_lastYears_desc());
            transform.put(length);

            Node.Property<Integer> full = new Node.Property(Boolean.class) {
                @Override
                public boolean canRead() {
                    return true;
                }

                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return view.isFullYears();
                }

                @Override
                public boolean canWrite() {
                    return true;
                }

                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    view.setFullYears((Boolean) t);
                }
            };
            full.setName(Bundle.periodogramTopComponent_fullYears_name());
            full.setShortDescription(Bundle.periodogramTopComponent_fullYears_desc());
            transform.put(full);
            sheet.put(transform);
            return sheet;
        }
    }
}
