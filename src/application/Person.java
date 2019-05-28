package application;

import java.io.InputStream;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class Person implements Runnable{
	
	private Elevator elevator;
	private Animation animation;
	private int xPos;
	private int yPos;
	private InputStream isIn = null;
	private Image personImageIn = null;
	private InputStream isOut = null;
	private Image personImageOut = null;
	private ImageView imageView = null;
	private int startingFloor;
	private int endingFloor;
	private Thread personThread = null;
	private BorderPane root;
	private ArrayList<Person> al = null;
	private boolean isMoving;
	private boolean needToMoveForward;
	
	
	public Person(Elevator elevator, BorderPane root) {
		isMoving = true;
		needToMoveForward = false;
		this.root = root;
		this.elevator = elevator;
		this.startingFloor = (int)(Math.random() * 6) + 1;
		this.endingFloor = 1;
		if (startingFloor == 1) {
			startingFloor = 2;
		}
		this.xPos = 0;
		this.yPos = 560 - 110 * (startingFloor - 1);
		isIn = Person.class.getResourceAsStream("/humanGoIn.png");
		personImageIn = new Image(isIn);
		isOut = Person.class.getResourceAsStream("/humanGoOut.png");
		personImageOut = new Image(isOut);
		imageView = new ImageView(personImageIn);
		imageView.setLayoutX(xPos);
		imageView.setLayoutY(yPos);
		this.animation = new Animation(imageView, 11, 12, 0, 0, 61, 100, 800, 2);
		this.imageView.setViewport(new Rectangle2D(animation.getOffsetX(), animation.getOffsetY(), animation.getWidth(), animation.getHeight()));
		//do not start thread in constructor if u get NPE (object may not been fully created)
		personThread = new Thread(this);
		personThread.start();
	}

	@Override
	public void run() {
		moveToElevator();
		while(true) {
			if(this.getPositionInQueue() == 1) {
				break;
			}
		}
		synchronized(elevator) {
			synchronized(this) {
				callTheElevator(startingFloor);
				try {
					this.wait();
					moveInsideElevator();
					notifyTheQueue();
					moveToNeededFloor(endingFloor);
					this.wait();
					moveOutsideElevator();
					this.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		moveToTheExit();
	}
	
	public void moveToElevator() {
		animation.play();
		int positionInQueue = this.getPositionInQueue();
		int destinationPoint = 427 - (positionInQueue - 1) * 61;
		KeyValue xValue = new KeyValue(this.getImageView().xProperty(), destinationPoint);
		KeyFrame frame = new KeyFrame(Duration.millis(5000), xValue);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(frame);
		timeline.play();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				animation.stop();
				isMoving = false;
				if(needToMoveForward) {
					Person.this.moveForward();
				}
			}
		});
	}
	
	public int getPositionInQueue() {
		this.getAl(startingFloor);
		int position = al.indexOf(this);
		if(position == -1) {
			return 0;
		}else {
			return position + 1;
		}
	}
	
	public void moveInsideElevator() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		animation.play();
		KeyValue xValue = new KeyValue(this.getImageView().xProperty(), 515);
		KeyFrame frame = new KeyFrame(Duration.millis(1000), xValue);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(frame);
		timeline.play();
		elevator.setLoaded(true);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				animation.stop();
				elevator.closeDoor();
			}
		});
	}
	
	public void callTheElevator(int floor) {
		
		if(floor >= elevator.getFloor()) {
			elevator.moveUp(floor);
		}else if(floor < elevator.getFloor()) {
			elevator.moveDown(floor);
		}
			
	}
	
	public void moveToNeededFloor(int floor) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(floor > startingFloor) {
			elevator.moveUp(floor);
		}else if(floor < startingFloor) {
			elevator.moveDown(floor);
		}
		
	}
	
	public void moveOutsideElevator() {
		int x = 515;
		int y = 555;
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				
				root.getChildren().remove(imageView);
				imageView = new ImageView(personImageOut);
				imageView.setLayoutX(x);
				imageView.setLayoutY(y);
				imageView.setViewport(new Rectangle2D(animation.getOffsetX(), animation.getOffsetY(), animation.getWidth(), animation.getHeight()));
				root.getChildren().add(imageView);
				
			}
		});
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.animation = new Animation(imageView, 11, 12, 0, 0, 61, 100, 800, 2);
		animation.play();
		KeyValue xValue = new KeyValue(this.getImageView().xProperty(), -88);
		KeyFrame frame = new KeyFrame(Duration.millis(1000), xValue);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(frame);
		timeline.play();
		elevator.setLoaded(false);
		elevator.closeDoor();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				animation.stop();
			}
		});
	}
	
	public void moveToTheExit() {
		animation.play();
		isMoving = true;
		KeyValue xValue = new KeyValue(this.getImageView().xProperty(), -515);
		KeyFrame frame = new KeyFrame(Duration.millis(4000), xValue);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(frame);
		timeline.play();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				isMoving = false;
				animation.stop();
				root.getChildren().remove(imageView);
				try {
					Person.this.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void moveForward() {
		
		int destinationPoint = 366 - (getPositionInQueue() - 1) * 61;//427
		animation.play();
		KeyValue xValue = new KeyValue(this.getImageView().xProperty(), destinationPoint + 61);
		KeyFrame frame = new KeyFrame(Duration.millis(500), xValue);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(frame);
		timeline.play();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				animation.stop();
			}
		});
	}
	
	public void notifyTheQueue() {
		for(Person person: al) {
			synchronized (person) {
				if(person.isMoving) {
					person.needToMoveForward = true;
				}else {
					person.notify();
					person.moveForward();
				}
			}
		}
	}
	
	public ArrayList<Person> getAl(int floor) {
		
		switch(floor) {
		case 2:
			al = Main.alSecondFloor;
			break;
		case 3:
			al = Main.alThirdFloor;
			break;
		case 4:
			al = Main.alFourthFloor;
			break;
		case 5:
			al = Main.alFifthFloor;
			break;
		case 6:
			al = Main.alSixthFloor;
			break;
		default:
			break;
		}
		return al;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	
	public int getStartingFloor() {
		return startingFloor;
	}

	public int getEndingFloor() {
		return endingFloor;
	}

	public Thread getThread() {
		return personThread;
	}

}
