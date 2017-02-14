package controller;

import socket.SocketController;
import weight.ConsoleWeightInterfaceController;
import weight.IWeightInterfaceController;
import socket.ISocketController;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main {

	public static void main(String[] args) {
		ISocketController socketHandler = new SocketController();
		IWeightInterfaceController weightController = new ConsoleWeightInterfaceController();
		//Injecting socket and uiController into mainController - Replace with improved versions...
		IMainController mainCtrl = new MainController(socketHandler, weightController);
		//.init and .start could be merged
		mainCtrl.start();
		
	}
}
