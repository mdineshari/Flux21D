import org.fusesource.jansi.AnsiConsole;


public class Console
{
	
	public static final String	ANSI_RESET	= "\u001B[0m";
	public static final String	ANSI_BLACK	= "\u001B[30m";
	public static final String	ANSI_RED	= "\u001B[31m";
	public static final String	ANSI_GREEN	= "\u001B[32m";
	public static final String	ANSI_YELLOW	= "\u001B[33m";
	public static final String	ANSI_BLUE	= "\u001B[34m";
	public static final String	ANSI_PURPLE	= "\u001B[35m";
	public static final String	ANSI_CYAN	= "\u001B[36m";
	public static final String	ANSI_WHITE	= "\u001B[37m";
	
	public static void printThis(String Text, String ColorCode)
	{
		AnsiConsole.systemInstall();
		if (ColorCode.trim().equalsIgnoreCase("red"))
		{
			AnsiConsole.out.print(ANSI_RED + Text + ANSI_RESET);
		}
		else
			if (ColorCode.trim().equalsIgnoreCase("black"))
			{
				AnsiConsole.out.print(ANSI_BLACK + Text + ANSI_RESET);
			}
			else
				if (ColorCode.trim().equalsIgnoreCase("green"))
				{
					AnsiConsole.out.print(ANSI_GREEN + Text + ANSI_RESET);
				}
				else
					if (ColorCode.trim().equalsIgnoreCase("yellow"))
					{
						AnsiConsole.out.print(ANSI_YELLOW + Text + ANSI_RESET);
					}
					else
						if (ColorCode.trim().equalsIgnoreCase("blue"))
						{
							AnsiConsole.out.print(ANSI_BLUE + Text + ANSI_RESET);
						}
						else
							if (ColorCode.trim().equalsIgnoreCase("purple"))
							{
								AnsiConsole.out.print(ANSI_PURPLE + Text + ANSI_RESET);
							}
							else
								if (ColorCode.trim().equalsIgnoreCase("cyan"))
								{
									AnsiConsole.out.print(ANSI_CYAN + Text + ANSI_RESET);
								}
								else
									if (ColorCode.trim().equalsIgnoreCase("white"))
									{
										AnsiConsole.out.print(ANSI_WHITE + Text + ANSI_RESET);
									}
	}
	
	
	
	public static void printError(String Text)
	{
		AnsiConsole.systemInstall();
		AnsiConsole.out.println(ANSI_RED + Text + ANSI_RESET);
		System.exit(0);
	}
	
	
	
	public static void printWarning(String Text)
	{
		AnsiConsole.systemInstall();
		AnsiConsole.out.println(ANSI_PURPLE + Text + ANSI_RESET);
	}
	
	
	
	public static void printHelp(String Text)
	{
		AnsiConsole.systemInstall();
		AnsiConsole.out.println(ANSI_YELLOW + Text + ANSI_RESET);
	}
	
	
	
	public static void printPositiveLog(String Text)
	{
		AnsiConsole.systemInstall();
		AnsiConsole.out.println(ANSI_GREEN + Text + ANSI_RESET);
	}
}
