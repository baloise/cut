package ch.basler.cat.client.ui.code;

import ch.basler.cat.client.backend.data.CodeType;
import ch.basler.cat.client.backend.data.CodeValue;
import ch.basler.cat.client.ui.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

/**
 * A view for performing create-read-update-delete operations on codeValues.
 * <p>
 * See also {@link CodeValueViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "CodeValue", layout = MainLayout.class)
public class CodeValueView extends HorizontalLayout
        implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "CodeValue";
    private final CodeValueGrid codeValueGrid;
    private final CodeValueForm form;
    private Select<CodeType> codeTypeSelect;

    private final CodeValueViewLogic viewLogic = new CodeValueViewLogic(this);
    private Button newCodeValue;
    private CodeValueDataProvider dataProvider = new CodeValueDataProvider();

    public CodeValueView() {
        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();

        codeValueGrid = new CodeValueGrid();
        codeValueGrid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        codeValueGrid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        form = new CodeValueForm(viewLogic);

        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(codeValueGrid);
        barAndGridLayout.setFlexGrow(1, codeValueGrid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(codeValueGrid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {

        codeTypeSelect = new Select<>();
        codeTypeSelect.setPlaceholder("Select codeType");
        codeTypeSelect.setItems(new CodeTypeDataProvider().getItems());
        codeTypeSelect.addValueChangeListener(changeEvent -> {
            if (changeEvent.getValue() != null) {
                newCodeValue.setEnabled(true);
                dataProvider.setFilter(changeEvent.getValue());
            }

        });
        newCodeValue = new Button("New codeValue");
        // Setting theme variant of new codeValueion button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newCodeValue.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newCodeValue.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newCodeValue.addClickListener(click -> viewLogic.newCodeValue());
        // A shortcut to click the new codeValue button by pressing ALT + N
        newCodeValue.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        newCodeValue.setEnabled(false);

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.setHeight("30%");


        topLayout.add(codeTypeSelect);
        topLayout.add(newCodeValue);
        topLayout.setVerticalComponentAlignment(Alignment.START, codeTypeSelect);
        topLayout.expand(codeTypeSelect);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    /**
     * Shows a temporary popup notification to the user.
     *
     * @param msg
     * @see Notification#show(String)
     */
    public void showNotification(String msg) {
        Notification.show(msg);
    }

    /**
     * Enables/Disables the new codeValue button.
     *
     * @param enabled
     */
    public void setNewCodeValueEnabled(boolean enabled) {
        newCodeValue.setEnabled(enabled);
    }

    /**
     * Deselects the selected row in the grid.
     */
    public void clearSelection() {
        codeValueGrid.getSelectionModel().deselectAll();
    }

    /**
     * Selects a row
     *
     * @param row
     */
    public void selectRow(CodeValue row) {
        codeValueGrid.getSelectionModel().select(row);
    }

    /**
     * Updates a codeValue in the list of codeValues.
     *
     * @param codeValue
     */
    public void updateCodeValue(CodeValue codeValue) {
        dataProvider.save(codeValue);
    }

    /**
     * Removes a codeValue from the list of codeValues.
     *
     * @param codeValue
     */
    public void removeCodeValue(CodeValue codeValue) {
        dataProvider.delete(codeValue);
    }

    /**
     * Displays user a form to edit a CodeValue.
     *
     * @param codeValue
     */
    public void editCodeValue(CodeValue codeValue) {
        showForm(codeValue != null);
        if (codeValue.isNewCodeValue()) {
            codeValue.setCodeType(codeTypeSelect.getValue());

        }
        form.editCodeValue(codeValue);
    }

    /**
     * Shows and hides the new codeValue form
     *
     * @param show
     */
    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}