package console;

public interface IWeightInterfaceController extends Runnable {
	
	void registerObserver(IWeightInterfaceObserver uiObserver);
	void unRegisterObserver(IWeightInterfaceObserver uiObserver);
	
	void showMessagePrimaryDisplay(String string);
	//TODO add other functions?

}
