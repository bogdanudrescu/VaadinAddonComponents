package org.vaadin.addons;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.multisplit.MultiSplitPanel;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("vaadinaddoncomponents")
public class VaadinaddoncomponentsUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = VaadinaddoncomponentsUI.class, widgetset = "org.vaadin.addons.multisplit.MultiSplitWidgetset")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        MultiSplitPanel multisplit = new MultiSplitPanel();
        multisplit.setSizeFull();

        multisplit.addComponent(new Label("Label 1"));
        multisplit.addComponent(new Label("Label 2"));
        multisplit.addComponent(new Button("Button"));

        setContent(multisplit);
    }

}