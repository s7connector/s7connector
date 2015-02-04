package ch.ri.s7.test.test.base;

public class Timer
{
	public Timer()
	{
		reset();
	}
	
	private long lastResetTime;
	
	public void reset()
	{
		lastResetTime = System.currentTimeMillis();
	}
	
	public long getMillis()
	{
		return System.currentTimeMillis() - lastResetTime;
	}
	
	public long getMillisAndReset()
	{
		reset();
		return getMillis();
	}
}
