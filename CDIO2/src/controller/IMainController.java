package controller;

import console.IUIController;
import socket.ISocketController;

public interface IMainController {
	void init(ISocketController socketHandler, IUIController uiController);
	void start();

}
