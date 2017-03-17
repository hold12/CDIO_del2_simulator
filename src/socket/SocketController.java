package socket;

import socket.SocketInMessage.SocketMessageType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class SocketController implements ISocketController {
	Set<ISocketObserver> observers = new HashSet<ISocketObserver>();
	//TODO Maybe add some way to keep track of multiple connections?
	private BufferedReader inStream;
	private DataOutputStream outStream;
	private int port;

	public SocketController(int port) {
		this.port = port;
	}

	@Override
	public void registerObserver(ISocketObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unRegisterObserver(ISocketObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void sendMessage(SocketOutMessage message) {
		if (outStream!=null){
			PrintWriter writer = new PrintWriter(outStream);
			writer.println(message.getMessage());
			writer.flush();

		} else {
			//TODO maybe tell someone that connection is closed?
		}
	}

	@Override
	public void run() {
		//TODO some logic for listening to a socket //(Using try with resources for auto-close of socket)
 		try (ServerSocket listeningSocket = new ServerSocket(port)){
			while (true){
				waitForConnections(listeningSocket);
			}
		} catch (IOException e1) {
			// TODO Maybe notify MainController?
			e1.printStackTrace();
		}


	}

	private void waitForConnections(ServerSocket listeningSocket) {
		try {
			Socket activeSocket = listeningSocket.accept(); //Blocking call
			inStream = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));
			outStream = new DataOutputStream(activeSocket.getOutputStream());
			String inLine;
			//.readLine is a blocking call
			//TODO How do you handle simultaneous input and output on socket?
			//TODO this only allows for one open connection - how would you handle multiple connections?
			while (true){
				inLine = inStream.readLine();
				if (inLine==null) break;
				switch (inLine.split(" ")[0]) {
				case "RM20": // Display a message in the secondary display and wait for response
					if (inLine.split(" ")[1].equals("8"))
						notifyObservers(new SocketInMessage(SocketMessageType.RM208, inLine.substring(7)));
					break;
				case "D":// Display a message in the primary display
					notifyObservers(new SocketInMessage(SocketMessageType.D, inLine.substring(2)));
					break;
				case "DW": //Clear primary display
                    notifyObservers(new SocketInMessage(SocketMessageType.DW, null));
                    break;
                    case "P111": //Show something in secondary display
                    notifyObservers(new SocketInMessage(SocketMessageType.P111, inLine.substring(5)));
                    break;
                    case "T": // Tare the weight
                    notifyObservers(new SocketInMessage(SocketMessageType.T, null));
                    break;
                    case "S": // Request the current load
                    notifyObservers(new SocketInMessage(SocketMessageType.S, null));
                    break;
                    case "K":
					if (inLine.split(" ").length>1){
						notifyObservers(new SocketInMessage(SocketMessageType.K, inLine.split(" ")[1]));
					}
					break;
				case "B": // Set the load
					notifyObservers(new SocketInMessage(SocketMessageType.B, inLine.substring(2)));
					break;
				case "Q": // Quit
					activeSocket.close();
					notifyObservers(new SocketInMessage(SocketMessageType.Q,null));
					break;
				default: //Something went wrong?
					//TODO implement
					break;
				}
			}
		} catch (IOException e) {
			//TODO maybe notify mainController?
			e.printStackTrace();
		}
	}

	private void notifyObservers(SocketInMessage message) {
		for (ISocketObserver socketObserver : observers) {
			socketObserver.notify(message);
		}
	}

}

