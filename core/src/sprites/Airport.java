package sprites;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Airport extends Sprite implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5531177322751852082L;
	private String name;
	private String location;
	private String timeZone;
	private boolean isBought;
	private int price;
	private float x, y;
	private ArrayList<Plane> planes;

	public Airport(String name, String location, String timeZone, int price, boolean isBought, float x, float y) {
		super(new Texture("ui/citydot_c1.png"));
		if(isBought)this.setTexture(new Texture("ui/citydot_c2.png"));
		this.name = name;
		this.location = location;
		this.timeZone = timeZone;
		this.price = price;
		this.isBought = isBought;
		this.setSize(16, 16);
		this.x = x;
		this.y = y;
		this.setPosition(x, y);
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}
	
	public String getTimeZone() {
		return timeZone;
	}

	public int getPrice() {
		return price;
	}

	public boolean isBought() {
		return isBought;
	}
	
	public ArrayList<Plane> getPlanes() {
		return planes;
	}

	public void setPlanes(ArrayList<Plane> planes) {
		this.planes = planes;
	}

	public void setBought(boolean isBought) {
		this.isBought = isBought;
		this.setRegion(new Texture("ui/citydot_c2.png"));
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	@Override
	public String toString() {
		return "Airport: " + name + "Pos: " + this.getX() + "," + this.getY();
	}



}

