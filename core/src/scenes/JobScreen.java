package scenes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.planes.pc.Planes;

import data.Data;
import data.DataTextButton;
import data.FlightPath;
import data.Job;
import sprites.Plane;

public class JobScreen implements Screen{
	private Planes game;
	private Plane currentPlane;
	private Stage Ui;
	private ArrayList<Data> data;
	private OrthographicCamera camera;
	private Label moneyLabel;
	private Image header;
	private Image bar;
	private Image coin;
	private ImageButton menuButton;
	private ImageButton flyButton;
	private ImageButton mapButton;
	private ImageButton flightsButton;
	private ArrayList<Job> jobs;
	private ArrayList<Job> selectedJobs;
	
	public JobScreen(final Planes game, ArrayList<Data> data) {
		this.game = game;
		this.data = data;
		Ui = new Stage();

		//Setup Camera
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2); 
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.zoom = 0.4f;
		
		//Header
		header = new Image(new Texture("ui/jobs_header169.png"));
		header.setScaleX(4f);
		header.setScaleY(3f);
		header.setPosition(0, 690);
		
		//Fly Button
		TextureRegionDrawable flyButtonTexture = game.createTextureRegionDrawable("ui/hanger_fly_button.png", 100, 100);
		flyButton = new ImageButton(flyButtonTexture, flyButtonTexture.tint(Color.GRAY));
		flyButton.setPosition(50, 50);
		flyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(selectedJobs.size() > 0)
				{
					ArrayList<FlightPath> path = new ArrayList<FlightPath>();
					for(Job job : selectedJobs)
						path.add(new FlightPath(job.getAirport().getX()+5, job.getAirport().getY()+5, job.getAirport(), job.getPayment()));
					currentPlane.setFlying(path);
					game.setScreen(game.getMapScreen());
				}
			}

		});

		//Back Button
		TextureRegionDrawable menuButtonTexture = game.createTextureRegionDrawable("ui/menu_close.png", 100, 100);
		menuButton = new ImageButton(menuButtonTexture, menuButtonTexture.tint(Color.GRAY));
		menuButton.setPosition(Gdx.graphics.getWidth()-100, 0);
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AirportScreen tmp = (AirportScreen)game.getPreviousScreen2();
				tmp.setDrawPlane(true);
				game.setScreen(tmp);
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
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(Ui);
		selectedJobs = new ArrayList<Job>();
		
		//Entries
		TextureRegionDrawable shopTexture = game.createTextureRegionDrawable("ui/flightlog_item.png", Gdx.graphics.getWidth(), 100);
		final LabelStyle style = new LabelStyle(new BitmapFont(), Color.WHITE);

		//Create table of owned planes
		Table scrollTable = new Table();
		if(jobs.size()>0) 
		{
			for(int i = 0; i<jobs.size(); i++) 
			{
				final int index = i;
				//Create entry
				Table t1 = new Table();
				//Add entry to table of entries
				t1.setBackground(shopTexture);
				t1.left();
				
				//Add destination to entry
				Image city = new Image(new Texture("ui/citydot_c1.png"));
				city.setOrigin(4, 4);
				city.scaleBy(3f);
				t1.add(city).padLeft(50);
				Label destinationName = new Label(jobs.get(i).getDestination(), style);
				destinationName.setFontScale(2f);
				destinationName.setWrap(true);
				t1.add(destinationName).padLeft(20).maxWidth(200);
				
				//Add passenger name to entry
				Label jobName = new Label(jobs.get(i).getName(), style);
				jobName.setFontScale(2f);
				jobName.setWrap(true);
				t1.add(jobName).padLeft(250).maxWidth(200);
				
				//Add passenger image to entry
				try {
					t1.add(new Image(game.createTextureRegionDrawable("bitizens/bitizen" + MathUtils.random(1, 10) + ".png", 38, 50))).padLeft(350);
				}catch(Exception e) {}

				//Add payment to entry
				Image payment = new Image(new Texture("ui/coin.png"));
				payment.setOrigin(4, 4);
				payment.scaleBy(3f);
				t1.add(payment).padLeft(150);
				Label amount = new Label(jobs.get(i).getPayment()+"", style);
				amount.setFontScale(2f);
				amount.setWrap(true);
				t1.add(amount).padLeft(20).maxWidth(200);
				
				// Add to flight button
				DataTextButton select = new DataTextButton("Select", new Skin (Gdx.files.internal("clean-crispy-ui.json")));
				select.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (selectedJobs.contains(jobs.get(index)))
						{
							selectedJobs.remove(jobs.get(index));
							select.setText("Select");
						}
						else
						{
							selectedJobs.add(jobs.get(index));
							select.setText("Remove");
						}
					}
					
				});
				t1.add(select).padLeft(100);
				
				// Add completed row to table
				scrollTable.add(t1).expand();
				scrollTable.row();
				
				//Add empty placeholder to allow easy access to the bottom entry when scrolling
				if(i == jobs.size()-1) 
				{
					float temp = 1;
					if(jobs.size()<7)
						temp = Math.abs(jobs.size()-7);
					for(int j = 0; j<temp; j++) 
					{
						t1 = new Table();
						t1.setBackground(shopTexture);
						t1.left();
						t1.add(new Image());
						scrollTable.add(t1).expand().row();
					}
				}
			}

			ScrollPane scroller = new ScrollPane(scrollTable);
			Table entries = new Table();
			entries.setPosition(0, -20);
			entries.setFillParent(true);
			entries.add(scroller).fill().expand();

			Ui.addActor(entries);
			Ui.setScrollFocus(scroller);
		}

		Ui.addActor(header);
		Ui.addActor(bar);
		Ui.addActor(coin);
		Ui.addActor(moneyLabel);
		Ui.addActor(flyButton);
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
		
		moneyLabel.setText(data.get(0).getMoney() + "");
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
		Ui.clear();
	}

	@Override
	public void dispose() {

	}

	
	public void setJobs(Plane plane, ArrayList<Job> jobs)
	{
		currentPlane = plane;
		this.jobs = jobs;
	}
}
