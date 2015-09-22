package com.eduworks.resolver.exception;

public class SoftException extends RuntimeException
{
	String	message;

	@Override
	public String getMessage()
	{
		return message;
	}

	public SoftException(String message)
	{
		this.message = message;
	}
}
