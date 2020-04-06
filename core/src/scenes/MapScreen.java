package scenes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.planes.pc.Planes;

import data.Data;
import data.FlightPath;
import sprites.Airport;
import sprites.Plane;

public class MapScreen implements Screen, InputProcessor{

	//Game assets
	private Planes game;
	private Plane currentPlane;
	private Texture map;
	private ArrayList<Data> playerData;
	private ArrayList<Airport> airports;
	private ArrayList<Plane> planes;
	private Airport selectedAirport;
	private Stage Ui;
	private Stage PlacePlaneUi;
	private Stage dialogStage;
	private Skin skin;
	private Drawable dialogBackground;
	private Label moneyLabel;
	private InputMultiplexer multiplexer;
	private InputMultiplexer multiplexer2;
	private BitmapFont font;
	private LabelStyle style;
	private boolean placePlaneMode;
	private float yOffSet = 15, xOffSet = 20;
	private OrthographicCamera camera;
	private Dialog buyDialog;
	private Label label;
	private ShapeRenderer shape;
	private ImageButton menuButton;
	private ImageButton backButton;
	private ImageButton mapButton;
	private ImageButton flightsButton;
	private Image coin;
	private Image bar;

	//Map dimensions : 3000 x 1910
	static float WORLD_WIDTH = 3000, WORLD_HEIGHT = 1910; 

	//Stuff for keeping the camera within boundaries of the map
	float leftBound , upperBound, rightBound, lowerBound;
	float newViewportWidth= 0, newViewportHeight = 0;
	Vector3 position;



