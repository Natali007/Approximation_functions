package com.natali007.approximation_functions;

import java.util.ArrayList;

import com.natali007.approximation_functions.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends ActionBarActivity {

	//The height and width of the display
	private int displayHeight, displayWidth;
	//An array that stores the values of the set points (coordinates of the phone) 
	public static ArrayList<Points> arrayPoints;
	//An array that stores the values of the set points (real coordinates)
	public static ArrayList<Points> realPoints;
	//An array that stores the values of the set points after Lagrange polynomial
	public static ArrayList<Points> arrayNewPoints;
	//Draw a graph of the function
	private boolean toPlot = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new DrawView(this));
		
		arrayPoints = new ArrayList<Points>();
		arrayNewPoints = new ArrayList<Points>();
		realPoints = new ArrayList<Points>();
		
		// to get the height and width of the display
        Display display = getWindowManager().getDefaultDisplay();
		
		DisplayMetrics metricsB = new DisplayMetrics();
		display.getMetrics(metricsB);
		
		displayHeight = metricsB.heightPixels;
		displayWidth = metricsB.widthPixels;
		
	}
	
	class DrawView extends View{
	
		//The style and color information for the coordinate plane
		private Paint p;
	
		//The style and color information for Points and graph of the function
	    private Paint paintPoint;
	    
	    //Path for drawing a graph of the function
	  	private Path path;
	    
	    //Name of the "button" that is responsible for cleaning
	    private String Cancel;
	    
	    //Name of the "button" that is responsible for approximation
	    private String Lagrang;
	    
	    //Coordinates set during the touch
	    private float TouchX, TouchY;

	    public DrawView(Context context) {
	      super(context);
	      
	      p = new Paint(Paint.ANTI_ALIAS_FLAG);
	      p.setStrokeWidth(2);
	      
	      paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
	      paintPoint.setStrokeWidth(2);
	      
	      path = new Path();
	      
	      Cancel = "—¡–Œ—";
		  Lagrang = "À¿√–¿Õ∆¿";
	    }
	    
	    @Override
	    protected void onDraw(Canvas canvas) {
	      //Set the canvas color
	      canvas.drawARGB(80, 102, 204, 255);
	      
	      // The coordinate axis X
	      p.setStyle(Paint.Style.STROKE);
	      p.setColor(Color.BLACK);
	      canvas.drawLine(20,  displayHeight-280, 20 ,  displayHeight-80, p);
	      //The coordinate axis Y
	      canvas.drawLine(20,  displayHeight-80, 160 ,  displayHeight-80, p);

	      p.setStyle(Paint.Style.FILL);
	      p.setColor(Color.WHITE);
	      for (int i=20; i<=140; i+=20){ //Drawing points on the  coordinate axis X
		      canvas.drawCircle(i,  displayHeight-80, 2, p);
	      }  
	      
	      for (int i=100; i<=280; i+=20){ //Drawing points on the  coordinate axis Y
		      canvas.drawCircle(20,  displayHeight-i, 2, p);
	      }  
	      
	      //Set the name of the "buttons"
	      p.setColor(Color.BLUE);
	      canvas.drawText(Cancel,displayWidth -70 ,50 , p);
	      canvas.drawText(Lagrang,displayWidth -80 ,90 , p);
	      //Set the frame for the "buttons"
	      p.setStyle(Paint.Style.STROKE);
	      canvas.drawRect(displayWidth-85, 30, displayWidth-15, 60, p);
	      canvas.drawRect(displayWidth-85, 70, displayWidth-15, 100, p);
	      
	      		//Draw the points - values of the function
			paintPoint.setColor(Color.MAGENTA);
			paintPoint.setStyle(Paint.Style.FILL);
			for (int i=0; i < arrayPoints.size(); i++ ){
				Points onTouch = arrayPoints.get(i);
				canvas.drawCircle( onTouch.x, onTouch.y, 2, paintPoint);
			}

			// If clicked the button for draw a graph of the function
			if (toPlot){
				Points onNewPoints;
				path.reset();
				for (int i=0; i < arrayNewPoints.size()-1; i++){
					onNewPoints = arrayNewPoints.get(i);
					if ((onNewPoints.y<=(displayHeight - 80)) && (onNewPoints.y>= (displayHeight - 280))){
						path.moveTo(onNewPoints.x, onNewPoints.y);
						path.lineTo(arrayNewPoints.get(i+1).x, arrayNewPoints.get(i+1).y );
					}
					
				}
				paintPoint.setStyle(Paint.Style.STROKE);
				paintPoint.setColor(Color.YELLOW);
				canvas.drawPath(path, paintPoint);
				toPlot = false;
			}	
			
	    }
	    
	   @Override
		public  boolean onTouchEvent(MotionEvent event){
			if(event.getAction() == MotionEvent.ACTION_DOWN){ 
				TouchX = (float) event.getX();
				TouchY =(float) event.getY();
				//If a touch in the coordinate plane
				if ((TouchY>=displayHeight-280) && (TouchY<=displayHeight-80)){
					
					if ((TouchX>= 30) && (TouchX <50)){
						arrayPoints.add(new Points(40, TouchY));
						invalidate();
						}
					
					if ((TouchX>= 50) && (TouchX <=70)){
						arrayPoints.add(new Points(60, TouchY));
						invalidate();
						}
					
					if ((TouchX>= 70) && (TouchX <90)){
						arrayPoints.add(new Points(80, TouchY));
						invalidate();
						}
					
					if ((TouchX>= 90) && (TouchX <=110)){
						arrayPoints.add(new Points(100, TouchY));
						invalidate();
						}
					
					if ((TouchX>= 110) && (TouchX <=130)){
						arrayPoints.add(new Points(120, TouchY));
						invalidate();
						}
					
					if ((TouchX>= 130) && (TouchX <150)){
						arrayPoints.add(new Points(140, TouchY));
						invalidate();
						}
				}
				// if touched button clean
				if ((TouchX >= displayWidth-85) && (TouchX <= displayWidth-15) && (TouchY>=30) && (TouchY<=60)){
					arrayPoints.clear();
					arrayNewPoints.clear();
					realPoints.clear();
					invalidate();
				}
				//if touched button Lagrange
				if ((TouchX >= displayWidth-85) && (TouchX <= displayWidth-15) && (TouchY>=70) && (TouchY<=100)){
					toPlot = true;
					float newX, newY, lagrangeY;
					
					for (int i=0; i< arrayPoints.size(); i++){
						Points touchPoints = arrayPoints.get(i);
						// Change  the phone coordinates  to real coordinates
						realPoints.add(new Points((touchPoints.x - 20)/20, ((displayHeight - touchPoints.y) -80)/20));
					}
					for (float i = (float) 0.1; i<=6; i+=0.1){
						//We get real coordinates and change the phone coordinates. newX and newY it's the phone coordinates
						newX = i*20 + 20;
						lagrangeY = lagrangeFunction(i);
						newY = ((float)displayHeight - 80) -  lagrangeY*20;				
						arrayNewPoints.add(new Points(newX , newY));
					}
					invalidate();
				}
					
			}
			return true;
		 }
	   
	   public float lagrangeFunction(float x) {
			 float S = 0;
			 float d1, d2;
				for (int i=0; i<MainActivity.realPoints.size(); i++){
					Points TouchPointI = MainActivity.realPoints.get(i);
					d1=1;
					d2=1;
					for (int j=0; j<MainActivity.realPoints.size(); j++){
						if (i!=j){
							Points TouchPointJ = MainActivity.realPoints.get(j);
							d1 = d1*(x - TouchPointJ.x);
							d2 = d2*(TouchPointI.x - TouchPointJ.x);
						}
					}
					S += TouchPointI.y * (d1/d2);
				}
				return S;
		 }		
	}	    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
