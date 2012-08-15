import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;

public class myserver {
	ServerSocket myserverSocket;
	Socket connection = null;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	String message;
	Scanner input = new Scanner(System.in);
	float[] n = { 0, 0, 0 };
	float[] o = { 0, 0, 0 };
	Robot robo;
	long t = new Date().getTime(), tu = t;
	Boolean connected = false, started = false;
	JLabel output;

	myserver(JLabel a) {
		output = a;
	}

	public void run(int port) {
		try {
			try {
				robo = new Robot();
			} catch (AWTException e) {

			}
			if (!started) {
				myserverSocket = new ServerSocket(port, 10);
				started = true;
			}
		} catch (Exception e) {
		}
	}

	public void getinput() {
		startwaiting();
		do {
			try {
				message = (String) in.readObject();	
				System.out.println(message);
				evaluate(message);
			} catch (Exception e) {
				output.setText("Press Start Button to Start Server...!!");
			}
		} while (connected && !message.equals("bye"));
	}

	public void startwaiting() {
		if (started && !connected) {
			try {
				output.setText("Press Connect Button on Phone...!!!");
				connection = myserverSocket.accept();
				output.setText("Connection received from "
						+ connection.getInetAddress().getHostName());
				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(connection.getInputStream());
				sendMessage("Connection successful");
				output.setText("Connection Successful");
				connected = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				output.setText("Error With Streams");
			}
		}
	}

	public void stopserver() {
		if (connected || started) {
			try {
				connected = false;
				started = false;
				in.close();
				out.close();
				myserverSocket.close();
				output.setText("Stopped");

			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}
	}

	public void evaluate(String message) {
		// TODO Auto-generated method stub
		String[] data = message.split(" ");
		switch (data[0]) {
		case "M":

			try {
				n[0] = new Float(data[1]);
				n[1] = new Float(data[2]);
				Thread mouse = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						Point a = MouseInfo.getPointerInfo().getLocation();
						int x = (int) a.getX();
						int y = (int) a.getY();
						double scale = 1;
						int xnew = (int) (scale * n[1] + x);
						int ynew = (int) (scale * n[0] + y);
						robo.mouseMove(xnew, ynew);
					}
				});
				mouse.run();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			break;

		case "KL":
			try {
				switch (data[1]) {
				case "0":
					robo.mousePress(InputEvent.BUTTON1_MASK);
					robo.mouseRelease(InputEvent.BUTTON1_MASK);
					break;
				case "1":
					robo.mousePress(InputEvent.BUTTON1_MASK);
					break;
				case "2":
					robo.mouseRelease(InputEvent.BUTTON1_MASK);
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			break;
		case "KR":
			try {
				switch (data[1]) {
				case "0":
					robo.mousePress(InputEvent.BUTTON3_MASK);
					robo.mouseRelease(InputEvent.BUTTON3_MASK);
					break;
				case "1":
					robo.mousePress(InputEvent.BUTTON3_MASK);
					break;
				case "2":
					robo.mouseRelease(InputEvent.BUTTON3_MASK);
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			break;
		case "S":
			try {
				switch (data[1]) {
				case "0":
					robo.mouseWheel(-1);
					break;
				case "1":
					robo.mouseWheel(+1);
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			break;
		case "R":
			n[1] = new Float(data[2]);
			if (n[1] < -1) {
				robo.keyRelease(KeyEvent.VK_RIGHT);
				robo.keyPress(KeyEvent.VK_LEFT);
				//robo.delay(30);
			} else if (n[1] > 1) {
				robo.keyRelease(KeyEvent.VK_LEFT);
				robo.keyPress(KeyEvent.VK_RIGHT);
				////robo.delay;
			} else {
				robo.keyRelease(KeyEvent.VK_LEFT);
				robo.keyRelease(KeyEvent.VK_RIGHT);
				////robo.delay(30);
			}
			break;

		case "J":
			n[0] = new Float(data[1]);
			n[1] = new Float(data[2]);
			if (n[1] < -1) {
				robo.keyRelease(KeyEvent.VK_RIGHT);
				robo.keyPress(KeyEvent.VK_LEFT);
				//robo.delay(30);
			} else if (n[1] > 1) {
				robo.keyRelease(KeyEvent.VK_LEFT);
				robo.keyPress(KeyEvent.VK_RIGHT);
				//robo.delay(30);
			} else {
				robo.keyRelease(KeyEvent.VK_LEFT);
				robo.keyRelease(KeyEvent.VK_RIGHT);
				//robo.delay(30);
			}
			if (n[0] < -1) {
				robo.keyRelease(KeyEvent.VK_DOWN);
				robo.keyPress(KeyEvent.VK_UP);
				//robo.delay(30);
			} else if (n[0] > 1) {
				robo.keyRelease(KeyEvent.VK_UP);
				robo.keyPress(KeyEvent.VK_DOWN);
				//robo.delay(30);
			} else {
				robo.keyRelease(KeyEvent.VK_DOWN);
				robo.keyRelease(KeyEvent.VK_UP);
				//robo.delay(30);
			}
			break;

		case "K":
			switch (data[2]) {
			case "0":
				switch (data[1]) {
				case "a":
					robo.keyPress(KeyEvent.VK_A);
					robo.keyRelease(KeyEvent.VK_A);
					break;
				case "b":
					robo.keyPress(KeyEvent.VK_B);
					robo.keyRelease(KeyEvent.VK_B);
					break;
				case "c":
					robo.keyPress(KeyEvent.VK_C);
					robo.keyRelease(KeyEvent.VK_C);
					break;
				case "x":
					robo.keyPress(KeyEvent.VK_X);
					robo.keyRelease(KeyEvent.VK_X);
					break;
				case "y":
					robo.keyPress(KeyEvent.VK_Y);
					robo.keyRelease(KeyEvent.VK_Y);
					break;
				case "z":
					robo.keyPress(KeyEvent.VK_Z);
					robo.keyRelease(KeyEvent.VK_Z);
					break;
				case "esc":
					robo.keyPress(KeyEvent.VK_ESCAPE);
					robo.keyRelease(KeyEvent.VK_ESCAPE);
					break;
				case "enter":
					robo.keyPress(KeyEvent.VK_ENTER);
					robo.keyRelease(KeyEvent.VK_ENTER);
					break;
				case "space":
					robo.keyPress(KeyEvent.VK_SPACE);
					robo.keyRelease(KeyEvent.VK_SPACE);
					break;
				case "up":
					robo.keyPress(KeyEvent.VK_UP);
					robo.keyRelease(KeyEvent.VK_UP);
					break;
				case "down":
					robo.keyPress(KeyEvent.VK_DOWN);
					robo.keyRelease(KeyEvent.VK_DOWN);
					break;
				}

			case "1":
				switch (data[1]) {
				case "up":
					robo.keyPress(KeyEvent.VK_UP);
					break;
				case "down":
					robo.keyPress(KeyEvent.VK_DOWN);
					break;
				}
				break;
			case "2":
				switch (data[1]) {
				case "up":
					robo.keyRelease(KeyEvent.VK_UP);
					break;
				case "down":
					robo.keyRelease(KeyEvent.VK_DOWN);
					break;
				}
				break;
			}
			break;
		default:
			output.setText(message);
		}

	}

	public void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
		}
	}

}
