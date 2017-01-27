package console;

public class DummyConsoleController implements IUIController{

	@Override
	public void run() {
		// TODO Some logic to catch some input!
		
	}

	@Override
	public void registerObserver(IUIObserver uiObserver) {
		// TODO keep track of observers
	}

	@Override
	public void unRegisterObserver(IUIObserver uiObserver) {
		// TODO Remove observer from list
		
	}

	@Override
	public void notifyObservers(UIInMessage inMessage) {
		// TODO notify your observers
	}

	@Override
	public void showMenu() {
		// TODO Show some fancy menu!
	}

	@Override
	public void showMessage(String string) {
		// TODO Show a message to the user on the displat
		
	}


}
