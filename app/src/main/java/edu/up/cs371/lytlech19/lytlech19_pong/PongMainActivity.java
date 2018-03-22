package edu.up.cs371.lytlech19.lytlech19_pong;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.LinearLayout;

import junit.framework.Test;

/**
 * PongMainActivity
 * 
 * This is the activity for the Pong game. It attaches a PongAnimator to
 * an AnimationSurface.
 * 
 * @author Andrew Nuxoll
 * @author Steven R. Vegdahl
 * @version July 2013
 * 
 */
public class PongMainActivity extends AppCompatActivity {

	/**
	 * creates an AnimationSurface containing a TestAnimator.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pong_main);

		// Connect the animation surface with the animator
		edu.up.cs371.lytlech19.lytlech19_pong.AnimationSurface mySurface = (edu.up.cs371.lytlech19.
				lytlech19_pong.AnimationSurface) this.findViewById(R.id.animationSurface);
		//setContentView(mySurface);
		PongAnimator pongAnimator = new PongAnimator();
		TestAnimator testAnimator = new TestAnimator();
		mySurface.setAnimator(pongAnimator);
	}
}
