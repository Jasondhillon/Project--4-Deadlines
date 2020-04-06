package sprites;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.javafaker.Faker;

import data.Data;
import data.FlightPath;
import data.Job;

public class Plane extends Sprite implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -541541953842841624L;
	private String name;
	public static Data data;
	private Airport location;
	private int number, price;
	private float speed = 1, tolerance = 300;
	private ArrayList<FlightPath> path;
	private Vector2 velocity = new Vector2();
	private int waypoint = 0;
	private boolean isFlying = false;
	public static ArrayList<Airport> airports;
	
	// All of these values are read from save, to change you need to reset data.txt
	public Plane(Sprite sprite, String name, int number, float speed, int price, Airport location) {
		super(sprite);
		this.name = name;
		this.number = number;
		this.speed = speed;
		this.price = price;
		this.location = location;
	}
	
	
	@Override
	public void draw(Batch batch)
	{
		if(isFlying) 
		{	
			update(Gdx.graphics.getDeltaTime());
			super.draw(batch);
		}
	}

	private void update(float delta)
	{
		if (waypoint != -1)
		{
			float angle = (float) Math.atan2(path.get(waypoint).getVector().y - getY(), path.get(waypoint).getVector().x - getX());
			velocity.set((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);
	
			setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
			setRotation(angle * MathUtils.radiansToDegrees -90);
	
			if(isWaypointReached()) 
			{
				setPosition(path.get(waypoint).getVector().x, path.get(waypoint).getVector().y);
				// Lands at final destination
				if(waypoint + 1 >= path.size())
				{
					data.setMoney(data.getMoney() + path.get(waypoint).getPayment());
					waypoint = -1;
					isFlying = false;
					setLocation(path.get(path.size() - 1).getDestination());
				}
				else
				{
					data.setMoney(data.getMoney() + path.get(waypoint).getPayment());
					waypoint++;
				}
			}
		}
	}
	
	public boolean isWaypointReached() {
		if(Math.abs(path.get(waypoint).getVector().x - getX()) <= tolerance*Gdx.graphics.getDeltaTime() && Math.abs(path.get(waypoint).getVector().y - getY()) <= tolerance*Gdx.graphics.getDeltaTime())
		{
			path.get(waypoint).setVisited(true);
			return true;
		}
		
		return false;
	}
	public ArrayList<Job> createJobs() 
	{
		Faker nameMaker = new Faker();
		ArrayList<Job> jobs = new ArrayList<>();
		for (int i = 0; i < 5; i++)
		{
			Airport a = airports.get(MathUtils.random(0,airports.size()-1));
			jobs.add(new Job("passenger", nameMaker.name().firstName() + " " + nameMaker.name().lastName(), a.getName(), a, a.getY(), a.getX(), MathUtils.random(50, 100), "" ));
		}
		return jobs;
	}
	

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public float getSpeed() {
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
	
	public void setLocation(Airport location) {
		this.location = location;
	}

	public Airport getLocation() {
		return location;
	}
	
	public ArrayList<FlightPath> getPath()
	{
		return path;
	}
	
	public int getWaypoint() {
		return waypoint;
	}


	public boolean isFlying()
	{
		return isFlying;
	}

	public void inAir()
	{
		this.isFlying = true;
	}
	
	public void setFlying(ArrayList<FlightPath> path)
	{
		waypoint = 0;
		this.isFlying = true;
		if(location != null)
		{
			setPosition(location.getX(), location.getY());
		}
		this.path = path;
	}

	@Override
	public String toString() {
		return "\n\nPlane:\n" + name;
	}
	
	
}
