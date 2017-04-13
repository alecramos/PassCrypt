/*Checklist
 * -Figure out a way to incorporate capital and lower-case letters (maybe by extracting a number from the encrypted password and adding a number that converts it to a letter by using the HTML char codes.)
 * -Check if hexadecimal password exceeds Long.MAX_VALUE
 * -Expand short passwords less than 8 characters.
 * -Have user select lengths if possible
 * -incorporate two other encryption methods 
 * -Maybe incorporate symbols
 * 
 * -Once program is finalized, create an HTML equivalent.
 */
import java.nio.charset.Charset;
import java.util.Scanner;
import org.jasypt.util.text.BasicTextEncryptor; //Uses Jasypt Library (www.jasypt.org)
import org.jasypt.util.binary.BasicBinaryEncryptor;

public class PassCrypt
{
	
	public static void main(String[] args)
	{
//------Initialization---------------------------------------------------------------------------------------------------
		boolean isTruncated = false;
		Scanner input  = new Scanner(System.in);
		System.out.println("PassCrypt 0.1 Alpha by Alec Ramos");
		System.out.println("Convert a word into a complex password!");

//------Program start; asks for user keyword-----------------------------------------------------------------------------
		System.out.println("Enter a word or string of characters (preferably 4-8 characters).");
		String ex = input.next();
                System.out.println("Choose Encryptoin Type (1 for Manual, 2 for Jasypt , 3 for Binary)");
                String userInput = input.next();
                String ascii = "";
                switch(userInput)
                {
                    case "1":
                        //------Begin encryption process (refer to the encryption methods below)-------------------------------------------------
                        ascii = hexEncrypt(ex);
                        break;
                    case "2":
                        ascii = jasyptEncrypt(ex);
                        ascii = removeSymbols(ascii);
                        break;
                    case "3":
                        ascii = binEncrypt(ex);
                        
                        
                }
                System.out.println(ascii);
		
	}
	
////////////////////////////////////////////////////
////////////METHODS/////////////////////////////////
////////////////////////////////////////////////////
	

////Encryption option one: Simple////
	public static String hexEncrypt(String text)
	{
//------Initialization of encryption process----------------------------------------------------------
		String converted = "";
		String newPass = "";
		
//------Converts text to hexadecimal----------------------------------------------------------------------------
		for(int i = 0; i < text.length(); i++)
		{
			char tempChar = text.charAt(i);
			int tempInt = tempChar;
			String tempHex = Integer.toHexString(tempInt);
			converted += tempHex;
		}
		
//------Creates an array of the converted keyword; swaps each hexadecimal character----------------------------------------------------------------
		String textArray[] = converted.split("");
		if((textArray.length-1) % 2 == 0)
		{
			for(int i = 1; i < textArray.length; i+=2)
			{
				String temp = "";
				temp = textArray[i];
				textArray[i] = textArray[i-1];
				textArray[i-1] = temp;
			}	
		}
                else
                    System.out.println("Didn't pass");
	
//------------------------------------------------------------------------------------		
		
//------Combines each element in the array into a string of characters, making the encrypted password.-------------------------------------------------
		for(int i = 0; i < textArray.length; i++)
		{
			newPass += textArray[i];
		}
		return newPass;
	}
	
	//Consider removal of method
	public static String[] extractLettersFrom(String password)
	{
		String[] letters = {"", ""};
		char[] brokenPass = password.toCharArray();
		String numLetter = "";
		int cannotBeNum = 0;
		System.out.println(brokenPass[0]);
		for(int i = 0; i < password.length(); i++)
		{
			char tempStr = brokenPass[i];
			int tempNum = Character.getNumericValue(tempStr);
			if(numLetter.length() == 0)
			{
				if(tempNum > 5 && tempNum <= 9)
				{
					numLetter += tempNum;
					if(numLetter.equals("9"))
					{
						numLetter += 0;
						break;
					}
				}
			}
			else
			{
				if(cannotBeNum != 0)
				{
					if(tempNum == 0 || tempNum > 4)
					{
						numLetter += tempNum;
						cannotBeNum = tempNum;
						break;
					}
				}
				else
				{
					
				}
				
			}
		}
		return letters;
	}
        
        public static boolean checkForLetters(String ascii)
        {
            boolean hasLetters = false;
		try
		{
			long number = Long.parseLong(ascii);
		}
		catch(Exception error404)
		{
			System.out.println("Encrypted password contains letters.");
			hasLetters = true;
		}
		System.out.println(ascii);
                return hasLetters;
        }

    public static String jasyptEncrypt(String ex)
    {
        BasicTextEncryptor te = new BasicTextEncryptor();
        te.setPassword(ex);
        return te.encrypt(ex);
    }
    
