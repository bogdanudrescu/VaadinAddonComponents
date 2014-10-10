package org.vaadin.addons.multisplit.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.Util;
import com.vaadin.client.Util.CssSize;
import com.vaadin.shared.ui.Orientation;

/**
 * Split panel widget which allows more then 2 components to be added to it.
 * 
 * @author bogdanudrescu
 */
public class VMultiSplitPanel extends ComplexPanel {

    /**
     * The class name prefix for the split panel.
     */
    public static final String CLASSNAME = "v-splitpanel";

    /*
     * Class name of the splitter.
     */
    private final String splitterClassName;

    /*
     * Class name of the slot.
     */
    private final String slotClassName;

    /*
     * The orientation of the split panel.
     */
    private final Orientation orientation;

    /*
     * Reference to the widget's element
     */
    private Element element = DOM.createDiv();

    /*
     * Map the slots with their sizes.
     */
    private Map<Element, CssSize> slot2Weights = new HashMap<Element, Util.CssSize>();

    /*
     * Map the widgets with the slot they belong to.
     */
    private Map<Widget, Element> widget2Slot = new HashMap<Widget, Element>();

    /**
     * Create a multiple split panel container.
     */
    public VMultiSplitPanel() {
        this(Orientation.HORIZONTAL);
    }

    /**
     * Create a multiple split panel container with the specified orientation.
     * 
     * @param orientation
     *            the panel's orientation.
     */
    public VMultiSplitPanel(Orientation orientation) {
        setElement(element);

        this.orientation = orientation;

        switch (orientation) {
        case HORIZONTAL:
            addStyleName(CLASSNAME + "-horizontal");
            splitterClassName = CLASSNAME + "-hsplitter";
            slotClassName = CLASSNAME + "-hslot";
            break;

        case VERTICAL:
            addStyleName("CLASSNAME + -vertical");
            splitterClassName = CLASSNAME + "-vsplitter";
            slotClassName = CLASSNAME + "-vslot";
            break;

        // This shouldn't happen.
        default:
            splitterClassName = null;
            slotClassName = null;
            break;
        }
    }

    /**
     * Adds a widget in the split panel.
     * 
     * @param child
     *            the widget to add to this split panel.
     */
    @Override
    public void add(Widget child) {

        // Detach new child.
        child.removeFromParent();

        // Logical attach.
        getChildren().add(child);

        // Physical attach.
        DOM.appendChild(element, createSplitter());
        DOM.appendChild(element, createSlot(child));

        // Adopt.
        adopt(child);
    }

    /**
     * Adds a widget in the split panel at the specified position.
     * 
     * @param child
     *            the widget to add to this split panel.
     * @param index
     *            the position where to add the widget relative to the other
     *            children.
     */
    public void add(Widget child, int index) {
        // Validate index; adjust if the widget is already a child of this
        // panel.
        index = adjustIndex(child, index);

        // Detach new child.
        child.removeFromParent();

        // Logical attach.
        getChildren().insert(child, index);

        int domIndex = index * 2;
        DOM.insertChild(element, createSplitter(), domIndex - 1);
        DOM.insertChild(element, createSlot(child), domIndex);

        // Adopt.
        adopt(child);
    }

    /**
     * Gets the children widget list.
     * 
     * @return the children widget list.
     */
    public Collection<Widget> getWidgets() {
        ArrayList<Widget> widgets = new ArrayList<Widget>(getWidgetCount());
        for (Widget widget : getChildren()) {
            widgets.add(widget);
        }
        return widgets;
    }

    /*
     * Creates a slot element.
     */
    private Element createSlot(Widget child) {
        Element slotElement = DOM.createDiv();

        slotElement.setClassName(slotClassName);

        DOM.appendChild(slotElement, child.getElement());

        widget2Slot.put(child, slotElement);
        setWeightImpl(slotElement, CssSize.fromString("100%"));

        return slotElement;
    }

    /*
     * Create a splitter.
     */
    private Element createSplitter() {
        Element splitter = DOM.createDiv();
        DOM.appendChild(splitter, DOM.createDiv()); // for styling

        splitter.setClassName(splitterClassName);

        return splitter;
    }

    @Override
    public boolean remove(Widget w) {
        boolean removed = super.remove(w);

        if (removed) {
            slot2Weights.remove(widget2Slot.remove(w));
        }

        return removed;
    }

    /**
     * Sets the weight of the specified widget. The weight is either the width
     * or the height, depending on the orientation.
     * 
     * @param widget
     *            the widget to set the weight for.
     * @param value
     *            the weight value.
     * @param unit
     *            the unit of the value.
     */
    public void setWeight(Widget widget, float value, Unit unit) {
        setWeightImpl(widget, CssSize.fromValueUnit(value, unit));
    }

    /**
     * Sets the weight of the specified widget. The weight is either the width
     * or the height, depending on the orientation.
     * 
     * @param widget
     *            the widget to set the weight for.
     * @param weight
     *            the css weight value.
     */
    public void setWeight(Widget widget, String weight) {
        setWeightImpl(widget, CssSize.fromString(weight));
    }

    /*
     * Sets the weight of the specified widget.
     */
    private void setWeightImpl(Widget widget, CssSize weight) {
        if (!getChildren().contains(widget)) {
            throw new IllegalArgumentException(
                    "The widget is not present in the panel.");
        }

        setWeightImpl(widget2Slot.get(weight), weight);
    }

    /*
     * Sets the weight of the specified slot.
     */
    private void setWeightImpl(Element slot, CssSize weight) {
        if (weight.getUnit() != Unit.PX && weight.getUnit() != Unit.PCT) {
            throw new IllegalArgumentException(
                    "Please set the weight only in px or %.");
        }

        slot2Weights.put(slot, weight);

        updateSlotWeights();
    }

    /*
     * Recalculate the weights of the slots.
     */
    void updateSlotWeights() {
        int totalWeight = orientation == Orientation.HORIZONTAL ? getParent()
                .getOffsetHeight() : getParent().getOffsetWidth();

        int totalPxWeight = getTotalPxWeight();

        if (totalPxWeight < totalWeight) {
            // FINISH THIS
        }

    }

    /*
     * Gets the total weight of the px values.
     */
    private int getTotalPxWeight() {
        int totalPxWeight = 0;

        for (CssSize size : slot2Weights.values()) {
            if (size.getUnit() == Unit.PX) {
                totalPxWeight += size.getValue();
            }
        }

        return totalPxWeight;
    }

    /*
     * Set the weight to the slot style.
     */
    private void updateSlotWeight(Element slot, CssSize weight) {
        if (orientation == Orientation.HORIZONTAL) {
            slot.getStyle().setHeight(weight.getValue(), weight.getUnit());
        } else {
            slot.getStyle().setWidth(weight.getValue(), weight.getUnit());
        }
    }

}
