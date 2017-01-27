package controller;

import console.DummyConsoleController;
import console.IUIController;
import socket.DummySocketHandler;
import socket.ISocketController;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main {

	public static void main(String[] args) {
		IMainController mainCtrl = new DummyMainController();
		ISocketController socketHandler = new DummySocketHandler();
		IUIController uiController = new DummyConsoleController();
		//Injecting socket and uiController - Replace with improved versions...
		mainCtrl.init(socketHandler, uiController);
		//.init and .start could be merged
		mainCtrl.start();
		
	}
}
