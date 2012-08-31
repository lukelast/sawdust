package com.googlecode.sawdust.client;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.sawdust.shared.LogEntry;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SawDust implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final RemoteServerServiceAsync rpcService = GWT
			.create(RemoteServerService.class);

	final ImportLogsPanel importPanel = new ImportLogsPanel();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		TabLayoutPanel tabs = new TabLayoutPanel(2, Unit.EM);

		// Create a FormPanel and point it at a service.
		final FormPanel form = new FormPanel();
		form.setAction("/myFormHandler");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// Create a TextBox, giving it a name so that it will be submitted.
		final TextBox tb = new TextBox();
		tb.setName("textBoxFormElement");
		panel.add(tb);

		// Create a ListBox, giving it a name and some values to be associated
		// with
		// its options.
		ListBox lb = new ListBox();
		lb.setName("listBoxFormElement");
		lb.addItem("foo", "fooValue");
		lb.addItem("bar", "barValue");
		lb.addItem("baz", "bazValue");
		panel.add(lb);

		// Create a FileUpload widget.
		FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");
		panel.add(upload);

		// Add a 'submit' button.
		panel.add(new Button("Submit", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				form.submit();
			}
		}));

		final LogTable logTable = new LogTable();

		tabs.add(logTable, "View Logs");
		tabs.add(this.importPanel, "Import");
		RootLayoutPanel.get().add(tabs);

		rpcService.getLogs(ImmutableList.<String> of(), 0L, 0L,
				new AsyncCallback<ImmutableList<LogEntry>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.toString());
					}

					@Override
					public void onSuccess(ImmutableList<LogEntry> result) {
						logTable.setData(result);
					}
				});

	}
}