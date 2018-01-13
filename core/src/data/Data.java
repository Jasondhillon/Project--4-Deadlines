package data;

import java.io.Serializable;
import java.util.ArrayList;

import sprites.Airport;
import sprites.Plane;

@SuppressWarnings("serial")
public class Data implements Serializable{
	private String name;
	private String location;
	private boolean isBought;
	private String timeZone;
	private int price;
	private float x;
	private float y;
	private ArrayList<Plane> planes;
	private int money;
	
	
	public Data(String name, String location, String timeZone, int price, boolean isBought, float x, float y) {
		this.name = name;
		this.location = location;
		this.timeZone = timeZone;
		this.price = price;
		this.isBought = isBought;
		this.x = x;
		this.y = y;
		this.money = -1;
	}
	
	public Data(int money, ArrayList<Plane> planes) {
		this.money = money;
		this.planes = planes;
	}
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	public void addPlane(Plane plane) {
		planes.add(plane);
	}
	
	public ArrayList<Plane> getPlane() {
		return planes;
	}
	
	public Airport createAirport() {
		return new Airport(name, location, timeZone, price, isBought, x, y);
	}

	@Override
	public String toString() {
		return "Data [name=" + name + ", location=" + location + ", price=" + price + ",isBought=" + isBought + ", timeZone=" + timeZone
				+ ", x=" + x + ", y=" + y + ", money=" + money + "]";
	}
	
	
}
