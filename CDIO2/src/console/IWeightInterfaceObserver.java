package console;

public interface IWeightInterfaceObserver {
	void notifyKeyPress(UIInMessage keypress);
	void notifyWeightChange(double newWeight);

}
