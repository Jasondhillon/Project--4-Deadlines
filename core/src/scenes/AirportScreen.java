package scenes;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.planes.pc.Planes;

import data.Data;
import sprites.Airport;
import sprites.Plane;

public class AirportScreen implements Screen{
	private Planes game;
	private Plane currentPlane;
	private Stage Ui;
	private ArrayList<Data> data;
	private OrthographicCamera camera;

	private String timeZone;
	private String texturePath;
	private String texturePath_night;

	private BitmapFont font;
	private Label airportName;
	private Label moneyLabel;
	
	private boolean drawPlane;

	private Texture background;
	private Texture tarmac;
	private Texture building;
	private Texture seats;
	private Texture planeTexture;

	public AirportScreen(final Planes game, ArrayList<Data> data) {
		this.game = game;
		this.data = data;
		Ui = new Stage();

		//Setup Camera
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2); 
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.zoom = 0.4f;

		//Menu Button
		TextureRegionDrawable menuButtonTexture = game.createTextureRegionDrawable("ui/menu_button.png", 100, 100);
		ImageButton menuButton = new ImageButton(menuButtonTexture, menuButtonTexture.tint(Color.GRAY));
		menuButton.setPosition(Gdx.graphics.getWidth()-100, 0);
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setPreviousScreen(game.getAirportScreen());
				game.setScreen(game.getMenuScreen());
			}

		});

		//Create Map button
		TextureRegionDrawable mapButtonTexture = game.createTextureRegionDrawable("ui/map_button.png", 80, 80);
		ImageButton mapButton = new ImageButton(mapButtonTexture, mapButtonTexture.tint(Color.GRAY));
		mapButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getMapScreen());
			}

		});
		mapButton.setPosition(Gdx.graphics.getWidth()-200, 0);

		//Create Flights Button
		TextureRegionDrawable flightsButtonTexture = game.createTextureRegionDrawable("ui/menu_flights.png", 80, 80);
		ImageButton flightsButton = new ImageButton(flightsButtonTexture, flightsButtonTexture.tint(Color.GRAY));
		flightsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setPreviousScreen2(game.getAirportScreen());
				game.setScreen(game.getFlightsScreen());
			}

		});
		flightsButton.setPosition(Gdx.graphics.getWidth()-290, 0);

		//Create Airport name
		font = new BitmapFont();
		airportName = new Label("", new LabelStyle(new BitmapFont(Gdx.files.internal("myFont.fnt")), new Color(.2f, .42f, .49f, 1)));
		airportName.setFontScale(1.5f);
		airportName.setPosition(530, 575);

		//Create Money text/image
		Image coin = new Image(new Texture("ui/coin.png"));
		coin.setScale(4.5f);
		coin.setPosition(10, 0);
		moneyLabel = new Label(data.get(0).getMoney() + "", new LabelStyle(font, Color.WHITE));
		moneyLabel.setFontScale(2f);
		moneyLabel.setPosition(50, 10);
		
		//Initialize bottom bar
		Image bar = new Image(new Texture("ui/map_dropdown.png"));
		bar.setScaleX(15f);
		bar.setScaleY(5f);
		bar.setPosition(0, -5);

		Ui.addActor(bar);
		Ui.addActor(coin);
		Ui.addActor(moneyLabel);
		Ui.addActor(airportName);
		Ui.addActor(menuButton);
		Ui.addActor(mapButton);
		Ui.addActor(flightsButton);
		
		drawPlane = false;

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(Ui);

		//Background
		ZonedDateTime temp = ZonedDateTime.now(ZoneId.of(timeZone));
		if(temp.getHour()>=20 || temp.getHour()<8) {
			background = new Texture(texturePath_night);
		}else {
			background = new Texture(texturePath);
		}
		if(drawPlane) {
			planeTexture = new Texture("planes/" + currentPlane.getNumber() + "_base.png");
		}
		moneyLabel.setText(data.get(0).getMoney() + "");
		tarmac = new Texture("backgrounds/airport1_tarmac.png");
		building = new Texture("backgrounds/airport3_base.png");
		seats = new Texture("backgrounds/airport3_seats.png");


	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.039f, .107f, .219f, 1); //Sets the background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears the screen
		game.getBatch().setProjectionMatrix(camera.combined);
		camera.update();

		game.getBatch().begin();
		game.getBatch().draw(background, Gdx.graphics.getWidth()/2-120, Gdx.graphics.getHeight()/2-100);
		game.getBatch().draw(tarmac, Gdx.graphics.getWidth()/2-120, Gdx.graphics.getHeight()/2-100);
		if(drawPlane)game.getBatch().draw(planeTexture, Gdx.graphics.getWidth()/2-60, Gdx.graphics.getHeight()/2-40);
		game.getBatch().draw(building, Gdx.graphics.getWidth()/2-120, Gdx.graphics.getHeight()/2-87);
		game.getBatch().draw(seats, Gdx.graphics.getWidth()/2-40, Gdx.graphics.getHeight()/2-55);
		game.getBatch().end();
		Ui.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		if(drawPlane){
			planeTexture.dispose();
			currentPlane = null;
			drawPlane = false;
		}
	}

	@Override
	public void dispose() {

	}

	public void setAirport(Airport airport) {
		texturePath = "backgrounds/" + airport.getLocation();
		texturePath_night = texturePath.substring(0, texturePath.length()-4) + "_night.png";
		airportName.setText(airport.getName());
		timeZone = airport.getTimeZone();
	}
	
	public void setPlane(Plane currentPlane) {
		this.currentPlane = currentPlane;
		drawPlane = true;
	}
}
