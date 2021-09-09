/*
 * Copyright 2013 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
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
package ec.nbdemetra.ui;

import demetra.desktop.nodes.AbstractNodeBuilder;
import demetra.desktop.nodes.NamedServiceNode;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import demetra.desktop.actions.Configurable;
import demetra.desktop.datatransfer.DataTransfer;

final class DemetraDataTransferPanel extends javax.swing.JPanel implements ExplorerManager.Provider {

    private final DemetraDataTransferOptionsPanelController controller;
    final ExplorerManager em;

    DemetraDataTransferPanel(DemetraDataTransferOptionsPanelController controller) {
        this.controller = controller;
        this.em = new ExplorerManager();
        initComponents();
//        treeTableView1.setProperties(new LocalObjectTsCollectionFormatter().createSheet().toArray()[0].getProperties());
        editButton.setEnabled(false);
        em.addVetoableChangeListener(evt -> {
            if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
                Node[] nodes = (Node[]) evt.getNewValue();
                editButton.setEnabled(nodes.length == 1 && nodes[0].getLookup().lookup(Configurable.class) != null);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        treeTableView1 = new org.openide.explorer.view.TreeTableView();
        jToolBar1 = new javax.swing.JToolBar();
        editButton = new javax.swing.JButton();

        treeTableView1.setRootVisible(false);

        jToolBar1.setFloatable(false);
        jToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.setRollover(true);

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/demetra/desktop/icons/preferences-system_16x16.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editButton, org.openide.util.NbBundle.getMessage(DemetraDataTransferPanel.class, "DemetraDataTransferPanel.editButton.text")); // NOI18N
        editButton.setToolTipText(org.openide.util.NbBundle.getMessage(DemetraDataTransferPanel.class, "DemetraDataTransferPanel.editButton.toolTipText")); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(editButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(treeTableView1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(treeTableView1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 216, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        em.getSelectedNodes()[0].getPreferredAction().actionPerformed(evt);
    }//GEN-LAST:event_editButtonActionPerformed

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    void load() {
        em.setRootContext(new AbstractNodeBuilder()
                .add(DataTransfer.getDefault().getProviders().stream().map(NamedServiceNode::new))
                .name("Data Transfer handler")
                .build());
    }

    void store() {
        for (Node o : em.getRootContext().getChildren().getNodes()) {
            if (o instanceof NamedServiceNode) {
                ((NamedServiceNode) o).applyConfig();
            }
        }
    }

    boolean valid() {
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton editButton;
    private javax.swing.JToolBar jToolBar1;
    private org.openide.explorer.view.TreeTableView treeTableView1;
    // End of variables declaration//GEN-END:variables
}