	public MapScreen(final Planes main, ArrayList<Data> data) 
	{
		
		this.game = main;
		this.playerData = data;
		planes = data.get(0).getBoughtPlanes();
		airports = new ArrayList<Airport>();
		Plane.airports = airports;
		placePlaneMode = false;

		//Create the stage(Static UI) elements
		Ui = new Stage();
		PlacePlaneUi = new Stage();
		dialogStage = new Stage();
		skin = new Skin (Gdx.files.internal("clean-crispy-ui.json"));
		font = new BitmapFont();
		font.getData().setScale(.8f);
		style = new LabelStyle(font, Color.WHITE);
		
		//Initialize input processor for both stage and scene
		multiplexer = new InputMultiplexer(this, Ui);
		multiplexer2 = new InputMultiplexer(this, PlacePlaneUi);
		Gdx.input.setInputProcessor(multiplexer); //Input Processor handles all the input events i.e - mouse and keyboard

		//Camera = self explanatory
		camera = new OrthographicCamera(WORLD_WIDTH/2, WORLD_HEIGHT/2); 
		camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
		camera.zoom = 2.0f;
		
		
		//Gets the boundaries
		getBoundries(); 

		//Initialize the map
		map = new Texture("map.png");

		//Generate Airport sprites from the data
		for(Data a : data) 
		{
			if(a.getMoney() == -1) airports.add(a.createAirport());
		}
		
		// Set all owned planes to fly to given path
		for (Plane a : planes)
		{	
			if (a.getLocation() != null)
			{
				a.setPosition(a.getLocation().getX(), a.getLocation().getY());
			}
		}

		//Menu Button
		TextureRegionDrawable menuButtonTexture = game.createTextureRegionDrawable("ui/menu_button.png", 100, 100);
		menuButton = new ImageButton(menuButtonTexture, menuButtonTexture.tint(Color.GRAY));
		menuButton.setPosition(Gdx.graphics.getWidth()-100, 0);
		menuButton.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				game.setPreviousScreen(game.getMapScreen());
				game.setScreen(game.getMenuScreen());
			}

		});

		//Create Back Button
		TextureRegionDrawable BackButtonTexture = game.createTextureRegionDrawable("ui/menu_close.png", 100, 100);
		backButton = new ImageButton(BackButtonTexture, BackButtonTexture.tint(Color.GRAY));
		backButton.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				game.setScreen(game.getFlightsScreen());
			}

		});
		backButton.setPosition(Gdx.graphics.getWidth()-100, 0);
		//Create Map button
		TextureRegionDrawable mapButtonTexture = game.createTextureRegionDrawable("ui/map_button.png", 80, 80);
		mapButton = new ImageButton(mapButtonTexture, mapButtonTexture.tint(Color.GRAY));
		mapButton.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getMapScreen());
			}

		});
		mapButton.setPosition(Gdx.graphics.getWidth()-200, 0);

		//Create Flights Button
		TextureRegionDrawable flightsButtonTexture = game.createTextureRegionDrawable("ui/menu_flights.png", 80, 80);
		flightsButton = new ImageButton(flightsButtonTexture, flightsButtonTexture.tint(Color.GRAY));
		flightsButton.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				game.setPreviousScreen2(game.getMapScreen());
				game.setScreen(game.getFlightsScreen());
			}

		});
		flightsButton.setPosition(Gdx.graphics.getWidth()-290, 0);

		//Create Money text/image
		coin = new Image(new Texture("ui/coin.png"));
		coin.setScale(4.5f);
		coin.setPosition(10, 0);
		moneyLabel = new Label(data.get(0).getMoney() + "", new LabelStyle(new BitmapFont(), Color.WHITE));
		moneyLabel.setFontScale(2f);
		moneyLabel.setPosition(50, 10);
		
		//Initialize bottom bar
		bar = new Image(new Texture("ui/map_dropdown.png"));
		bar.setScaleX(15f);
		bar.setScaleY(5f);
		bar.setPosition(0, -5);
		
		
		// Dialog
			buyDialog = new Dialog("", skin);
			dialogBackground = new TextureRegionDrawable(new TextureRegion(new Texture("ui/NB_dialog.png")));
			dialogBackground.setMinHeight(300);
			dialogBackground.setMinWidth(300);
			buyDialog.setBackground(dialogBackground);
			TextButton btnYes = new TextButton("Yes", skin);
			btnYes.addListener(new ClickListener() 
{
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) 
				{
	
					if(playerData.get(0).getMoney() - selectedAirport.getPrice() >= 0) 
					{
						selectedAirport.setBought(true);
						playerData.get(0).setMoney(playerData.get(0).getMoney()-selectedAirport.getPrice());
						buyDialog.hide();
						Gdx.input.setInputProcessor(multiplexer);
						return true;
	
					}
					else
					{
						final Dialog error = new Dialog("", skin);
						error.setBackground(dialogBackground);
	
						Label label2 = new Label("You don't have\n enough money\n to purchase this!", style);
						label2.setFontScale(2f);
						label2.setAlignment(Align.center);
	
						error.getContentTable().add(label2).padTop(20f);
	
						TextButton btnOkay = new TextButton("Okay", skin);
						btnOkay.addListener(new ClickListener() 
						{
							@Override
							public boolean touchDown(InputEvent event, float x, float y,
									int pointer, int button) 
							{
	
								error.hide();
								buyDialog.hide();
								Gdx.input.setInputProcessor(multiplexer);
								return true;
							}
	
						});
						btnOkay.getLabel().setFontScale(1.8f);
	
						Table t = new Table();
						t.add(btnOkay).width(80f).height(80f).pad(5f);
	
						error.getButtonTable().add(t).center();
						error.show(Ui);
					}
					return true;
				}
	
			});
			btnYes.getLabel().setFontScale(1.8f);
	
			TextButton btnNo = new TextButton("No", skin);
			btnNo.addListener(new ClickListener() 
			{
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) 
				{
	
					buyDialog.hide();
					Gdx.input.setInputProcessor(multiplexer);
					return true;
				}
	
			});
			btnNo.getLabel().setFontScale(1.8f);
	
			Table t = new Table();
			t.add(btnYes).width(60f).height(60f).pad(5f);
			t.add(btnNo).width(60f).height(60f).pad(5f);
			buyDialog.getButtonTable().add(t).center();
			label = new Label("", style);
			label.setFontScale(2f);
			label.setAlignment(Align.center);
			buyDialog.getContentTable().add(label).padTop(20f);
			
			shape = new ShapeRenderer();
			
	}


	//Same as the create() method, place to initialize anything you need
	@Override
	public void show() 
	{
		if(placePlaneMode)
		{
			Gdx.input.setInputProcessor(multiplexer2);
			PlacePlaneUi.addActor(bar);
			PlacePlaneUi.addActor(coin);
			PlacePlaneUi.addActor(moneyLabel);
			PlacePlaneUi.addActor(mapButton);
			PlacePlaneUi.addActor(flightsButton);
			PlacePlaneUi.addActor(menuButton);
			PlacePlaneUi.addActor(backButton);
		}
		else
		{
			Gdx.input.setInputProcessor(multiplexer);
			Ui.addActor(bar);
			Ui.addActor(coin);
			Ui.addActor(moneyLabel);
			Ui.addActor(mapButton);
			Ui.addActor(flightsButton);
			Ui.addActor(backButton);
			Ui.addActor(menuButton);
		}
	}
		

	//Renders what we want on the screen
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(.039f, .107f, .219f, 1); //Sets the background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears the screen
		game.getBatch().setProjectionMatrix(camera.combined);
		shape.setProjectionMatrix(camera.combined);
		camera.update();
		
		
		game.getBatch().begin();
		
		// Show all airports
		if(placePlaneMode == false) 
		{
			game.getBatch().draw(map, 0 , 0);
			for(Airport a : airports) 
			{
				a.draw(game.getBatch());
				font.draw(game.getBatch(), a.getName(), a.getX()+ xOffSet, a.getY() + yOffSet);
			}
		}
		// Show only the bought airports for placing planes
		else
		{
			game.getBatch().draw(map, 0 , 0);
			for(Airport a : airports) 
			{
				if(a.isBought() == true) 
				{
					a.draw(game.getBatch());
					font.draw(game.getBatch(), a.getName(), a.getX()+ xOffSet, a.getY() + yOffSet);
				}
			}
		}
		
		
		// Draw all the planes that are flying
		for(Plane a : planes)
		{
			if(a.isFlying())
				if (a.getWaypoint() != -1)
					a.draw(game.getBatch());
		}
		
		game.getBatch().end();
		
		// Draw the flight paths
		for(Plane a : planes)
		{
			if(a.isFlying())
			{
				if (a.getWaypoint() != -1)
				{
					Vector2 previous = a.getPath().get(0).getVector();
					// Make a line from the plane to the current destination
					if(!a.getPath().get(a.getWaypoint()).isVisited())
					{
					shape.setColor(Color.CYAN);
					shape.begin(ShapeType.Line);
				 	shape.line(new Vector2(a.getX(), a.getY()), a.getPath().get(a.getWaypoint()).getVector());
				 	shape.end();
					}
					
				 	for (FlightPath flight: a.getPath())
				 	{
				 		// Draw the path for destinations we aren't already on
				 		if(a.getPath().get(a.getWaypoint()).getVector() != flight.getVector())
				 		{
					 		if (!flight.isVisited())
					 		{
					 		shape.setColor(Color.YELLOW);
					 		shape.begin(ShapeType.Line);
					 		shape.line(previous, flight.getVector());
					 		shape.end();
					 		}
				 		}
				 		
				 		previous = flight.getVector();
				 	}
				}
			}
	 		
		}
		moneyLabel.setText(playerData.get(0).getMoney() + "");
		if (placePlaneMode)
		{
			PlacePlaneUi.draw();
		}
		else
		{
			Ui.draw();
		}
		dialogStage.draw();
		dialogStage.act();
	}
	
	//Used to resize the screen
	@Override
	public void resize(int width, int height) {

	}
	//Used to stop rendering sprites/score etc.
	@Override
	public void pause() {

	}
	//Continue everything that you stopped when you paused
	@Override
	public void resume() {

	}
	//Used when the application goes in the background/is no longer visible. i.e - pressing home button on phone
	@Override
	public void hide() {
		setPlacePlaneMode(false);
		currentPlane = null;
	}
	//Used to dispose of elements, especially textures
	@Override
	public void dispose() {
		shape.dispose();
		dialogStage.dispose();
	}

	//Methods for handling input
	private final Vector2 mouseInWorld2D = new Vector2();
	private final Vector3 mouseInWorld3D = new Vector3();
	private final Vector3 mousePositionZoom = new Vector3();
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for(Airport a : airports){
			if(a.getBoundingRectangle().contains(mouseInWorld2D.x,mouseInWorld2D.y)){
				if(a.isBought()) {
					
					// Select airport and go to airport screen
					if(placePlaneMode == false) {
						game.getAirportScreen().setAirport(a); //Set the airportScreen to the selected airport
						game.setScreen(game.getAirportScreen()); //Switch screens
					// Place Plane in airport
					}else{
						game.getAirportScreen().setAirport(a);
						currentPlane.setLocation(a);
						game.getAirportScreen().setPlane(currentPlane);
						game.setScreen(game.getAirportScreen());
					}
				}else{
					if(placePlaneMode == false) {
						selectedAirport = a;
						createBuyDialog(); //Create the buy airport dialog
					}

				}
			}

		}
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouseInWorld3D.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouseInWorld3D);
		mouseInWorld2D.x = mouseInWorld3D.x;
		mouseInWorld2D.y = mouseInWorld3D.y;
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float x = Gdx.input.getDeltaX()*camera.zoom;
		float y = Gdx.input.getDeltaY()*camera.zoom;


		float cameraAdjustX, cameraAdjustY;

		if(camera.zoom != 2.0f) {
			if (camera.position.x <= leftBound) {
				cameraAdjustX = camera.position.x - leftBound;
				camera.position.x -= cameraAdjustX;
			}
			else if (camera.position.x >= rightBound) {
				cameraAdjustX = camera.position.x - rightBound;
				camera.position.x -= cameraAdjustX;
			}
			if (camera.position.y >= upperBound) {
				cameraAdjustY = camera.position.y - (upperBound - 5);
				camera.position.y -= cameraAdjustY;

			}
			else if (camera.position.y <= lowerBound) {
				cameraAdjustY = lowerBound - (camera.position.y - 5);
				camera.position.y += cameraAdjustY;
			}         
			else {
				camera.translate(-x,y);
			}
		}
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		//Zooming out
		if(amount>0) {

			//For fine zoom between 0.2f<0.5f
			if(camera.zoom<0.5f) {
				camera.zoom+=.02f;
				//Resize the airports
				for(Airport a: airports) {
					a.setSize(16, 16);
					font.getData().setScale(.9f);
					yOffSet = 15;
					xOffSet = 20;
				}
				getBoundries();
				bounding();

				//For regular zoom that won't exceed max zoom (2.0f)
			}else if(camera.zoom + 0.2f <2.0f){
				camera.zoom+=0.2f;
				getBoundries();
				bounding();

				//Don't do anything at max zoom, keep the frame still
			}else if(camera.zoom == 2.0f){

				//Set zoom to max (2.0f)
			}else {
				camera.zoom = 2.0f;
				getBoundries();
				bounding();
			}

			//Zooming in
		}else if(amount<0) {
			//Mouse position before zooming in
			camera.unproject(mousePositionZoom.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			float beforeZoomX = mousePositionZoom.x;
			float beforeZoomY = mousePositionZoom.y;


			//For regular zoom between 0.5f>2.0f
			if(camera.zoom>=0.5f) {
				camera.zoom-=0.2f;

			//For fine zoom between 0.5f>0.2f
			}else if(camera.zoom > 0.2f) {
				camera.zoom-=.02f;
				//Resize the airports
				for(Airport a: airports) {
					a.setSize(8, 8);
					font.getData().setScale(.5f);
					yOffSet = 15;
					xOffSet = 7;
				}
			}
			//Find difference in mouse before and after zoom, adjust camera to zoom where the mouse is
			camera.update();
			camera.unproject(mousePositionZoom.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			camera.position.add(beforeZoomX - mousePositionZoom.x, beforeZoomY - mousePositionZoom.y, 0);
			camera.update();
			getBoundries();
			bounding();
		}
		return true;
	}

	//Boundaries
	public void bounding() {
		float cameraAdjustX, cameraAdjustY;

		if (camera.position.x <= leftBound) {
			cameraAdjustX = camera.position.x - leftBound;
			camera.position.x -= cameraAdjustX;
		}
		else if (camera.position.x >= rightBound) {
			cameraAdjustX = camera.position.x - rightBound;
			camera.position.x -= cameraAdjustX;
		}
		if (camera.position.y >= upperBound) {
			cameraAdjustY = camera.position.y - upperBound + 5;
			camera.position.y -= cameraAdjustY;

		}
		else if (camera.position.y <= lowerBound) {
			cameraAdjustY = lowerBound - camera.position.y + 1;
			camera.position.y += cameraAdjustY;
		}   
	}
	public void getBoundries() {
		newViewportHeight = camera.viewportHeight * camera.zoom;
		newViewportWidth  = camera.viewportWidth  * camera.zoom;   
		leftBound  = newViewportWidth/2;
		rightBound = leftBound + (WORLD_WIDTH - newViewportWidth);
		lowerBound = newViewportHeight/2;
		upperBound = lowerBound + (WORLD_HEIGHT - newViewportHeight); 
	}

	//Used for serializing the data 
	public ArrayList<Airport> getAirports() {
		return airports;
	}

	//Returns the input multiplexer
	public InputMultiplexer getInputMultiplexer() {
		return multiplexer;
	}

	//Creates the aiport buy dialog boxes
	public void createBuyDialog() {
		Gdx.input.setInputProcessor(dialogStage);
		
		label.setText("Would you like to buy\n " + selectedAirport.getName() + "\nfor\n $" + selectedAirport.getPrice());
		buyDialog.show(dialogStage);
	}
	
	//TODO: Displays the plane flying on the map
	public void createPlaneTracker(Plane plane) {
		
	}
	
	public void setCurrentPlane(Plane currentPlane) {
		this.currentPlane = currentPlane;
	}
	
	public void setPlacePlaneMode(boolean mode) {
		placePlaneMode = mode;
	}
}
