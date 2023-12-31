package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.service.DepartmentService;
import model.service.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnModel;

	@FXML
	private TableColumn<Seller, String> tableColumnColor;
	
	@FXML
	private TableColumn<Seller, String> tableColumnPlaca;
	
	@FXML
	private TableColumn<Seller, String> tableColumnTag;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btSearch;
	
	@FXML
	private TextField barSearch;
	
	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event));
	}
	
	@FXML
	public void onBtSearchAction(ActionEvent event) {
		if(barSearch.getText().split("")[0].equals("") || barSearch.getText().split("")[0].equals(" ")) {
			uptadeTableView();
		}else {
			filterTableView(barSearch.getText().toUpperCase());
		}
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnModel.setCellValueFactory(new PropertyValueFactory<>("model"));
		tableColumnColor.setCellValueFactory(new PropertyValueFactory<>("color"));
		tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
		tableColumnTag.setCellValueFactory(new PropertyValueFactory<>("tag"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

	}

	public void uptadeTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	public void filterTableView(String name) {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		obsList = FXCollections.observableArrayList();
		
		tableViewSeller.setItems(searchBar(obsList, name));
		initEditButtons();
		initRemoveButtons();
	}

	private ObservableList<Seller> searchBar(ObservableList<Seller> obsList, String name) {
		List<Seller> list = service.findAll();
		List<String> campObj = new ArrayList<>();
		List<String> campName = new ArrayList<>();
		
		for(Seller obj: list) {
			//AQUI PEGA NOME SOBRENOME DO OBJ E COLOCAR EM ARRAY
				campObj.addAll(Arrays.asList(obj.getName().split(" ")));
				campName.addAll(Arrays.asList(name.split(" ")));
			//FIM
			
				//COMPARAÇÃO DE NOMES ESCRITA NA BARRA E DO BANCO DE DADOS
					if(campObj.size() >= campName.size()) {
							for(int i = 0; i < campName.size(); i++) {
								if(campName.get(i).toUpperCase().equals(campObj.get(i).toUpperCase())) {
									obsList.add(obj);
									break;
								}
							}
					}else {
						if(obj.getName().toUpperCase().equals(name.toUpperCase())) {
							obsList.add(obj);
						}
					}
				//FIM
					
					campObj.clear();
					campName.clear();
			}
		
		return obsList;
	}

	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.setServices(new SellerService(), new DepartmentService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.uptadeFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		uptadeTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
						
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result =Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				uptadeTableView();
			}catch(DbIntegrityException e) {
				Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
				System.out.print(e.getMessage());
			}
		}
	}

}
