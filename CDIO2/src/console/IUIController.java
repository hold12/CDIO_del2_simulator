package console;

public interface IUIController extends Runnable {
	
	void registerObserver(IUIObserver uiObserver);
	void unRegisterObserver(IUIObserver uiObserver);
	void notifyObservers(UIInMessage inMessage);
	
	void showMenu();
	void showMessage(String string);

}
