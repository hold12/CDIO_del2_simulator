package controller;

import socket.ISocketController;
import socket.SocketController;
import weight.IWeightInterfaceController;
import weight.gui.WeightInterfaceControllerGUI;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main {
	public static void main(String[] args) {
		int port = 8000;
		try {
			port = args.length == 1 ? Integer.parseInt(args[0]) : 8000;
		} catch (Exception e) {}

		ISocketController socketHandler = new SocketController(port);
		IWeightInterfaceController weightController = new WeightInterfaceControllerGUI();
		//Injecting socket and uiController into mainController - Replace with improved versions...
		IMainController mainCtrl = new MainController(socketHandler, weightController);
		//.init and .start could be merged
		mainCtrl.start();
		
	}
}
