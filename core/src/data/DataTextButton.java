package data;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import sprites.Plane;

public class DataTextButton extends TextButton {
	Plane plane;
	
	public DataTextButton(String text, Skin skin) {
		super(text, skin);
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}


}
