package weight.gui;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import weight.IWeightInterfaceController.InputType;

public class FxApp extends Application {
	private Text txtload, txtbottom;
	private Text[] txtsft = new Text[6];
	private Text[] txtinfo = new Text[4];
	private TextField userInput;
	private Slider slider;
	private Button btnexit, btnzero, btntara, btnsend, btnshift; 
	private Button[] btnsft = new Button[6];
	private Button[] btnnum = new Button[10];
	public static final String[] str_lower = {".", "abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vxy", "z"};
	public static final String[] str_upper = {".", "ABC", "DEF", "GHI", "JKL", "MNO", "PQR", "STU", "VXY", "Z"};
	private InputType inputType = InputType.NUMBERS;
	private boolean userInputPlaceholderTentative = false, userInputTypeLocked = false;
	private int caretPosition = 0;
	private WeightGUI l;
	private Timer timer; 
	final int DELAY = 333;

	public static void go(){
		launch();
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("local.fxml"));
			StackPane root = (StackPane) loader.load();

			Scene scene = new Scene(root,974,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setX(0);
			primaryStage.setY(0);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override public void handle(WindowEvent t) { System.exit(0); }
			});

			txtload = (Text) loader.getNamespace().get("txt_load");
			txtinfo[0] = (Text) loader.getNamespace().get("txt_info_1");
			txtinfo[1] = (Text) loader.getNamespace().get("txt_info_2");
			txtinfo[2] = (Text) loader.getNamespace().get("txt_info_3");
			txtinfo[3] = (Text) loader.getNamespace().get("txt_info_4");
			txtbottom = (Text) loader.getNamespace().get("txt_bottom");

			userInput = (TextField) loader.getNamespace().get("userInput");
			userInput.caretPositionProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					/* 
					 * when button is pressed, the textfield looses focus and caret is moved to 0.
					 * Hence the previousCaretPosition is the actual position.
					 */
//					System.out.println("onChange "+oldValue+" --> "+newValue+"    ("+caretPosition+")");
					if(newValue.intValue() == 0 && caretPosition != 0){
//						System.out.println("Caret forced");
						userInput.positionCaret(caretPosition);
					}
				}
			});
			userInput.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					if(userInputPlaceholderTentative){
						userInputPlaceholderTentative = false;
						userInput.setText(e.getText());
						caretPosition = 1;
						userInput.positionCaret(caretPosition);
					}
				}
			});

			for(int i=0; i < 6; i++){
				txtsft[i] = (Text) loader.getNamespace().get("txt_softkey_"+(i+1));
				btnsft[i] = (Button) loader.getNamespace().get("btn_softkey_"+(i+1));
				final int j = i;
				btnsft[i].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						onSoftKeyPressed(j); 
					}
				});
			}

			slider = (Slider) loader.getNamespace().get("slider");
			slider.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					onSliderValueChange(newValue.doubleValue());
				}
			});

			btnexit = (Button) loader.getNamespace().get("btn_exit");
			btnexit.setOnAction(new EventHandler<ActionEvent>() { 
				@Override public void handle(ActionEvent event) { onExitButtonPressed(); }
			});
			
			btnzero = (Button) loader.getNamespace().get("btn_zero");
			btnzero.setOnAction(new EventHandler<ActionEvent>() { 
				@Override public void handle(ActionEvent event) { onZeroButtonPressed(); }
			});
			
			btntara = (Button) loader.getNamespace().get("btn_tara");
			btntara.setOnAction(new EventHandler<ActionEvent>() { 
				@Override public void handle(ActionEvent event) { onTaraButtonPressed(); }
			});
			
			btnsend = (Button) loader.getNamespace().get("btn_send");
			btnsend.setOnAction(new EventHandler<ActionEvent>() { 
				@Override public void handle(ActionEvent event) { onSendButtonPressed(); }
			});
			
			final FxAppInputBtnHandler inputHandler = new FxAppInputBtnHandler();
			for(int i=0; i < 10; i++){
				final int btn = i;
				btnnum[i] = (Button) loader.getNamespace().get("btn_"+(i));
				btnnum[i].setOnAction(new EventHandler<ActionEvent>() { 
					@Override public void handle(ActionEvent event) { onNumBtnPressed(inputHandler, btn); }
				});
			}

			btnshift = (Button) loader.getNamespace().get("btn_shift");
			btnshift.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) { onShiftBtnPressed(); }
			});

			btnzero = (Button) loader.getNamespace().get("btn_zero");
			btnzero.setOnAction(new EventHandler<ActionEvent>() { 
				@Override public void handle(ActionEvent event) { onZeroButtonPressed(); }
			});

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		WeightGUI.getInstance().setApp(this);
	}
	public FxApp() {} 

	public void setSim(WeightGUI l){
		this.l = l;
	}


	//output
	private void onSliderValueChange(Double newValue){ l.onSliderValueChange(newValue); }
	private void onExitButtonPressed(){ l.onExitButtonPressed(); }
	private void onZeroButtonPressed(){ l.onZeroButtonPressed(); }
	private void onTaraButtonPressed(){ l.onTaraButtonPressed(); }
	private void onSendButtonPressed(){ l.onSendButtonPressed(); }
	private void onNumBtnPressed(final FxAppInputBtnHandler inputHandler, final int btn) {
		char c = inputHandler.onButtonPressed(btn, inputType, DELAY);
		if(timer == null) timer = new Timer();
		else {
			timer.cancel();
			timer = new Timer();
		}
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				l.onNumBtnPressed(c);
			}
		}, DELAY);
		
		userInput.requestFocus();
	}
	private void onShiftBtnPressed() {
		toggle_input_type();
		userInput.requestFocus();
		userInput.positionCaret(caretPosition);
	}
	private void onSoftKeyPressed(int i){
		l.onSoftBtnPressed(i);
	}

	//input
	public void printLoad(final String load) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				
				txtload.setText(load.length() > 7 ? load.substring(0, 7) : load);
			}
		});
	}
	public void printBottom(final String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtbottom.setText(msg);
				txtinfo[3].setVisible(false);
				userInput.setVisible(false);
				txtbottom.setVisible(true);
			}
		});
	}
	public void softkeysHide() {
		for(Text t : txtsft) { t.setText(""); }
	}
	public void softkeysShow(String[] sftkeys, int firstSoftkey, boolean[] sftkeysChecked) {
		int i = 0;
		while(i < txtsft.length && i+firstSoftkey < sftkeys.length){
			int index = i+firstSoftkey;
			boolean checked = false;
			try{
				checked = sftkeysChecked[index];
			} catch(ArrayIndexOutOfBoundsException e) {
				checked = false;
			}
			txtsft[i].setText(sftkeys[index] + (checked ? "<" : ""));
			i++;
		}
	}

	//internal
	private void toggle_input_type(){
		if(userInputTypeLocked) return;
		switch(inputType){
		case LOWER: setButtonsUpper(); break;
		case UPPER: setButtonsNumbers(); break;
		case NUMBERS: setButtonsLower(); break;
		}
	}
	public void setButtonsLower(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<btnnum.length; i++){
					btnnum[i].setText(str_lower[i]);
				}
				inputType = InputType.LOWER;
			}
		});
	}
	public void setButtonsUpper(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<btnnum.length; i++){
					btnnum[i].setText(str_upper[i]);
				}
				inputType = InputType.UPPER;
			}
		});
	}
	public void setButtonsNumbers(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<btnnum.length; i++){
					btnnum[i].setText(""+i);
				}
				inputType = InputType.NUMBERS;
			}
		});
	}












}
