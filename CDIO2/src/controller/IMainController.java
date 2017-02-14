package controller;

import socket.ISocketController;
import weight.IWeightInterfaceController;

public interface IMainController {
	void init(ISocketController socketHandler, IWeightInterfaceController uiController);
	void start();

}
