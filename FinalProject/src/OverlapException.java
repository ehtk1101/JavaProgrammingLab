// This class is for exception for overlapped express way for planning by user.

public class OverlapException extends Exception{
private String message;
	
	public OverlapException() {
		this.message = "You already selected this one! choose another one!"; // this message will display on the dialog
	}
	
	public String getMessage() {
		return message;
	}
}
