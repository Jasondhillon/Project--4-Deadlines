package sprites;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Plane extends Sprite implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -541541953842841624L;
	String name, location;
	int number, speed, price, posX, posY;
	ArrayList<Airport> destination;
	
	public Plane(String name, int number, int speed, int price, String location, int posX, int posY) {
		this.name = name;
		this.number = number;
		this.speed = speed;
		this.price = price;
		this.location = location;
		this.posX = posX;
		this.posY = posY;
		this.destination = new ArrayList<Airport>();
	}
	
	public void flightPath(ArrayList<Airport> travelPath) {
		//TODO: Make the plane fly to each airport,
		//probably get the position of each airport by referencing their name
		//then move the plane over time(based on speed) to each postion on the map
		//through grid like cooordinates
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "\n\nPlane:\n" + name;
	}
	
	
}
