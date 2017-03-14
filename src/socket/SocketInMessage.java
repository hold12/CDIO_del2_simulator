package socket;

public class SocketInMessage {
	private SocketMessageType type;
	private String message;

	public SocketInMessage(SocketMessageType type, String message) {
		this.message=message;
		this.type=type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public SocketMessageType getType() {
		return type;
	}
	
	public enum SocketMessageType{
		RM204, RM208, D, DW, T, S, B, Q, P111, K
	}

}
