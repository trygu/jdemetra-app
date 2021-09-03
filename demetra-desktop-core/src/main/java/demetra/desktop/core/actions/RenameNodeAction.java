/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demetra.desktop.core.actions;

import demetra.desktop.actions.AbilityNodeAction;
import demetra.desktop.actions.Renameable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

import java.util.stream.Stream;

/**
 *
 * @author Philippe Charles
 */
@ActionID(category = "File", id = "demetra.desktop.core.actions.RenameNodeAction")
@ActionRegistration(displayName = "#RenameNodeAction", lazy = false)
@NbBundle.Messages({"RenameNodeAction=Rename..."})
public final class RenameNodeAction extends AbilityNodeAction<Renameable> {

    public RenameNodeAction() {
        super(Renameable.class);
    }

    @Override
    protected void performAction(Stream<Renameable> items) {
        items.forEach(Renameable::rename);
    }

    @Override
    public String getName() {
        return Bundle.RenameNodeAction();
    }
}
