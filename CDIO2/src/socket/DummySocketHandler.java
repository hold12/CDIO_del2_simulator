package socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class DummySocketHandler implements ISocketController {
	Set<ISocketObserver> observers = new HashSet<ISocketObserver>();
	// Maybe add some way to keep track of multiple connections?
	private BufferedReader inStream;
	private DataOutputStream outStream;
	
			
	@Override
	public void registerObserver(ISocketObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unRegisterObserver(ISocketObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers(SocketInMessage message) {
		for (ISocketObserver socketObserver : observers) {
			socketObserver.notify(message);
		}
	}

	@Override
	public void sendMessage(SocketOutMessage message) {
		if (outStream!=null){
			try {
				outStream.writeBytes(message.getMessage());
				outStream.flush();
			} catch (IOException e) {
				// TODO Notify someone???
				e.printStackTrace();
			}
		} else {
			//TODO maybe tell someone that connection is closed?
		}
	}

	@Override
	public void run() {
		//TODO some logic for listening to a socket //(Using try with resources for auto-close of socket)
		try (ServerSocket listeningSocket = new ServerSocket(Port)){ 
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
			while (!(inLine = inStream.readLine().toUpperCase()).isEmpty()){
				 switch (inLine.split(" ")[0]) {
				case "RM":
					//TODO implement logic for RM command
					notifyObservers(new SocketInMessage("Got RM command!"));
					break;
				case "D":
					break;
				case "T":
					break;
				case "S":
					break;
				case "B":
					break;
				case "Q":
					break;
				default:
					sendMessage(new SocketOutMessage("Unknown Command"));
					break;
				}
			 }
		} catch (IOException e) {
			//TODO maybe notify mainController?
			e.printStackTrace();
		}
	}

}

