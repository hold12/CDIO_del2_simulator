package weight;

public interface IWeightInterfaceObserver {
	void notifyKeyPress(KeyPress keypress);
	void notifyWeightChange(double newWeight);

}
