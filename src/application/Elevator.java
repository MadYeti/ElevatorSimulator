package application;

import java.io.InputStream;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Elevator {

	private Animation animationOpenDoor;
	private Animation animationCloseDoor;
	private int xPos;
	private int yPos;
	private int floor;
	private boolean isLoaded;
	private InputStream is = null;
	private Image elevatorImage = null;
	private ImageView imageView = null;
	private InputStream isOpenDoor = null;
	private Image openDoorImage = null;
	private ImageView openDoorImageView = null;
	private InputStream isCloseDoor = null;
	private Image closeDoorImage = null;
	private ImageView closeDoorImageView = null;
	private ImageView personView = null;
	private ArrayList<Person> alFirstFloor = null;
	private ArrayList<Person> alSecondFloor = null;
	private ArrayList<Person> alThirdFloor = null;
	private ArrayList<Person> alFourthFloor = null;
	private ArrayList<Person> alFifthFloor = null;
	private ArrayList<Person> alSixthFloor = null;
	private ArrayList<Person> al = null;
	
	public Elevator(ArrayList<Person> alFirstFloor, ArrayList<Person> alSecondFloor, ArrayList<Person> alThirdFloor, ArrayList<Person> alFourthFloor, ArrayList<Person> alFifthFloor, ArrayList<Person> alSixthFloor) {
		this.alFirstFloor = alFirstFloor;
		this.alSecondFloor = alSecondFloor;
		this.alThirdFloor = alThirdFloor;
		this.alFourthFloor = alFourthFloor;
		this.alFifthFloor = alFifthFloor;
		this.alSixthFloor = alSixthFloor;
		this.xPos = 488;
		this.yPos = 564;
		this.floor = 1;
		this.isLoaded = false;
		is = Elevator.class.getResourceAsStream("/elevator.png");
		elevatorImage = new Image(is);
		imageView = new ImageView(elevatorImage);
		isOpenDoor = Elevator.class.getResourceAsStream("/elevatorOpening.png");
		openDoorImage = new Image(isOpenDoor);
		openDoorImageView = new ImageView(openDoorImage);
		animationOpenDoor = new Animation(openDoorImageView, 5, 5, 0, 0, 112, 100, 2000, 1);
		isCloseDoor = Elevator.class.getResourceAsStream("/elevatorClosing.png");
		closeDoorImage = new Image(isCloseDoor);
		closeDoorImageView = new ImageView(closeDoorImage);
		animationCloseDoor = new Animation(closeDoorImageView, 5, 5, 0, 0, 112, 100, 2000, 1);
		imageView.setLayoutX(xPos);
		imageView.setLayoutY(yPos);
		openDoorImageView.setLayoutX(xPos);
		openDoorImageView.setLayoutY(yPos);
		closeDoorImageView.setLayoutX(xPos);
		closeDoorImageView.setLayoutY(yPos);
	}

	public void moveUp(int floor) {
		int duration = (floor - this.getFloor()) * 1000 + 1;
		
		switch(floor) {
		case 2:
			al = alSecondFloor;
			break;
		case 3:
			al = alThirdFloor;
			break;
		case 4:
			al = alFourthFloor;
			break;
		case 5:
			al = alFifthFloor;
			break;
		case 6:
			al = alSixthFloor;
			break;
		default:
			break;
		}
		
		
		KeyValue yValueOpenDoor = new KeyValue(this.getOpenDoorImageView().yProperty(), -(549 - 110 * (6 - floor)));
		KeyFrame frameOpenDoor = new KeyFrame(Duration.millis(duration), yValueOpenDoor);
		Timeline timelineOpenDoor = new Timeline();
		timelineOpenDoor.getKeyFrames().add(frameOpenDoor);
		KeyValue yValueCloseDoor = new KeyValue(this.getCloseDoorImageView().yProperty(), -(549 - 110 * (6 - floor)));
		KeyFrame frameCloseDoor = new KeyFrame(Duration.millis(duration), yValueCloseDoor);
		Timeline timelineCloseDoor = new Timeline();
		timelineCloseDoor.getKeyFrames().add(frameCloseDoor);
		KeyValue yValueElevator = new KeyValue(this.getImageView().yProperty(), -(549 - 110 * (6 - floor)));
		KeyFrame frameElevator = new KeyFrame(Duration.millis(duration), yValueElevator);
		Timeline timelineElevator = new Timeline();
		timelineElevator.getKeyFrames().add(frameElevator);
		timelineCloseDoor.play();
		timelineOpenDoor.play();
		timelineElevator.play();
		timelineElevator.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				Elevator.this.setFloor(floor);
				openDoor();
				synchronized (al.get(0)) {
					al.get(0).notify();
				}
				alFirstFloor.add(al.get(0));
				al.remove(0);
				al = null;
			}
			
		});
		
	}
	
	public void moveDown(int floor) {
		int duration = (this.getFloor() - floor) * 1000 + 1;
		
		KeyValue yValueOpenDoor = new KeyValue(this.getOpenDoorImageView().yProperty(), -549 + (110 * (6 - floor)));
		KeyFrame frameOpenDoor = new KeyFrame(Duration.millis(duration), yValueOpenDoor);
		Timeline timelineOpenDoor = new Timeline();
		timelineOpenDoor.getKeyFrames().add(frameOpenDoor);
		KeyValue yValueCloseDoor = new KeyValue(this.getCloseDoorImageView().yProperty(), -549 + (110 * (6 - floor)));
		KeyFrame frameCloseDoor = new KeyFrame(Duration.millis(duration), yValueCloseDoor);
		Timeline timelineCloseDoor = new Timeline();
		timelineCloseDoor.getKeyFrames().add(frameCloseDoor);
		KeyValue yValueElevator = new KeyValue(this.getImageView().yProperty(), -549 + (110 * (6 - floor)));
		KeyFrame frameElevator = new KeyFrame(Duration.millis(duration), yValueElevator);
		Timeline timelineElevator = new Timeline();
		personView = alFirstFloor.get(0).getImageView();
		KeyValue yValuePerson = new KeyValue(personView.yProperty(), 110 * (alFirstFloor.get(0).getStartingFloor() - floor));
		KeyFrame framePerson = new KeyFrame(Duration.millis(duration), yValuePerson);
		Timeline timelinePerson = new Timeline();
		timelinePerson.getKeyFrames().add(framePerson);
		timelinePerson.play();
		timelineElevator.getKeyFrames().add(frameElevator);
		timelineCloseDoor.play();
		timelineOpenDoor.play();
		timelineElevator.play();
		timelineElevator.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Elevator.this.setFloor(floor);
				openDoor();
				synchronized (alFirstFloor.get(0)) {
					alFirstFloor.get(0).notify();
				}
			}
			
		});
		
	}
	
	public void openDoor() {
		animationOpenDoor.playFromStart();
	}
	
	public void closeDoor() {
		animationCloseDoor.playFromStart();
		animationCloseDoor.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				animationOpenDoor = new Animation(openDoorImageView, 5, 5, 0, 0, 112, 100, 2000, 1);
				animationCloseDoor = new Animation(closeDoorImageView, 5, 5, 0, 0, 112, 100, 2000, 1);
				if(!Elevator.this.isLoaded) {
					synchronized (alFirstFloor.get(0)) {
						alFirstFloor.get(0).notify();
					}
					alFirstFloor.remove(0);
				}
			}
		});
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}
	
	public void setAl(ArrayList<Person> al) {
		this.al = al;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public ImageView getOpenDoorImageView() {
		return openDoorImageView;
	}

	public ImageView getCloseDoorImageView() {
		return closeDoorImageView;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}
	
}
