package weight;

public interface IWeightInterfaceController extends Runnable {
	
	void registerObserver(IWeightInterfaceObserver uiObserver);
	void unRegisterObserver(IWeightInterfaceObserver uiObserver);
	
	void showMessagePrimaryDisplay(String string);
	void showMessageSecondaryDisplay(String string);
	void changeInputType(InputType type);
	void setSoftButtonTexts(String[] texts);
	
	public enum InputType {
		UPPER, LOWER, NUMBERS
	}

}
