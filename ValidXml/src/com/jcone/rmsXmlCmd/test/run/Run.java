package com.jcone.rmsXmlCmd.test.run;

import java.io.File;
import java.util.ArrayList;

public class Run
{
	static ArrayList<File> arrSendList = new ArrayList<File>();

	public static void main1(String[] args)
	{
		System.out.println("Start");

		if (args.length == 0)
		{
			System.err.println("스키마 검증할 파일명이 없습니다.");
			System.exit(1);
		}

		if (args.length > 1)
		{
			System.err.println("하나의 파일만 입력해주세요.");
			System.exit(1);
		}

		String sourceFilePath = args[0];
		ExcuteXmlCmd proc = new ExcuteXmlCmd();
		proc.set_sourceFilePath(sourceFilePath);

		/*
		String _sourceFilePath = "C:\\Development\\workspace_for_uac\\RMSXmlCmd\\REQ_XML\\";
		String _destFilePath = "C:\\Development\\workspace_for_uac\\RMSXmlCmd\\RES_XML\\";
		ArrayList<File> fileList = getFileList(_sourceFilePath);

		for (File f : fileList)
		{
			proc.set_sourceFilePath(_sourceFilePath + f.getName());
			proc.set_destFilePath(_destFilePath + f.getName());
			//proc.set_destFilePath(null);

			proc.excute(false);
		}
		 */
	}

	public static void main(String[] args)
	{
		String _sourceFilePath = "C:\\Development\\workspace_for_uac\\RMSXmlCmd\\REQ_XML\\";
		String _destFilePath = "C:\\Development\\workspace_for_uac\\RMSXmlCmd\\RES_XML\\";
		ArrayList<File> fileList = getFileList(_sourceFilePath);

		ExcuteXmlCmd proc = new ExcuteXmlCmd();
		for (File f : fileList)
		{
			proc.set_sourceFilePath(_sourceFilePath + f.getName());
			proc.set_destFilePath(_destFilePath + f.getName());
			//proc.set_destFilePath(null);

			proc.excute(false);
		}
	}

	private static ArrayList<File> getFileList(String _strDir)
	{
		String strFileName = "";
		int i = 0;
		File dirF = new File(_strDir);
		String arrDirList[] = dirF.list();
		File f = null;

		if(arrDirList != null)
		{
			int len = arrDirList.length;

			for(i = 0; i < len; i++)
			{
				strFileName = _strDir + "\\" + arrDirList[i];
				f = new File(strFileName);
				if(f.isDirectory())
				{
					try
					{
						getFileList(f.getAbsolutePath());
					}
					catch(Exception e)
					{
						e.printStackTrace();
						break;
					}
				}
				else
				{
					if(f.length() != 0L)
					{
						arrSendList.add(f);
					}
				}
			}
		}
		else
		{
			//arrSendList.add(_strDir);
		}

		return arrSendList;
	}
}