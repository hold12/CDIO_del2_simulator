package controller;

import socket.SocketController;
import weight.IWeightInterfaceController;
import weight.console.ConsoleWeightInterfaceController;
import weight.gui.WeightGUI;
import socket.ISocketController;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main {
	private static boolean gui= true;

	public static void main(String[] args) {
		ISocketController socketHandler = new SocketController();
		IWeightInterfaceController weightController = gui? new WeightGUI(): new ConsoleWeightInterfaceController();
		//Injecting socket and uiController into mainController - Replace with improved versions...
		IMainController mainCtrl = new MainController(socketHandler, weightController);
		//.init and .start could be merged
		mainCtrl.start();
		
	}
}
