package com.jcone.rmsXmlCmd.test;

public abstract class Worker
{
	protected abstract void doit(String s);

	public final void work(String s)
	{
		System.out.println("출근");
		doit(s);
		System.out.println("퇴근");
	}
}