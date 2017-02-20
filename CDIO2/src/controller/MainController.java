package controller;

import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketInMessage;
import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;
/**
 * MainController - integrating input from socket and ui. Implements ISocketObserver and IUIObserver to handle this.
 * @author Christian Budtz
 * @version 0.1 2017-01-24
 *
 */
public class MainController implements IMainController, ISocketObserver, IWeightInterfaceObserver {

	private ISocketController socketHandler;
	private IWeightInterfaceController weightController;

	public MainController(ISocketController socketHandler, IWeightInterfaceController uiController) {
		this.init(socketHandler, uiController);
	}

	@Override
	public void init(ISocketController socketHandler, IWeightInterfaceController uiController) {
		this.socketHandler = socketHandler;
		this.weightController=uiController;
	}

	@Override
	public void start() {
		if (socketHandler!=null && weightController!=null){
			//Make this controller interested in messages from the socket
			socketHandler.registerObserver(this);
			//Start socketHandler in own thread
			new Thread(socketHandler).start();
			//TODO set up weightController - Look above for inspiration (Keep it simple ;))
			
			
		} else {
			System.err.println("No controllers injected!");
		}
	}

	//Listening for socket input
	@Override
	public void notify(SocketInMessage message) {
		//TODO implement logic for handling input from socket
		System.out.println("Message from Socket received:" + message); //Some dummy code 
		weightController.showMessagePrimaryDisplay("Message received" + message); //Some dummy code
	}
	//Listening for UI input
	@Override
	public void notifyKeyPress(KeyPress keyPress) {
		//TODO implement logic for handling input from ui
		System.out.println("Got input from UI" + keyPress);

	}

	@Override
	public void notifyWeightChange(double newWeight) {
		// TODO Auto-generated method stub

	}

}
