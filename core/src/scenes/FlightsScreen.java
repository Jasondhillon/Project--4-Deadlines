package scenes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.planes.pc.Planes;

import data.Data;
import data.DataButton;
import sprites.Airport;

public class FlightsScreen implements Screen{
	private Planes game;
	private Stage Ui;
	private ArrayList<Data> data;
	private OrthographicCamera camera;
	private Label moneyLabel;
	private Image bar;
	private Image coin;
	private ImageButton menuButton;
	private ImageButton mapButton;
	private ImageButton flightsButton;

	public FlightsScreen(final Planes game, ArrayList<Data> data) {
		this.game = game;
		this.data = data;
		Ui = new Stage();

		//Setup Camera
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2); 
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.zoom = 0.4f;

		//Back Button
		TextureRegionDrawable menuButtonTexture = game.createTextureRegionDrawable("ui/menu_close.png", 100, 100);
		menuButton = new ImageButton(menuButtonTexture, menuButtonTexture.tint(Color.GRAY));
		menuButton.setPosition(Gdx.graphics.getWidth()-100, 0);
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getPreviousScreen2());
			}

		});

		//Create Map button
		TextureRegionDrawable mapButtonTexture = game.createTextureRegionDrawable("ui/map_button.png", 80, 80);
		mapButton = new ImageButton(mapButtonTexture, mapButtonTexture.tint(Color.GRAY));
		mapButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getMapScreen());
			}

		});
		mapButton.setPosition(Gdx.graphics.getWidth()-200, 0);

		//Create Flights Button
		TextureRegionDrawable flightsButtonTexture = game.createTextureRegionDrawable("ui/menu_flights.png", 80, 80);
		flightsButton = new ImageButton(flightsButtonTexture, flightsButtonTexture.tint(Color.GRAY));
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

		//TODO : Create background for shop
		//TODO : Make a list of all the planes to buy
		//TODO : Make Sprites for the planes

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(Ui);
		//Entries
		TextureRegionDrawable shopTexture = game.createTextureRegionDrawable("ui/flightlog_item.png", Gdx.graphics.getWidth(), 100);
		TextureRegionDrawable infoTexture = game.createTextureRegionDrawable("ui/info.png", 25, 25);
		TextureRegionDrawable placePlaneTexture = game.createTextureRegionDrawable("ui/startlocation_start_button.png", 75, 50);
		LabelStyle style = new LabelStyle(new BitmapFont(), Color.WHITE);


		//TODO: onClick send info from dialog to create a new Plane Sprite
		//TODO: Create a hangarScreen to store planes in
		//TODO: Send planes to Airport, add Plane to airport ArrayList<Plane>
		//TODO: Keep a global list of planes and their location so we can swap to and from them
		//TODO: Add location either being [airport/in the air/hangar]
		
		//TODO: Debugging
		System.out.println("New Table");
		
		//Create table of entries
		Table scrollTable = new Table();
		if(data.get(0).getPlane().size()>0) {
			for(int i = 0; i<data.get(0).getPlane().size(); i++) {

				//Create entry
				Table t1 = new Table();
				t1.setBackground(shopTexture);
				t1.left();
				

				//Add plane name to entry
				Label planeName = new Label(data.get(0).getPlane().get(i).getName(), style);
				planeName.setFontScale(2f);
				planeName.setWrap(true);
				t1.add(planeName).padLeft(50).maxWidth(200);

				//Add plane image to entry
				System.out.println("planes/" + data.get(0).getPlane().get(i).getNumber() + "_base.png");
				try {
					t1.add(new ImageButton(game.createTextureRegionDrawable("planes/" + data.get(0).getPlane().get(i).getNumber() + "_base.png", 160, 80)).padLeft(400));
				}catch(Exception e) {}

				//Add info button
				ImageButton infoButton = new ImageButton(infoTexture, infoTexture.tint(Color.GRAY));
				infoButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {

					}

				});
				t1.add(infoButton).padLeft(50);
				
				
				//If the plane is already placed in an airport, do not display the place plane button
				if(data.get(0).getPlane().get(i).getLocation() == null) {
					//Add place button
					DataButton flyButton = new DataButton(placePlaneTexture, placePlaneTexture.tint(Color.GRAY), data.get(0).getPlane().get(i));
					flyButton.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							
							//Check to see if atleast an airport is bought to place a plane in
							Boolean airportBought = false;
							for(Airport a : game.getMapScreen().getAirports()) {
								if(a.isBought()) airportBought = true;
							}
							
							//If airport is bought, place the plane in the airport
							if(airportBought == true) {
								DataButton temp = (DataButton)event.getListenerActor();
								game.getMapScreen().setPlacePlaneMode(true);
								game.getMapScreen().setCurrentPlane(temp.getPlane());
								game.setScreen(game.getMapScreen());
								
							//If no airports are bought then display an error dialog
							}else{
								final Dialog error = new Dialog("", new Skin (Gdx.files.internal("clean-crispy-ui.json")));
								TextureRegionDrawable dialogBackground = new TextureRegionDrawable(new TextureRegion(new Texture("ui/NB_dialog.png")));
								dialogBackground.setMinHeight(300);
								dialogBackground.setMinWidth(300);
								error.setBackground(dialogBackground);

								Label label2 = new Label("You don't have\n any airports\n purchased!", style);
								label2.setFontScale(2f);
								label2.setAlignment(Align.center);

								error.getContentTable().add(label2).padTop(20f);

								TextButton btnOkay = new TextButton("Okay", new Skin (Gdx.files.internal("clean-crispy-ui.json")));
								btnOkay.addListener(new ClickListener() {
									@Override
									public boolean touchDown(InputEvent event, float x, float y,
											int pointer, int button) {
										error.hide();
										error.cancel();
										error.remove();
										error.reset();
										return true;
									}
								});
								btnOkay.getLabel().setFontScale(1.8f);

								error.getButtonTable().add(btnOkay).center();
								error.show(Ui);
							}
						}

					});
					t1.add(flyButton).padLeft(200);
				}else{
					//Add plane location 
					Label planeLocation = new Label(data.get(0).getPlane().get(i).getLocation().getName(), style);
					planeLocation.setFontScale(2f);
					planeLocation.setWrap(true);
					t1.add(planeLocation).padLeft(50).maxWidth(200);
				}


				//Add entry to table of entries
				scrollTable.add(t1).expand();
				scrollTable.row();
				
				
				if(i == data.get(0).getPlane().size() && i>5) {
					t1 = new Table();
					t1.setBackground(game.createTextureRegionDrawable("ui/flightlog_item.png", Gdx.graphics.getWidth(), 100));
					t1.left();
					t1.add(new Image());
					scrollTable.add(t1).expand().row();
				}
			}

			ScrollPane scroller = new ScrollPane(scrollTable);
			
			Table entries = new Table();
			entries.setFillParent(true);
			entries.add(scroller).fill().expand();

			Ui.addActor(entries);
			Ui.setScrollFocus(scroller);
		}

		Ui.addActor(bar);
		Ui.addActor(coin);
		Ui.addActor(moneyLabel);
		Ui.addActor(menuButton);
		Ui.addActor(mapButton);
		Ui.addActor(flightsButton);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.039f, .107f, .219f, 1); //Sets the background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears the screen
		game.getBatch().setProjectionMatrix(camera.combined);
		camera.update();

		Ui.draw();
		Ui.act();
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
		Ui.clear();
	}

	@Override
	public void dispose() {

	}

}
