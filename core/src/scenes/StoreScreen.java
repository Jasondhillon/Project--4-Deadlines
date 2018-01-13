package scenes;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.planes.pc.Planes;

import data.BuyButton;
import data.Data;
import sprites.Plane;

public class StoreScreen implements Screen{
	private Planes game;
	private Stage Ui;
	private ArrayList<Data> data;
	private Label moneyLabel;
	private LabelStyle style;
	private ScrollPane scroller;
	private Dialog buyDialog;

	public StoreScreen(final Planes game, ArrayList<Data> data) {
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
				game.setPreviousScreen2(game.getStoreScreen());
				game.setScreen(game.getFlightsScreen());
			}

		});
		flightsButton.setPosition(Gdx.graphics.getWidth()-290, 0);

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

		//Entries
		TextureRegionDrawable shopTexture = game.createTextureRegionDrawable("ui/shop_item.png", Gdx.graphics.getWidth(), 100);
		TextureRegionDrawable infoTexture = game.createTextureRegionDrawable("ui/info.png", 25, 25);
		style = new LabelStyle(new BitmapFont(), Color.WHITE);


		//TODO: onClick send info from dialog to create a new Plane Sprite
		//TODO: Create a hangarScreen to store planes in
		//TODO: Send planes to Airport, add Plane to airport ArrayList<Plane>
		//TODO: Keep a global list of planes and their location so we can swap to and from them
		//TODO: Add location either being [airport/in the air/hangar]

		//Create table of entries
		Table scrollTable = new Table();
		//int[] position = new int[51];
		for(int i = 1; i<=game.getPlanesData().size(); i++) {

			//Create entry
			Table t1 = new Table();
			t1.setBackground(shopTexture);
			t1.left();

			//Add plane name to entry
			Label planeName = new Label(game.getPlanesData().get(i-1).getName(), style);
			planeName.setFontScale(2f);
			planeName.setWrap(true);
			t1.add(planeName).padLeft(50).maxWidth(200);

			//Add plane image to entry
			try {
				if(i == 49) t1.add(new ImageButton(game.createTextureRegionDrawable("planes/" + i + "_base.png", 80, 80)).padLeft(480));
				else t1.add(new ImageButton(game.createTextureRegionDrawable("planes/" + i + "_base.png", 160, 80)).padLeft(400));
			}catch(Exception e) {}

			//Add info button
			ImageButton infoButton = new ImageButton(infoTexture, infoTexture.tint(Color.GRAY));
			infoButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {

				}

			});
			t1.add(infoButton).padLeft(50);

			//Add cost to entry
			Label planeCost = new Label(game.getPlanesData().get(i-1).getPrice() + "", style);
			planeCost.setFontScale(2f);
			t1.add(planeCost).padLeft(130).prefWidth(80);

			//Add buy button
			TextureRegionDrawable buyButtonTexture = game.createTextureRegionDrawable("ui/shop_buy.png", 75, 50);
			BuyButton buyButton = new BuyButton(buyButtonTexture, buyButtonTexture.tint(Color.GRAY), game.getPlanesData().get(i-1));
			buyButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					createBuyDialog(buyButton.getPlane());
					buyDialog.show(Ui);
				}

			});
			t1.add(buyButton).padLeft(100);

			//Add entry to table of entries
			scrollTable.add(t1).expand();
			scrollTable.row();
			if(i == game.getPlanesData().size()) {
				t1 = new Table();
				t1.setBackground(game.createTextureRegionDrawable("ui/flightlog_item.png", Gdx.graphics.getWidth(), 100));
				t1.left();
				t1.add(new Image());
				scrollTable.add(t1).expand();
			}
		}

		scroller = new ScrollPane(scrollTable);

		Table entries = new Table();
		entries.setFillParent(true);
		entries.add(scroller).fill().expand();


		Ui.addActor(entries);
		Ui.addActor(bar);
		Ui.addActor(coin);
		Ui.addActor(moneyLabel);
		Ui.addActor(menuButton);
		Ui.addActor(mapButton);
		Ui.addActor(flightsButton);


	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(Ui);
		Ui.setScrollFocus(scroller);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.039f, .107f, .219f, 1); //Sets the background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears the screen

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
	}

	@Override
	public void dispose() {

	}

	//Creates the aiport buy dialog boxes
	public void createBuyDialog(Plane plane) {
		final Skin skin = new Skin (Gdx.files.internal("clean-crispy-ui.json"));
		final Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("ui/NB_dialog.png")));
		drawable.setMinHeight(300);
		drawable.setMinWidth(300);


		buyDialog = new Dialog("", skin);

		TextButton btnYes = new TextButton("Yes", skin);
		btnYes.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if(data.get(0).getMoney() - plane.getPrice() >= 0) {
					data.get(0).setMoney(data.get(0).getMoney()- plane.getPrice());
					data.get(0).addPlane(plane);
					System.out.println(data.get(0).getPlane());
					buyDialog.hide();
					buyDialog.cancel();
					buyDialog.remove();

				}else{
					final Dialog error = new Dialog("", skin);
					error.setBackground(drawable);

					Label label2 = new Label("You don't have\n enough money\n to purchase this!", style);
					label2.setFontScale(2f);
					label2.setAlignment(Align.center);

					error.getContentTable().add(label2).padTop(20f);

					TextButton btnOkay = new TextButton("Okay", skin);
					btnOkay.addListener(new ClickListener() {
						@Override
						public boolean touchDown(InputEvent event, float x, float y,
								int pointer, int button) {

							error.hide();
							error.cancel();
							error.remove();
							error.reset();
							buyDialog.hide();
							buyDialog.cancel();
							buyDialog.remove();  
							buyDialog.reset();
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
		btnNo.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				// Do whatever here for exit button
				buyDialog.hide();
				buyDialog.cancel();
				buyDialog.remove();      
				buyDialog.reset();

				return true;
			}

		});
		btnNo.getLabel().setFontScale(1.8f);

		buyDialog.setBackground(drawable);

		Table t = new Table();
		t.add(btnYes).width(60f).height(60f).pad(5f);
		t.add(btnNo).width(60f).height(60f).pad(5f);

		Label label = new Label("Would you like to buy\n" + plane.getName() + "\nfor\n $" + plane.getPrice() , style);
		label.setFontScale(2f);
		label.setAlignment(Align.center);

		buyDialog.getContentTable().add(label).padTop(20f);
		buyDialog.getButtonTable().add(t).center();
	}


}


