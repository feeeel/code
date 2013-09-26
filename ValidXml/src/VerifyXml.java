import java.io.File;

import com.jcone.rmsXmlCmd.test.run.ExcuteXmlCmd;

public class VerifyXml
{
	public static void main(String[] args)
	{
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
		File f = new File(sourceFilePath);

		ExcuteXmlCmd proc = new ExcuteXmlCmd();
		proc.set_sourceFilePath(f.getAbsolutePath());
		proc.excute(false);
	}
}