package controller;

import console.IWeightInterfaceController;
import socket.ISocketController;

public interface IMainController {
	void init(ISocketController socketHandler, IWeightInterfaceController uiController);
	void start();

}
