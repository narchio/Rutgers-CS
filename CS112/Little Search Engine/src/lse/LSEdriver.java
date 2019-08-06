package lse;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class LSEdriver {

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		LittleSearchEngine lse = new LittleSearchEngine();

		String file = "docs.txt";

		try {
			lse.makeIndex(file, "noisewords.txt");
		} catch (FileNotFoundException e)
		{
		}

		Scanner stdin = new Scanner(System.in);

		System.out.print("enter keyWord1: ");
		String kw1 = stdin.nextLine();
		System.out.print("enter keyWord2: ");
		String kw2 = stdin.nextLine();

		ArrayList<String> result = lse.top5search(kw1, kw2);

		if(result == null) {
			System.out.println("words not found in either doc");
		}

		if(result != null) {
			for (int index = 0; index < result.size(); index++) {
				System.out.println(index+1 + ". " + result.get(index));
			}
		}

	}

}