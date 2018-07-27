package functionalj.types.result;

@SuppressWarnings("javadoc")
public class ResultNotReadyException extends ResultNotAvailableException {
    
	private static final long serialVersionUID = 7838693114260918473L;

	public ResultNotReadyException() {
        
    }
    public ResultNotReadyException(String message, Throwable cause) {
        super(message, cause);
    }
    
}