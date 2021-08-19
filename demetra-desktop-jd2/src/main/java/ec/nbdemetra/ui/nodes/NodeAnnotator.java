/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.nbdemetra.ui.nodes;

import demetra.ui.GlobalService;
import demetra.ui.util.CollectionSupplier;
import java.awt.Image;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.openide.nodes.Node;

/**
 *
 * @author CHARPHI
 */
@GlobalService
public final class NodeAnnotator {

    private static final NodeAnnotator INSTANCE = new NodeAnnotator();

    @NonNull
    public static NodeAnnotator getDefault() {
        return INSTANCE;
    }

    private final CollectionSupplier<NodeAnnotatorSpi> providers = NodeAnnotatorSpiLoader::get;

    public Image annotateIcon(Node node, Image image) {
        Image result = image;
        for (NodeAnnotatorSpi o : providers.get()) {
            result = o.annotateIcon(node, result);
        }
        return result;
    }

    public String annotateName(Node node, String name) {
        String result = name;
        for (NodeAnnotatorSpi o : providers.get()) {
            result = o.annotateName(node, result);
        }
        return result;
    }
}
