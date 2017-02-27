package weight.gui;

import java.util.ArrayList;
import java.util.Arrays;

import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;

public class WeightGUI implements IWeightInterfaceController {
	private static WeightGUI instance;
	private ArrayList<IWeightInterfaceObserver> observers = new ArrayList<IWeightInterfaceObserver>(); 
	private FxApp fxApp;

	public WeightGUI() { instance = this; }
	@Override public void run() { FxApp.go(); }
	public static WeightGUI getInstance() { return instance; }
	public void setApp(FxApp fxApp) { this.fxApp = fxApp; fxApp.setSim(this); }
	

	// System --> GUI
	// Methods required by interface
	@Override
	public void registerObserver(IWeightInterfaceObserver uiObserver) {
		this.observers.add(uiObserver);
	}
	@Override
	public void unRegisterObserver(IWeightInterfaceObserver uiObserver) {
		this.observers.remove(uiObserver);
	}
	@Override
	public void showMessagePrimaryDisplay(String string) {
		fxApp.printLoad(string);
	}
	@Override
	public void showMessageSecondaryDisplay(String string) {
		fxApp.printBottom(string);
	}
	@Override
	public void changeInputType(InputType type) {
		switch(type){
		case LOWER: fxApp.setButtonsLower(); break;
		case NUMBERS: fxApp.setButtonsNumbers(); break;
		case UPPER: fxApp.setButtonsUpper(); break;
		default: fxApp.setButtonsLower(); break;
		}
		
	}
	@Override
	public void setSoftButtonTexts(String[] texts) {
		int firstSoftkey = 0;
		if (texts == null) {
			texts = new String[0];
		}
		boolean[] sftkeysChecked = new boolean[texts.length];
		Arrays.fill(sftkeysChecked, false);
		fxApp.softkeysShow(texts, firstSoftkey, sftkeysChecked);
	}
	
	
	// GUI --> System
	public void onSliderValueChange(Double newValue) {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyWeightChange(newValue / 1000);
		}
	}
	void onExitButtonPressed() {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyKeyPress(KeyPress.Exit());
		}
	}
	void onZeroButtonPressed() {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyKeyPress(KeyPress.Zero());
		}
	}
	void onTaraButtonPressed() {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyKeyPress(KeyPress.Tara());
		}
		
	}
	void onSendButtonPressed() {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyKeyPress(KeyPress.Send());
		}
	}
	public void onNumBtnPressed(char btn){
		for (IWeightInterfaceObserver o : observers) {
			o.notifyKeyPress(KeyPress.Character(btn));
		}
	}
	public void onSoftBtnPressed(int i) {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyKeyPress(KeyPress.SoftButton(i));
		}
	}
	
	
	
	
	
	
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
