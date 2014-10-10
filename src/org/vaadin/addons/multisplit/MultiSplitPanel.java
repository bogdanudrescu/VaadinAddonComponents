package org.vaadin.addons.multisplit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.vaadin.addons.multisplit.shared.MultiSplitPanelState;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

/**
 * Split panel accepting more the 2 components.
 * 
 * @author bogdanudrescu
 */
public class MultiSplitPanel extends AbstractComponentContainer {

    /*
     * The list of components in the split panel.
     */
    private List<Component> components = new LinkedList<Component>();

    @Override
    public void addComponent(Component c) {
        components.add(c);

        try {
            super.addComponent(c);
        } catch (Exception e) {
            components.remove(c);
            throw e;
        }
    }

    /**
     * Adds the specified component at the specified index.
     * 
     * @param c
     *            the component to add.
     * @param index
     *            the index where to add it.
     */
    public void addComponent(Component c, int index) {

        // If c is already in this, we must remove it before proceeding
        // see ticket #7668
        if (c.getParent() == this) {
            // When c is removed, all components after it are shifted down
            if (index > getComponentIndex(c)) {
                index--;
            }
            removeComponent(c);
        }

        components.add(index, c);

        try {
            super.addComponent(c);
        } catch (Exception e) {
            components.remove(c);
            throw e;
        }
    }

    /**
     * Returns the index of the given component.
     * 
     * @param component
     *            the component to look up.
     * @return the index of the component or -1 if the component is not a child.
     */
    public int getComponentIndex(Component component) {
        return components.indexOf(component);
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        int index = getComponentIndex(oldComponent);

        removeComponent(oldComponent);

        addComponent(newComponent, index);
    }

    @Override
    public int getComponentCount() {
        return components.size();
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    @Override
    protected MultiSplitPanelState getState() {
        return (MultiSplitPanelState) super.getState();
    }

}
