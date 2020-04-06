package data;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

import sprites.Airport;

public class FlightPath implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5098484716422346004L;
	private boolean visited;
	private Vector2 vector;
	private Airport destination;
	private int payment;

	public FlightPath(float f, float g, Airport airport, int payment)
	{
		super();
		vector = new Vector2(f,g);
		destination = airport;
		this.payment = payment;
		visited = false;
	}

	public Vector2 getVector()
	{
		return vector;
	}

	public void setVector(Vector2 vector)
	{
		this.vector = vector;
	}

	public boolean isVisited()
	{
		return visited;
	}

	public void setVisited(boolean visited)
	{
		this.visited = visited;
	}
	
	public Airport getDestination()
	{
		return destination;
	}
	


	public int getPayment()
	{
		return payment;
	}
	

	@Override
	public String toString()
	{
		return "FlightPath [visited=" + visited + ", vector=" + vector + "]";
	}
	
	
	
	
}
