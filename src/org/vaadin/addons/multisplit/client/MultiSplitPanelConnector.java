package org.vaadin.addons.multisplit.client;

import java.util.Collection;
import java.util.List;

import org.vaadin.addons.multisplit.MultiSplitPanel;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Client side connector for the {@link MultiSplitPanel}
 * 
 * @author bogdanudrescu
 */
@Connect(MultiSplitPanel.class)
public class MultiSplitPanelConnector extends
        AbstractComponentContainerConnector {

    @Override
    protected void init() {
        super.init();

        System.err.println("init");
    }

    @Override
    public VMultiSplitPanel getWidget() {
        return (VMultiSplitPanel) super.getWidget();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        System.err.println("updateCaption: " + connector);
    }

    @Override
    public void onConnectorHierarchyChange(
            ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
        System.err.println("onConnectorHierarchyChange: "
                + connectorHierarchyChangeEvent);

        VMultiSplitPanel widget = getWidget();

        List<ComponentConnector> oldChildren = connectorHierarchyChangeEvent
                .getOldChildren();
        for (ComponentConnector oldConnector : oldChildren) {
            widget.remove(oldConnector.getWidget());
        }

        Collection<Widget> widgets = widget.getWidgets();

        List<ServerConnector> children = connectorHierarchyChangeEvent
                .getConnector().getChildren();
        for (int i = 0; i < children.size(); i++) {
            ServerConnector serverConnector = children.get(i);
            ComponentConnector connector = (ComponentConnector) serverConnector;

            Widget childWidget = connector.getWidget();
            if (!widgets.contains(childWidget)) {
                widget.add(childWidget, i);
            }
        }

    }
}
