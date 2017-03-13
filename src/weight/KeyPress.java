package weight;

/**
 * DTO for user input
 * @author Christian
 *
 */
public class KeyPress {
	private int keyNumber;
	private char character;
	private KeyPressType type;
	
	public static KeyPress Exit(){
		return new KeyPress(0,'\\', KeyPressType.EXIT);
	}
	
	public static KeyPress Zero(){
		return new KeyPress(0,'\\', KeyPressType.ZERO);
	}
	
	public static KeyPress Tara(){
		return new KeyPress(0,'\\',KeyPressType.TARA);
	}
	
	public static KeyPress Send(){
		return new KeyPress(0,'\\', KeyPressType.SEND);
	}
	
	public static KeyPress SoftButton(int number){
		return new KeyPress(number, '\\', KeyPressType.SOFTBUTTON);
	}
	
	public static KeyPress Character(char character){
		return new KeyPress(0,character,KeyPressType.TEXT);
	}
	
	private KeyPress(int keyNumber, char character, KeyPressType type){
		this.keyNumber=keyNumber; this.character=character; this.type=type;
	}
	
	public int getKeyNumber() {
		return keyNumber;
	}

	public char getCharacter() {
		return character;
	}

	public KeyPressType getType() {
		return type;
	}
	
	public enum KeyPressType{
		SOFTBUTTON, TEXT, EXIT, ZERO, TARA, SEND, C	
	}
}
