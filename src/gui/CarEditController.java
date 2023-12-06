package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.entities.Car;

public class CarEditController implements Initializable{
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField carModel;
	@FXML
	private TextField color;
	@FXML
	private TextField plate;
	
	private Car car;

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public void uptadeCar() {
		if(getCar() == null) {
			throw new NullPointerException("Obj Car was null");
		}
		
		carModel.setText(getCar().getCarModel());
		color.setText(getCar().getColor());
		plate.setText(getCar().getPlate());
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		
		for(DataChangeListener dataChange : dataChangeListeners) {
			dataChange.onDataChanged();
		}
		
	}
	
	
}
