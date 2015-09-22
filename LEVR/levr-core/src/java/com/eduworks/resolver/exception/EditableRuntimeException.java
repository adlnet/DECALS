package com.eduworks.resolver.exception;

public class EditableRuntimeException extends RuntimeException
{
	StringBuilder b = new StringBuilder();
	public EditableRuntimeException(String ex)
	{
		b.append(ex);
	}
	public void append(String ex)
	{
		b.append(ex);
	}
	@Override
	public String getMessage()
	{
		return b.toString();
	}
}
