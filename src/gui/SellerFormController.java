package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.service.DepartmentService;
import model.service.SellerService;

public class SellerFormController implements Initializable {

	private Seller seller;

	private SellerService service;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtModel;

	@FXML
	private TextField txtColor;

	@FXML
	private TextField txtPlaca;

	@FXML
	private ComboBox<Department> comboBoxDepartment;
	
	@FXML
	private ComboBox<String> comboBoxTag;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorModel;

	@FXML
	private Label labelErrorColor;

	@FXML
	private Label labelErrorPlaca;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Department> obsList;

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (seller == null) {
			throw new IllegalStateException("Seller was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			seller = getFormData();
			service.saveOrUptade(seller);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error Saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {

		for (DataChangeListener dataChange : dataChangeListeners) {
			dataChange.onDataChanged();
		}

	}

	private Seller getFormData() {
		Integer id = Utils.tryParseToInt(txtId.getText());

		ValidationException error = new ValidationException("Validation error");

		// .Trim é para eliminar todos os espaços em branco
			
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			error.addErrors("name", "Field can't be empty");
		}
		if (txtModel.getText() == null || txtModel.getText().trim().equals("")) {
			error.addErrors("model", "Field can't be empty");
		}
		if (txtColor.getText() == null || txtColor.getText().trim().equals("")) {
			error.addErrors("color", "Field can't be empty");
		}
		if (txtPlaca.getText() == null || txtPlaca.getText().trim().equals("")) {
			error.addErrors("placa", "Field can't be empty");
		}
		
		if (error.getErrors().size() > 0) {
			throw error;
		}

		return new Seller(id, txtName.getText(), txtModel.getText(), txtColor.getText(), txtPlaca.getText(),comboBoxTag.getValue(),
		comboBoxDepartment.getValue());
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldMaxLength(txtModel, 60);
		Constraints.setTextFieldMaxLength(txtColor, 60);
		Constraints.setTextFieldMaxLength(txtPlaca, 15);
		initializeComboBoxDepartment();
		initializeComboBoxTag();
	}

	public void uptadeFormData() {
		if (seller == null) {
			throw new IllegalStateException("Entity was null.");
		}
		txtId.setText(String.valueOf(seller.getId()));
		txtName.setText(seller.getName());
		txtModel.setText(seller.getModel());
		txtColor.setText(seller.getColor());
		txtPlaca.setText(seller.getPlaca());
		if(seller.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}else {
			comboBoxDepartment.setValue(seller.getDepartment());
		}
		if(seller.getTag() == null) {
			comboBoxTag.getSelectionModel().selectFirst();
		}else {
			comboBoxTag.setValue(seller.getTag());
		}
		
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("name"))? errors.get("name") : "");
		labelErrorModel.setText((fields.contains("model"))? errors.get("model") : "");
		labelErrorColor.setText((fields.contains("color"))? errors.get("color") : "");
		labelErrorPlaca.setText((fields.contains("placa"))? errors.get("placa") : "");
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxTag() {
		ObservableList<String> obs = FXCollections.observableArrayList("Sim", "Não");
		
		comboBoxTag.setItems(obs);
	}
}
