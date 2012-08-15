package insane.game.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameControllerActivity extends Activity implements
		SensorEventListener {
	EditText input, port;
	EditText[] ip = new EditText[4];
	Button connect, send, lclick, rclick, up, down, drag;
	ImageButton mouse, racing, joystic;
	Button[] racebutton = new Button[8];
	Button[] joysticbutton = new Button[9];
	TextView status;
	public ObjectInputStream is;
	public ObjectOutputStream os;
	String ipadd;
	int portadd;
	boolean connected = false;
	String message;
	Socket socket;
	private SensorManager sensorManager;
	String menu = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		start();
	}

	private void start() {
		// TODO Auto-generated method stub
		setContentView(R.layout.connect);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		menu = "start";
		ip[0] = (EditText) findViewById(R.id.ip1);
		ip[1] = (EditText) findViewById(R.id.ip2);
		ip[2] = (EditText) findViewById(R.id.ip3);
		ip[3] = (EditText) findViewById(R.id.ip4);
		port = (EditText) findViewById(R.id.port);
		connect = (Button) findViewById(R.id.connect);
		status = (TextView) findViewById(R.id.status);
		ip[0].setText("192");
		ip[1].setText("168");
		ip[2].setText("181");
		ip[3].setText("1");
		port.setText("4007");
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		// connect.setOnClickListener(this);
	}

	private void conect() {
		// TODO Auto-generated method stub
		if (!connected) {
			try {
				ipadd = ip[0].getText().toString() + "."
						+ ip[1].getText().toString() + "."
						+ ip[2].getText().toString() + "."
						+ ip[3].getText().toString();
				status.setText(ipadd);
				portadd = Integer.parseInt(port.getText().toString());
				socket = new Socket(ipadd, portadd);
				try {
					os = new ObjectOutputStream(socket.getOutputStream());
					os.flush();
					is = new ObjectInputStream(socket.getInputStream());
					status.setText("Connection successful");
					sendMessage("Connection successful");
					connected = true;
					setContentView(R.layout.choicemenu);
					menu = "choicemenu";
					mouse = (ImageButton) findViewById(R.id.choice_mouse);
					racing = (ImageButton) findViewById(R.id.choice_racing);
					joystic = (ImageButton) findViewById(R.id.choice_joystic);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					status.setText("Error with streams");
					e.printStackTrace();
				}
			} catch (UnknownHostException e) {
				status.setText("host name could not be resolved");
				e.printStackTrace();
			} catch (IOException e) {
				status.setText("Cannot connect to server");
				e.printStackTrace();
			} catch (Exception e) {
				status.setText("Other Error");
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String msg) {
		try {
			os.writeObject(msg);
			os.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (connected && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] values = event.values;
			String a, b;
			DecimalFormat newFormat = new DecimalFormat("#.#");
			a = newFormat.format(values[0]) + " ";
			b = newFormat.format(values[1]) + " ";
			if (menu.contentEquals("mouse")) {
				sendMessage("M " + a + b);
			} else if (menu.contentEquals("racing")) {
				if (values[0] > 1.0 || values[1] > 1.0 || values[0] < -1.0
						|| values[1] < -1.0) {
					sendMessage("R " + a + b);
				}
			} else if (menu.contentEquals("joystic")) {
				if (values[0] > 1.0 || values[1] > 1.0 || values[0] < -1.0
						|| values[1] < -1.0) {
					sendMessage("J " + a + b);
				}
			}
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == connect.getId()) {
			conect();
		} else if (v.getId() == mouse.getId()) {
			mouse();
		} else if (v.getId() == racing.getId()) {
			racing();
		} else if (v.getId() == joystic.getId()) {
			joystic();
		} else if (v.getId() == lclick.getId()) {
			sendMessage("KL 0");
		} else if (v.getId() == rclick.getId()) {
			sendMessage("KR 0");
		} else if (v.getId() == up.getId()) {
			sendMessage("S 0");
		} else if (v.getId() == down.getId()) {
			sendMessage("S 1");
		}
	}

	private void joystic() {
		// TODO Auto-generated method stub
		menu = "joystic";
		setContentView(R.layout.joystic);
		joysticbutton[0] = (Button) findViewById(R.id.X);
		joysticbutton[1] = (Button) findViewById(R.id.Y);
		joysticbutton[2] = (Button) findViewById(R.id.Z);
		joysticbutton[3] = (Button) findViewById(R.id.A2);
		joysticbutton[4] = (Button) findViewById(R.id.B2);
		joysticbutton[5] = (Button) findViewById(R.id.C2);
		joysticbutton[6] = (Button) findViewById(R.id.Space2);
		joysticbutton[7] = (Button) findViewById(R.id.Enter2);
		joysticbutton[8] = (Button) findViewById(R.id.Esc2);
	}

	public void raceClick(View v) {
		if (v.getId() == racebutton[0].getId()) {
			sendMessage("K a 0");
		} else if (v.getId() == racebutton[1].getId()) {
			sendMessage("K b 0");
		} else if (v.getId() == racebutton[2].getId()) {
			sendMessage("K c 0");
		} else if (v.getId() == racebutton[3].getId()) {
			sendMessage("K esc 0");
		} else if (v.getId() == racebutton[4].getId()) {
			sendMessage("K enter 0");
		} else if (v.getId() == racebutton[5].getId()) {
			sendMessage("K space 0");
		}
	}

	public void joysticClick(View v) {
		if (v.getId() == joysticbutton[0].getId()) {
			sendMessage("K x 0");
		} else if (v.getId() == joysticbutton[1].getId()) {
			sendMessage("K y 0");
		} else if (v.getId() == joysticbutton[2].getId()) {
			sendMessage("K z 0");
		} else if (v.getId() == joysticbutton[3].getId()) {
			sendMessage("K a 0");
		} else if (v.getId() == joysticbutton[4].getId()) {
			sendMessage("K b 0");
		} else if (v.getId() == joysticbutton[5].getId()) {
			sendMessage("K c 0");
		} else if (v.getId() == joysticbutton[6].getId()) {
			sendMessage("K space 0");
		} else if (v.getId() == joysticbutton[7].getId()) {
			sendMessage("K enter 0");
		} else if (v.getId() == joysticbutton[8].getId()) {
			sendMessage("K esc 0");
		} else {
			sendMessage("Nothing");
		}

	}

	private void racing() {
		// TODO Auto-generated method stuba
		menu = "racing";
		setContentView(R.layout.racing);
		racebutton[0] = (Button) findViewById(R.id.A);
		racebutton[1] = (Button) findViewById(R.id.B);
		racebutton[2] = (Button) findViewById(R.id.C);
		racebutton[3] = (Button) findViewById(R.id.Esc);
		racebutton[4] = (Button) findViewById(R.id.Enter);
		racebutton[5] = (Button) findViewById(R.id.Space);
		racebutton[6] = (Button) findViewById(R.id.Up);
		racebutton[7] = (Button) findViewById(R.id.Down);
		racebutton[6].setOnTouchListener(new Button.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					sendMessage("K up 1");
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					sendMessage("K up 1");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					sendMessage("K up 2");
				}
				return false;
			}

		});
		racebutton[7].setOnTouchListener(new Button.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					sendMessage("K down 1");
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					sendMessage("K down 1");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					sendMessage("K down 2");
				}
				return false;
			}

		});
	}

	private void mouse() {
		// TODO Auto-generated method stub
		setContentView(R.layout.mouse);
		menu = "mouse";
		lclick = (Button) findViewById(R.id.lclick);
		// lclick.setOnClickListener(this);
		rclick = (Button) findViewById(R.id.rclick);
		up = (Button) findViewById(R.id.up);
		down = (Button) findViewById(R.id.down);
		drag = (Button) findViewById(R.id.drag);
		// rclick.setOnClickListener(this);
		drag.setOnTouchListener(new Button.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					sendMessage("KL 1");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					sendMessage("KL 2");
				}
				return false;
			}

		});
		up.setOnTouchListener(new Button.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					sendMessage("S 0");
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					sendMessage("S 0");
				}
				return false;
			}

		});
		down.setOnTouchListener(new Button.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					sendMessage("S 1");
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					sendMessage("S 1");
				}
				return false;
			}

		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (menu.contentEquals("start")) {
				finish();
			} else if (menu.contentEquals("choicemenu")) {
				try {
					socket.close();
					os.close();
					is.close();
					connected = false;
					start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (menu.contentEquals("mouse")
					|| menu.contentEquals("racing")
					|| menu.contentEquals("joystic")) {
				setContentView(R.layout.choicemenu);
				menu = "choicemenu";
				mouse = (ImageButton) findViewById(R.id.choice_mouse);
				racing = (ImageButton) findViewById(R.id.choice_racing);
				joystic = (ImageButton) findViewById(R.id.choice_joystic);
			}
		}
		return false;

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try {
			socket.close();
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}