package com.roy.wolf.exception;

public class WolfException extends Throwable {
	private static final long serialVersionUID = -8368858440168576472L;

	public WolfException() {
	}

	public WolfException(String detailMessage) {
		super(detailMessage);
	}

	public WolfException(Throwable cause) {
		super(cause);
	}

	public WolfException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}

}
