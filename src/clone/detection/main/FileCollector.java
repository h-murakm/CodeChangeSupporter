package clone.detection.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class FileCollector {

	static class FileSort implements Comparator{

		public int compare(File src, File target){
			int diff = src.getName().compareTo(target.getName());
			return diff;
		}

		public int compare(Object obj, Object obj1){
			return compare((File) obj, (File) obj1);
		}
	}

	public static void listPath(File file){
		File fs[] = file.listFiles();
		Arrays.sort(fs, new FileSort());
		File afile[];
		int j = (afile = fs).length;
		for (int i = 0; i < j; i++){
			File f = afile[i];
			if (f.isDirectory())
				listPath(f);
			else if (f.isFile())
				try{
					if (f.isFile()){
						String path = f.getCanonicalPath();
						if (path.endsWith(".java")){
							insertFile(path);
						}
					}
				} catch (IOException e){
					e.printStackTrace();
				}
		}

	}

	private static void insertFile(String path) {
		CloneDetectionMain.fileNameList.add(path);
	}
}
