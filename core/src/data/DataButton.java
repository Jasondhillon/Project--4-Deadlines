package data;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import sprites.Plane;

public class DataButton extends ImageButton {
	Plane plane;
	
	public DataButton(TextureRegionDrawable imageUp, Drawable imageDown, Plane plane) {
		super(imageUp, imageDown);
		this.plane = plane;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}


}
