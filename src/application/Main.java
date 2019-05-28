package application;
	
import java.io.InputStream;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	protected static ArrayList<Person> alFirstFloor = new ArrayList<Person>();
	protected static ArrayList<Person> alSecondFloor = new ArrayList<Person>();
	protected static ArrayList<Person> alThirdFloor = new ArrayList<Person>();
	protected static ArrayList<Person> alFourthFloor = new ArrayList<Person>();
	protected static ArrayList<Person> alFifthFloor = new ArrayList<Person>();
	protected static ArrayList<Person> alSixthFloor = new ArrayList<Person>();
	private EventHandler<KeyEvent> eventHandler = null;
	private Person person = null;
	private InputStream ceiling = Main.class.getResourceAsStream("/ceilings.png");
	private InputStream firstFloor = Main.class.getResourceAsStream("/firstFloor.png");
	private InputStream secondFloor = Main.class.getResourceAsStream("/secondFloor.png");
	private InputStream thirdFloor = Main.class.getResourceAsStream("/thirdFloor.png");
	private InputStream fourthFloor = Main.class.getResourceAsStream("/fourthFloor.png");
	private InputStream fifthFloor = Main.class.getResourceAsStream("/fifthFloor.png");
	private InputStream sixthFloor = Main.class.getResourceAsStream("/sixthFloor.png");
	
	
	@Override
	public void start(Stage primaryStage) {
		
		BorderPane root = new BorderPane();
		Image ceilingImage = new Image(ceiling);
		ImageView ceilingImageView = new ImageView(ceilingImage);
		Image firstFloorImage = new Image(firstFloor);
		ImageView firstFloorImageView = new ImageView(firstFloorImage);
		Image secondFloorImage = new Image(secondFloor);
		ImageView secondFloorImageView = new ImageView(secondFloorImage);
		Image thirdFloorImage = new Image(thirdFloor);
		ImageView thirdFloorImageView = new ImageView(thirdFloorImage);
		Image fourthFloorImage = new Image(fourthFloor);
		ImageView fourthFloorImageView = new ImageView(fourthFloorImage);
		Image fifthFloorImage = new Image(fifthFloor);
		ImageView fifthFloorImageView = new ImageView(fifthFloorImage);
		Image sixthFloorImage = new Image(sixthFloor);
		ImageView sixthFloorImageView = new ImageView(sixthFloorImage);
		Elevator elevator = new Elevator(alFirstFloor, alSecondFloor, alThirdFloor, alFourthFloor, alFifthFloor, alSixthFloor);
		
		eventHandler = new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent keyEvent) {
				
				if (keyEvent.getCode() == KeyCode.ENTER) {
					person = new Person(elevator, root);
					switch(person.getStartingFloor()) {
						case 1:
							alFirstFloor.add(person);
							break;
						case 2:
							alSecondFloor.add(person);
							break;
						case 3:
							alThirdFloor.add(person);
							break;
						case 4:
							alFourthFloor.add(person);
							break;
						case 5:
							alFifthFloor.add(person);
							break;
						case 6:
							alSixthFloor.add(person);
							break;
						default:
							break;
					}
					root.getChildren().add(person.getImageView());
                }
				
			}
		};
		elevator.getCloseDoorImageView().setOpacity(0.55);
		elevator.getOpenDoorImageView().setOpacity(0.55);
		root.getChildren().addAll(ceilingImageView, elevator.getImageView(), elevator.getOpenDoorImageView(), elevator.getCloseDoorImageView(), firstFloorImageView, secondFloorImageView, thirdFloorImageView, fourthFloorImageView, fifthFloorImageView, sixthFloorImageView);
		sixthFloorImageView.setLayoutX(10);
		sixthFloorImageView.setLayoutY(5);
		fifthFloorImageView.setLayoutX(10);
		fifthFloorImageView.setLayoutY(120);
		fourthFloorImageView.setLayoutX(10);
		fourthFloorImageView.setLayoutY(230);
		thirdFloorImageView.setLayoutX(10);
		thirdFloorImageView.setLayoutY(340);
		secondFloorImageView.setLayoutX(10);
		secondFloorImageView.setLayoutY(450);
		firstFloorImageView.setLayoutX(10);
		firstFloorImageView.setLayoutY(560);
		
		Scene scene = new Scene(root, 600, 664);
		scene.setOnKeyPressed(eventHandler);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
		    @Override
		    public void handle(WindowEvent t) {
		        Platform.exit();
		        System.exit(0);
		    }
		    
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
