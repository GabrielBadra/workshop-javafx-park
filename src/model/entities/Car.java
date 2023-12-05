package model.entities;

import java.io.Serializable;

public class Car implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String carModel;
	private String Color;
	private String Plate;
	
	public Car(String carModel, String color, String plate) {
		this.carModel = carModel;
		this.Color = color;
		this.Plate = plate;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public String getPlate() {
		return Plate;
	}

	public void setPlate(String plate) {
		Plate = plate;
	}

	@Override
	public String toString() {
		return "Car [carModel=" + carModel + ", Color=" + Color + ", Plate=" + Plate + "]";
	}
	
	
	

}
