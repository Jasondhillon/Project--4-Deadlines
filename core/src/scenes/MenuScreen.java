package scenes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.planes.pc.Planes;

import data.Data;

public class MenuScreen implements Screen{
	private Planes game;
	private Stage Ui;
	private ArrayList<Data> data;
	private Label moneyLabel;

	public MenuScreen(final Planes game, ArrayList<Data> data) {
		this.game = game;
		this.data = data;
		Ui = new Stage();

		//Back Button
		TextureRegionDrawable menuButtonTexture = game.createTextureRegionDrawable("ui/menu_close.png", 100, 100);
		ImageButton menuButton = new ImageButton(menuButtonTexture, menuButtonTexture.tint(Color.GRAY));
		menuButton.setPosition(Gdx.graphics.getWidth()-100, 0);
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getPreviousScreen());
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
				game.setPreviousScreen2(game.getMenuScreen());
				game.setScreen(game.getFlightsScreen());
			}

		});
		flightsButton.setPosition(Gdx.graphics.getWidth()-290, 0);

		//Create fake Settings Button
		ImageTextButton settingsButton = new ImageTextButton("Settings", new ImageTextButtonStyle(null, null, null, new BitmapFont()));
		splitTextImage(settingsButton, game.createTextureRegionDrawable("ui/menu_settings.png", 80, 80), 90);
		settingsButton.setPosition(Gdx.graphics.getWidth()/2 + 150, Gdx.graphics.getHeight()/2);

		//Create Store Button
		ImageTextButton storeButton = new ImageTextButton("Store", new ImageTextButtonStyle(null, null, null, new BitmapFont()));
		splitTextImage(storeButton, game.createTextureRegionDrawable("ui/menu_shop.png", 100, 100), 90);
		storeButton.setPosition(Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()/2);
		storeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getStoreScreen());
			}

		});


		//Create Money text/image
		Image coin = new Image(new Texture("ui/coin.png"));
		coin.setScale(4.5f);
		coin.setPosition(10, 0);
		moneyLabel = new Label(data.get(0).getMoney() + "", new LabelStyle(new BitmapFont(), Color.WHITE));
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
		Ui.addActor(settingsButton);
		Ui.addActor(storeButton);
		Ui.addActor(menuButton);
		Ui.addActor(mapButton);
		Ui.addActor(flightsButton);


	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(Ui);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.039f, .107f, .219f, 1); //Sets the background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears the screen

		Ui.draw();
		moneyLabel.setText(data.get(0).getMoney() + "");
		game.getBatch().begin();
		game.getBatch().end();
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
	}

	@Override
	public void dispose() {

	}

	//Puts the image and text on two seperate rows
	public void splitTextImage(ImageTextButton button, TextureRegionDrawable texture, int size) {
		button.setSize(90, 90);
		button.getStyle().imageUp = texture;
		button.getStyle().imageDown = texture.tint(Color.GRAY);
		button.clearChildren();
		button.add(button.getImage()).row();
		button.add(button.getLabel());

	}

}
