package top.sogrey.csv2json.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.CDL;
import org.json.JSONArray;
import org.springframework.util.StringUtils;

//import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONReader;

public class Json2CSV {

	public static File json2csv(String jsonFilePath, String outputFileName) {
		BufferedWriter bufferedWriter = null;
		FileReader fileReader;
		try {

			System.out.println("正在转换：" + jsonFilePath);

			File jsonFile = new File(jsonFilePath);

			if (StringUtils.isEmpty(outputFileName)) {
				String jsonFileName = jsonFile.getName();
				outputFileName = jsonFileName.substring(0, jsonFileName.lastIndexOf("."));
			}

			File target = new File(jsonFile.getParent(), outputFileName + ".csv");
			String targetFilePath = target.getAbsolutePath();
			System.out.println("目标文件：" + targetFilePath);

			fileReader = new FileReader(jsonFile);
			int length = 0;
			char[] byteInData = new char[50];
			StringBuffer buffer = new StringBuffer();
			while ((length = fileReader.read(byteInData)) != -1) {

				buffer.append(new String(byteInData, 0, length));

			}

			fileReader.close();

			String content = buffer.toString();

			org.json.JSONArray jsonArray = new JSONArray(content);
			String csv = CDL.toString(jsonArray);

			bufferedWriter = new BufferedWriter(new FileWriter(target));
			bufferedWriter.write(csv);
			bufferedWriter.close();
			
			System.out.println("转换完成。");

			return target;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
