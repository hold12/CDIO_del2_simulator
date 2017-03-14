package controller;

import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketInMessage;
import socket.SocketOutMessage;
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
	private KeyState keyState = KeyState.K1;
	private double bruttoWeight, tara = 0;

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
			//Makes this controller interested in messages from the socket
			socketHandler.registerObserver(this);
			//Starts socketHandler in own thread
			new Thread(socketHandler).start();

			weightController.registerObserver(this);
			new Thread(weightController).start();
		} else {
			System.err.println("No controllers injected!");
		}
	}

	//Listening for socket input
	@Override
	public void notify(SocketInMessage message) {
		System.out.println(message);
		switch (message.getType()) {
		case B:
			break;
		case D:
			weightController.showMessagePrimaryDisplay(message.getMessage()); 
			break;
		case Q:
			break;
		case RM204:
			break;
		case RM208:
			break;
		case S:
			break;
		case T:
			this.tara = this.bruttoWeight;
			this.bruttoWeight = 0;
			weightController.showMessagePrimaryDisplay(String.format("%.3f" , bruttoWeight) + "kg");
			break;
		case DW:
			break;
		case K:
			handleKMessage(message);
			break;
		case P111:
			break;
		}

	}

	private void handleKMessage(SocketInMessage message) {
		switch (message.getMessage()) {
		case "1" :
			this.keyState = KeyState.K1;
			break;
		case "2" :
			this.keyState = KeyState.K2;
			break;
		case "3" :
			this.keyState = KeyState.K3;
			break;
		case "4" :
			this.keyState = KeyState.K4;
			break;
		default:
			socketHandler.sendMessage(new SocketOutMessage("ES"));
			break;
		}
	}
	//Listening for UI input
	@Override
	public void notifyKeyPress(KeyPress keyPress) {
		System.out.println(keyPress);
		//TODO implement logic for handling input from ui
		switch (keyPress.getType()) {
		case SOFTBUTTON:
			break;
		case TARA:
            this.tara = this.bruttoWeight;
            this.bruttoWeight = 0;
		    weightController.showMessagePrimaryDisplay(String.format("%.3f" , bruttoWeight) + "kg");
			break;
		case TEXT:
			break;
		case ZERO:
			break;
		case C:
			break;
		case EXIT:
			break;
		case SEND:
			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3) ){
				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
			}
			break;
		}
	}

	@Override
	public void notifyWeightChange(double newWeight) {
		bruttoWeight = newWeight;
		weightController.showMessagePrimaryDisplay(String.format("%.3f" , bruttoWeight - tara) + "kg");
	}

}
