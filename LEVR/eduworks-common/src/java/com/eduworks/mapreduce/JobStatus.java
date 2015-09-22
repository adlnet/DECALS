package com.eduworks.mapreduce;

import java.io.Serializable;

public class JobStatus implements Serializable
{
	public enum	STATE {COMPLETE,FAILED,INCOMPLETE};
	private int	I;
	private STATE state;
	private Object	o;
	private int mod;
	
	@Override
	public String toString()
	{
		return I+1 + "/" + mod + ": " + state.toString();
	}

	public void setState(STATE status)
	{
		this.state = status;
	}

	public int getMod()
	{
		return mod;
	}
	
	public void setI(int i)
	{
		this.I = i;
	}

	public int getI()
	{
		return I;
	}

	public boolean isComplete()
	{
		return state == STATE.COMPLETE;
	}
	public boolean isFailed()
	{
		return state == STATE.FAILED;
	}

	public void setObject(Object o)
	{
		this.o = o;
	}

	public Object getObject()
	{
		return o;
	}

	public void setMod(int mod)
	{
		this.mod = mod;
	}

	public static boolean isMine(long bigI, long mod, long index)
	{
		return index % mod == bigI;
	}
	
	public boolean isMine(int i2)
	{
		return i2 % mod == I;
	}

}
