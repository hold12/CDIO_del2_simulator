package controller;

import console.IUIController;
import console.IUIObserver;
import console.UIInMessage;
import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketInMessage;
/**
 * MainController - integrating input from socket and ui. Implements ISocketObserver and IUIObserver to handle this.
 * @author Christian Budtz
 * @version 0.1 2017-01-24
 *
 */
public class DummyMainController implements IMainController, ISocketObserver, IUIObserver {

	private ISocketController socketHandler;
	private IUIController uiController;

	@Override
	public void init(ISocketController socketHandler, IUIController uiController) {
		this.socketHandler = socketHandler;
		this.uiController=uiController;
	}

	@Override
	public void start() {
		if (socketHandler!=null && uiController!=null){
		//Make this controller interested in messages from the socket
		socketHandler.registerObserver(this);
		//Start socketHandler in own thread
		new Thread(socketHandler).start();
		//Sign up for consoleInput
		uiController.registerObserver(this);
		//Start uiController in own thread
		new Thread(uiController).start();
		} else {
			System.err.println("No controllers injected!");
			//TODO handle with some exception?
		}
	}

	//Listening for socket input
	@Override
	public void notify(SocketInMessage message) {
		//TODO implement logic for handling input from socket
		System.out.println("Message from Socket received:" + message);
		uiController.showMessage("Message received" + message); //Some dummy code
	}
	//Listening for UI input
	@Override
	public void notify(UIInMessage consoleInMessage) {
		//TODO implement logic for handling input from ui
		System.out.println("Got input from UI" + consoleInMessage);

	}

}
