package com.planes.pc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import data.Data;
import scenes.AirportScreen;
import scenes.FlightsScreen;
import scenes.JobScreen;
import scenes.MapScreen;
import scenes.MenuScreen;
import scenes.StoreScreen;
import sprites.Airport;
import sprites.Plane;

/*
 * @author Jason Dhillon, Jai-Jai, Jacob Sheppard
 * Meme Team Dream Meme
 */

@SuppressWarnings("serial")
public class Planes extends Game implements Serializable{
	private SpriteBatch batch;
	private MapScreen mapScreen;
	private MenuScreen menuScreen;
	private AirportScreen airportScreen;
	private FlightsScreen flightsScreen;
	private JobScreen jobScreen;
	private StoreScreen storeScreen;
	private Screen previousScreen;
	private Screen previousScreen2;
	private ArrayList<Data> playerData;
	private ArrayList<Plane> planesData;
	private File data = new File("data.txt");
	private Sprite sprite;

	//Create() runs only once, just like how a constructor works, used to initialize everything
	@Override
	public void create () {
		batch = new SpriteBatch();
		sprite = new Sprite(new Texture(Gdx.files.internal("map_plane.png")));
		sprite.setSize(50, 50);
		sprite.setOriginCenter();
		
		// No save data
		if(!data.exists()) {
			//Create first time airport selection screen
			playerData = new ArrayList<Data>();
			// Initialize money to $50,000 and and empty list of planes, store in index 0
			playerData.add(new Data(50000, new ArrayList<Plane>()));
			// Populate the airport data from JSON
			getAirportsJson();
		
		// Save data
		}else{
			playerData = SerializationRead();
			if(playerData == null) {
				System.out.println("The data.txt is broken!");
				System.exit(0);
			}
		}

		//Batch = the set of things to draw - only one can exist, kind of like having only one main method
		planesData = new ArrayList<Plane>();
		Plane.data = playerData.get(0);
		// Populate the planes data from JSON
		getPlanesJson();
		mapScreen = new MapScreen(this, playerData);
		menuScreen = new MenuScreen(this, playerData);
		airportScreen = new AirportScreen(this, playerData);
		flightsScreen = new FlightsScreen(this, playerData);
		storeScreen = new StoreScreen(this, playerData);
		jobScreen = new JobScreen(this, playerData);
		setScreen(mapScreen);

	}

	//Is called per frame per second, constantly updates the screen
	@Override
	public void render () {
		super.render();
	}
	@Override
	public void dispose () {
		batch.dispose();
		SerializationWrite(mapScreen.getAirports());
	}

	public SpriteBatch getBatch() {
		return this.batch;
	}

	public MapScreen getMapScreen() {
		return mapScreen;
	}

	public MenuScreen getMenuScreen() {
		return menuScreen;
	}

	public AirportScreen getAirportScreen() {
		return airportScreen;
	}

	public StoreScreen getStoreScreen() {
		return storeScreen;
	}

	public FlightsScreen getFlightsScreen() {
		return flightsScreen;
	}
	
	public JobScreen getJobScreen() {
		return jobScreen;
	}
	
	public void setPreviousScreen(Screen screen) {
		previousScreen = screen;
	}

	public Screen getPreviousScreen() {
		return previousScreen;
	}

	public void setPreviousScreen2(Screen screen) {
		previousScreen2 = screen;
	}

	public Screen getPreviousScreen2() {
		return previousScreen2;
	}

	public ArrayList<Plane> getPlanesData(){
		return planesData;
	}


	@SuppressWarnings("unchecked")
	public ArrayList<Data> SerializationRead(){
		ObjectInputStream in;
		ArrayList<Data> temp;

		try{
			in = new ObjectInputStream(new FileInputStream("data.txt"));
			temp =  (ArrayList<Data>) in.readObject();
			for(Plane a : temp.get(0).getBoughtPlanes())
			{
				a.set(sprite);
			}
			in.close();
			return temp;

		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		return null;
	}	

	public void SerializationWrite(ArrayList<Airport> toWrite){
		ObjectOutputStream out;
		ArrayList<Data> temp = new ArrayList<Data>();
		// Write current money and bought planes
		temp.add(new Data(playerData.get(0).getMoney(), playerData.get(0).getBoughtPlanes()));
		// Write airports
		for(Airport a : toWrite) {
			temp.add(new Data(a.getName(),a.getLocation(),a.getTimeZone(), a.getPrice(), a.isBought(), a.getX(), a.getY()));
		}

		try{
			data.createNewFile();
			out = new ObjectOutputStream(
					new FileOutputStream("data.txt"));
			out.writeObject(temp);
			out.close();


		}catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	private void getPlanesJson() {
		JsonValue jsonFile = new JsonReader().parse( Gdx.files.internal("planes/planeInfo.json"));
		for(int i = 1; i<=jsonFile.size; i++) {
//			System.out.println(i + "\n"+ jsonFile.get(i-1).get(1).toString() + "\n"  + jsonFile.get(i-1).getInt(2) + "\n" + jsonFile.get(i-1).getInt(3));
			// jsonFile.get(i-1).getInt(2)
			planesData.add(new Plane(sprite, jsonFile.get(i-1).get(1).toString(), i, 50, jsonFile.get(i-1).getInt(3), null));
		}
	}
	
	private void getAirportsJson() {
		JsonValue jsonFile = new JsonReader().parse( Gdx.files.internal("cityInfo.json"));
		for(int i = 1; i<=jsonFile.size; i++) {
			String temp = "";
			if(jsonFile.get(i-1).getInt(7) < 10) {
				temp = "0" + jsonFile.get(i-1).getInt(7) + "_background.png";
			}else {
				temp = jsonFile.get(i-1).getInt(7) + "_background.png";
			}
			// Airports are stored from index 1 onwards
			playerData.add(new Data(jsonFile.get(i-1).get(2).toString(), temp , "America/New_York", 2000, false, jsonFile.get(i-1).getInt(3)-6, 1905 - jsonFile.get(i-1).getInt(4)));
		}
	}
	
	

	public ArrayList<Data> getPlayerData()
	{
		return playerData;
	}

	//Resizes the textures
	public TextureRegionDrawable createTextureRegionDrawable(String url, int width, int height) {
		TextureRegionDrawable temp = new TextureRegionDrawable(new TextureRegion(new Texture(url)));
		temp.setMinWidth(width);
		temp.setMinHeight(height);
		return temp;


	}
}