    public static String removeSymbols(String text)
    {
        char[] brokenWord = {};
        String noSymbs = "";
        brokenWord=text.toCharArray();
        for(char element: brokenWord)
        {
            if(!isSymbol(element))
            {
                noSymbs+=element;
            }
        }
        return noSymbs;
    }
    
    //TO NOT BE CALLED IN MAIN EVER
    private static boolean isSymbol(char charThing)
    {
            return ((int)charThing >= 33 && (int)charThing <= 47) ||
                    ((int)charThing >= 58 && (int)charThing <= 64) ||
                    ((int)charThing >= 91 && (int)charThing <= 96) ||
                    ((int)charThing >= 123 && (int)charThing <= 127);
    }
    
    public static String binEncrypt(String text)
    {
       // Charset UTF_8 = Charset.forName("UTF-8");
        String binText = "";
        BasicTextEncryptor be = new BasicTextEncryptor();
        be.setPassword(text);
        String binThing = "";
        for(char c: text.toCharArray())
        {
            binThing+= Integer.toBinaryString(c);
        }
       // System.out.println(binThing);
        for(char c: binThing.toCharArray())
        {
            if(c=='1')
                binText+="one";
            else
                binText+="zero";        
        }
        return hexEncrypt(be.encrypt(binText));         
    }
    
    public static String comboCrypt(String text)
    {
       return text; 
    }
    
    //------Checks encrypted password for letters by trying to parse string as a number. If exception occurs, then the password contains a letter---------------------------------
		
//------If exception doesn't occur, then a letter will be extracted by finding a decimal number equivalent to a letter----------------------------------------
//		(ex. If encrypted password is 8541; program can find 85 and convert it to a letter [using ASCII code] to use in the password)
		
		/* tempStr = each number in char format in the array
		 * brokenAscii = the encrypted password broken up as an array of char
		 * numLetter = number extracted from the encrypted password to use as a letter
		 */
		/*if(!hasLetters)
		{
			char[] brokenAscii = ascii.toCharArray();
			String numLetter = "";
			int cannotBeNum = 0;
			System.out.println(brokenAscii[0]);
			for(int i = 0; i < ascii.length(); i++)
			{
				char tempStr = brokenAscii[i];
				int tempNum = Character.getNumericValue(tempStr);
				if(numLetter.length() == 0)
				{
					if(tempNum > 5 && tempNum <= 9)
					{
						numLetter += tempNum;
						if(numLetter.equals("9"))
						{
							numLetter += 0;
							break;
						}
					}
				}
				else
				{
					if(cannotBeNum != 0)
					{
						if(tempNum == 0 || tempNum > 4)
						{
							numLetter += tempNum;
							cannotBeNum = tempNum;
							break;
						}
					}
					else
					{
						
					}
					
				}
			}
			
//----------Determines placement of the letter. If second number is 5 or greater, placement is at the beginning of the password. Otherwise, at the end----------------------------
			int letterNum = Integer.parseInt(numLetter);
			int templetter = Character.getNumericValue(brokenAscii[1]);
			if(templetter >= 5)
			{
				ascii = (char)letterNum + ascii;
			}
			else
			{
				ascii = ascii + (char)letterNum;
			}
		}*/
		
		
//------If the encrypted password is longer than 8 characters, ask user to shorten to 8.---------------------------------
		/*if(ascii.length() > 8 && ascii.length() < 12)
		{
			System.out.println("The encrypted password is larger than 8 characters.");
			System.out.println("Would you like to shorten the password to 8 characters? (1=Y. 2=N)");
			int answer = input.nextInt();
			if(answer == 1)
			{
				ascii = ascii.substring(0, 8);
				isTruncated = true;
			}
		}*/
                
                
                
//------Unfinished length modifier code; do not execute until code is complete------------------------------------------------------------------
		/*else if(ascii.length() > 12 && ascii.length() <= 16)
		{
			System.out.println("The encrypted password is larger than 12 characters.");
			System.out.println("Would you like to shorten the password to 12 characters? (1=Y. 2=N)");
			int answer = input.nextInt();
			if(answer == 1)
			{
				ascii = ascii.substring(0, 8);
				isTruncated = true;
			}
		}
		else
		{
			System.out.println("The encrypted password is larger than 8 characters.");
			System.out.println("Would you like to shorten the password to 8 characters? (1=Y. 2=N)");
			int answer = input.nextInt();
			if(answer == 1)
			{
				ascii = ascii.substring(0, 8);
				isTruncated = true;
			}
		}*/
		
		//Displays encrypted password.
		/*System.out.println("Here is your encrypted password!: " + ascii);
		if(isTruncated)
		{
			System.out.println("Remember, your password has been shortened.");
		}
		System.out.println("Remember your inputed keyword for this application!: " + ex);*/
}
