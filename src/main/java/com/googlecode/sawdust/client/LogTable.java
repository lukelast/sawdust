package com.googlecode.sawdust.client;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.googlecode.sawdust.shared.LogEntry;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class LogTable extends DataGrid<LogEntry> {

	public void setData(List<LogEntry> data) {
		final ListDataProvider<LogEntry> dataProvider = new ListDataProvider<LogEntry>(
				data);
		dataProvider.addDataDisplay(this);

		final Column<LogEntry, String> timeColumn = new Column<LogEntry, String>(
				new TextCell()) {
			@Override
			public String getValue(LogEntry logEntry) {
				return Long.toString(logEntry.getTimestamp());
			}
		};
		this.addColumn(timeColumn, "Time");

		final Set<String> allColumns = new TreeSet<String>();

		for (LogEntry cols : dataProvider.getList()) {
			allColumns.addAll(cols.getKeys());
		}

		for (final String columnKey : allColumns) {

			Column<LogEntry, String> column = new Column<LogEntry, String>(
					new TextCell()) {
				@Override
				public String getValue(LogEntry value) {
					return value.getProperty(columnKey);
				}
			};
			this.addColumn(column, columnKey);
		}
	}

	public LogTable() {

		// dataProvider.getList().add(
		// new LogEntry("log", 0, ImmutableMap.of("col1", "row1col1",
		// "col2", "row1col2")));
		// dataProvider.getList().add(
		// new LogEntry("log", 1, ImmutableMap.of("col1", "row2col1",
		// "col2", "row2col2")));
		// dataProvider.getList().add(
		// new LogEntry("log", 333, ImmutableMap.of("col1", "row3col1",
		// "col2", "row3col2", "col3", "row3col3")));

		// Set a key provider that provides a unique key for each contact. If
		// key is
		// used to identify contacts when fields (such as the name and address)
		// change.
		// super(ContactDatabase.ContactInfo.KEY_PROVIDER);
		setWidth("100%");

		// Set the message to display when the table is empty.
		setEmptyTableWidget(new Label("Empty"));

		// Attach a column sort handler to the ListDataProvider to sort the
		// list.
		// ListHandler<ContactInfo> sortHandler = new ListHandler<ContactInfo>(
		// ContactDatabase.get().getDataProvider().getList());
		// dataGrid.addColumnSortHandler(sortHandler);

		// Create a Pager to control the table.
		// SimplePager.Resources pagerResources =
		// GWT.create(SimplePager.Resources.class);
		// pager = new SimplePager(TextLocation.CENTER, pagerResources, false,
		// 0, true);
		// pager.setDisplay(dataGrid);

		// Add a selection model so we can select cells.
		// final SelectionModel<ContactInfo> selectionModel =
		// new
		// MultiSelectionModel<ContactInfo>(ContactDatabase.ContactInfo.KEY_PROVIDER);
		// dataGrid.setSelectionModel(selectionModel,
		// DefaultSelectionEventManager
		// .<ContactInfo> createCheckboxManager());

		// Initialize the columns.
		// initTableColumns(selectionModel, sortHandler);

		// Add the CellList to the adapter in the database.
		// ContactDatabase.get().addDataDisplay(this);

		// Create the UiBinder.
		// Binder uiBinder = GWT.create(Binder.class);
		// return uiBinder.createAndBindUi(this);
	}
}