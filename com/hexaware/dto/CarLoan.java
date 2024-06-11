package com.hexaware.dto;

public class CarLoan {
	private String carModel;
    private int carValue;

    public CarLoan() {
    	
    }

	public CarLoan(String carModel, int carValue) {
		super();
		this.carModel = carModel;
		this.carValue = carValue;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public int getCarValue() {
		return carValue;
	}

	public void setCarValue(int carValue) {
		this.carValue = carValue;
	}

	@Override
	public String toString() {
		return "CarLoan [carModel=" + carModel + ", carValue=" + carValue + "]";
	}
    
	
    
}
