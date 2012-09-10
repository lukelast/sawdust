package com.googlecode.sawdust.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ImportLogsPanel extends Composite {

	private static ImportLogsPanelUiBinder uiBinder = GWT
			.create(ImportLogsPanelUiBinder.class);

	interface ImportLogsPanelUiBinder extends UiBinder<Widget, ImportLogsPanel> {
	}

	private final RemoteServerServiceAsync rpcService = GWT
			.create(RemoteServerService.class);

	@UiField
	InputElement fileFetchPath;

	@UiField
	InputElement fileFetchLogName;



    public ImportLogsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("importButton")
	void ImportButtonClick(ClickEvent event) {

		rpcService.importLogs(this.fileFetchPath.getValue(),
				this.fileFetchLogName.getValue(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Void result) {
						fileFetchLogName.setValue("");
					}
				});
	}

    @UiHandler("buttonDbConsole")
    public void handleClick(ClickEvent event) {
        rpcService.launchDbConsole(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(Void result) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }
}