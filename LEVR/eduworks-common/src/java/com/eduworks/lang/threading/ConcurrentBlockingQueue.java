package com.eduworks.lang.threading;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class ConcurrentBlockingQueue<T> extends ConcurrentLinkedQueue<T> implements BlockingQueue<T>
{

	@Override
	public void put(T e) throws InterruptedException
	{
		super.add(e);
	}

	@Override
	public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException
	{
		return super.add(e);
	}

	@Override
	public T take() throws InterruptedException
	{
		return super.poll();
	}

	@Override
	public T poll(long timeout, TimeUnit unit) throws InterruptedException
	{
    long nanos = unit.toNanos(timeout);
    long future = System.nanoTime()+nanos;
    while (System.nanoTime() < future)
    {
    	T t = null;
    	if ((t = super.poll()) == null) 
    		EwThreading.sleep(1);
    	if (t != null) return t;
    }
    return null;
	}

	@Override
	public int remainingCapacity()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int drainTo(Collection<? super T> c)
	{
		int count = 0;
		T t = null;
		while ((t = super.poll()) != null);
		{
			c.add(t);
			count++;
		}
		return count;
	}

	@Override
	public int drainTo(Collection<? super T> c, int maxElements)
	{
		int count = 0;
		T t = null;
		while ((t = super.poll()) != null && maxElements < count);
		{
			c.add(t);
			count++;
		}
		return count;
	}

}
