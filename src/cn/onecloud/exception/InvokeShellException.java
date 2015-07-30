package cn.onecloud.exception;
/**
 * @author czw
 *  Exception thrown to indicate that invoke the shell command have error.  
 */
public class InvokeShellException extends Exception
{
	private static final long serialVersionUID = 1L;

	public InvokeShellException()
	{
		super();
	}

	public InvokeShellException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace)
	{
		//super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvokeShellException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvokeShellException(String message)
	{
		super(message);
	}

	public InvokeShellException(Throwable cause)
	{
		super(cause);
	}
}
