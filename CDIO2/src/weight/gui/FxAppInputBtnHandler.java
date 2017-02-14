package weight.gui;

import weight.IWeightInterfaceController.InputType;

public class FxAppInputBtnHandler {
	private int lastBtnPressed = -1;
	private long lastTimePressed = -1;
	private char btnValue;
	
	public char onButtonPressed(int btn, InputType input_type, int delay){
		if(InputType.LOWER == input_type || InputType.UPPER == input_type){
			if(btn == lastBtnPressed && System.currentTimeMillis() < lastTimePressed+delay){
				btnValue = nextValue(btn, btnValue, input_type);
			} else {
				btnValue = InputType.LOWER == input_type ? FxApp.str_lower[btn].charAt(0) : FxApp.str_upper[btn].charAt(0);
			}

			lastTimePressed = System.currentTimeMillis();
			lastBtnPressed = btn;
		} else {
			btnValue = Character.forDigit(btn, 10);
		}
		return btnValue;
	}
	private char nextValue(int btn, char currentValue, InputType input_type){
		String str;
		switch(input_type){
		case LOWER: str = FxApp.str_lower[btn]; break;
		case UPPER: str = FxApp.str_upper[btn]; break;
		case NUMBERS:
		default: return currentValue;
		}
		int currentIndex = str.indexOf(currentValue);
		int index = (currentIndex +1) % str.length();
		return str.charAt(index);
	}
	
	public void reset(){
		lastBtnPressed = -1;
		lastTimePressed = -1;
		btnValue = '_';
	}

}
