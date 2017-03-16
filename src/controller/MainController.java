package controller;

import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketInMessage;
import socket.SocketOutMessage;
import sun.misc.FloatingDecimal;
import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;

import java.util.Locale;

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
	private double grossWeight, tareWeight = 0;
	private String keysPressed = "";
	private SocketInMessage.SocketMessageType currentState;

	public MainController(ISocketController socketHandler, IWeightInterfaceController weightInterfaceController) {
		this.init(socketHandler, weightInterfaceController);
	}

	@Override
	public void init(ISocketController socketHandler, IWeightInterfaceController weightInterfaceController) {
		this.socketHandler = socketHandler;
		this.weightController=weightInterfaceController;
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
		switch (message.getType()) {
		case B:
			notifyWeightChange(FloatingDecimal.parseDouble(message.getMessage()));
			break;
		case D:
			String text = message.getMessage().substring(1,message.getMessage().length()-1);
			weightController.showMessagePrimaryDisplay(text);
			socketHandler.sendMessage(new SocketOutMessage("D A"));
			break;
		case Q:
			System.exit(0);
			break;
		case RM204:
			break;
		case RM208:
            String[] messageArray = message.getMessage().split("\" \"");
            String text1 = messageArray[0].substring(1);
            String text2 = messageArray[1];
            String text3 = messageArray[2].substring(0,messageArray[2].length()-1);

            //Numeric entry
            if (text3.startsWith("&")) {
                switch (text3.substring(0, 2)) {
                    case "&1":
                        weightController.changeInputType(IWeightInterfaceController.InputType.UPPER);
                        break;
                    case "&2":
                        weightController.changeInputType(IWeightInterfaceController.InputType.LOWER);
                        break;
                    case "&3":
                        weightController.changeInputType(IWeightInterfaceController.InputType.NUMBERS);
                        break;
                }
                text3 = text3.substring(2);
            }
            //show message to user
            weightController.showMessageSecondaryDisplay(text1);
            weightController.showMessagePrimaryDisplay(text2 + text3);
            socketHandler.sendMessage(new SocketOutMessage("RM20 B"));
            currentState = SocketInMessage.SocketMessageType.RM208;
            break;
		case S:
		    socketHandler.sendMessage(new SocketOutMessage("S S      " + String.format("%.3f", grossWeight - tareWeight) + " kg"));
			break;
		case T:
			this.tareWeight = this.grossWeight;
			this.grossWeight = 0;
			weightController.showMessagePrimaryDisplay(String.format("%.3f" , grossWeight) + "kg");
			break;
		case DW:
			this.tareWeight = 0;
			this.grossWeight = 0;
			weightController.showMessagePrimaryDisplay(String.format(Locale.US, "%.4f" , grossWeight) + " kg");
			socketHandler.sendMessage(new SocketOutMessage("DW A"));
			break;
		case K:
			handleKMessage(message);
			break;
		case P111:
			String P111text = message.getMessage().substring(1,message.getMessage().length()-1);
			if (P111text.length() > 30)
				P111text = P111text.substring(0,30);
			weightController.showMessageSecondaryDisplay(P111text);
			socketHandler.sendMessage(new SocketOutMessage("P111 A"));
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
		System.out.println(keyPress.getCharacter());
		//TODO implement logic for handling input from ui
		switch (keyPress.getType()) {
		case SOFTBUTTON:
			break;
		case TARA:
            this.tareWeight = this.grossWeight;
            this.grossWeight = 0;
		    weightController.showMessagePrimaryDisplay(String.format("%.3f" , grossWeight) + "kg");
			break;
		case TEXT:
            keysPressed+=keyPress.getCharacter();
		    weightController.showMessagePrimaryDisplay(keysPressed);
			break;
		case ZERO:
            this.tareWeight = 0;
            this.grossWeight = 0;
            weightController.showMessagePrimaryDisplay(String.format("%.3f" , grossWeight) + "kg");
			break;
		case CANCEL:
			break;
		case EXIT:
			System.exit(0);
			break;
		case SEND:
			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3) ){
				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
			}
			if(currentState == SocketInMessage.SocketMessageType.RM208) {
				socketHandler.sendMessage(new SocketOutMessage("RM20 A " + keysPressed));
			}
			currentState = null;
			keysPressed = "";
            break;
		}
	}

	@Override
	public void notifyWeightChange(double newWeight) {
		grossWeight = newWeight;
		weightController.showMessagePrimaryDisplay(String.format(Locale.US, "%.4f" , grossWeight - tareWeight) + " kg");
	}

}
