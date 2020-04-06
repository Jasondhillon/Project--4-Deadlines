package data;

import sprites.Airport;

public class Job
{
	private String type;
	private String name;
	private String destination;
	private float x, y; 
	private int payment;
	private String icon;
	private Airport airport;
	
	public Job(String type, String name, String destination, Airport airport, float x, float y, int payment, String icon){
		this.type = type;
		this.name = name;
		this.destination = destination;
		this.airport = airport;
		this.x = x;
		this.y = y;
		this.payment = payment;
		this.icon = icon;
	}
	
	public String getName()
	{
		return name;
	}
	

	public Airport getAirport()
	{
		return airport;
	}

	public String getDestination()
	{
		return destination;
	}

	public int getPayment()
	{
		return payment;
	}

	public String getIcon()
	{
		return icon;
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
	public String toString()
	{
		return "\nPassenger " + name + ", going to " + destination + "\n";
	}

	
	public String getType()
	{
		return type;
	}
	
	
	
	
}
