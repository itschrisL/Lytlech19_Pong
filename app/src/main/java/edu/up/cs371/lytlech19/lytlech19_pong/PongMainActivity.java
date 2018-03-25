package edu.up.cs371.lytlech19.lytlech19_pong;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Test;

/**
 * PongMainActivity
 * 
 * This is the activity for the Pong game. It attaches a PongAnimator to
 * an AnimationSurface.
 * 
 * @author Andrew Nuxoll
 * @author Steven R. Vegdahl
 * @author Chris Lytle
 * @version March 2018
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
		TextView textView1 = findViewById(R.id.TextView1);
		TextView textView2 = findViewById(R.id.TextView2);

		final PongAnimator pongAnimator = new PongAnimator();
		TestAnimator testAnimator = new TestAnimator();
		mySurface.setAnimator(pongAnimator);
		mySurface.setTextViews(textView1, textView2);

		Button addBallButton = findViewById(R.id.newBallButton);
		addBallButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pongAnimator.addBall(true);
			}
		});

		Button addBrickButton = findViewById(R.id.AddBrickButton);
		addBrickButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				pongAnimator.addBrick();
			}
		});
	}
}
