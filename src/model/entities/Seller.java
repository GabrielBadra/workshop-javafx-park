package model.entities;

import java.io.Serializable;

public class Seller implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String model;
	private String color;
	private String placa;
	private String tag;
	
	private Department department;
	
	public Seller() {
	}

	public Seller(Integer id, String name, String model, String color, String placa,String tag, Department department) {
		super();
		this.id = id;
		this.name = name;
		this.model = model;
		this.color = color;
		this.placa = placa;
		this.tag = tag;
		this.department = department;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seller other = (Seller) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Seller [id=" + id + ", name=" + name + ", model=" + model + ", color=" + color + ", placa=" + placa
				+ ", tag=" + tag + ", department=" + department + "]";
	}


}
