package top.sogrey.csv2json.run;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import top.sogrey.csv2json.tools.Json2CSV;

@Component
@Order(value = 1)
public class MyStartupRunner1 implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

		if (args.length >= 1) {
			String cmd = args[0].toString().toLowerCase();
			switch (cmd) {
			case "test":
				System.out.println("测试命令。");
				break;
			case "json2csv":
				System.out.println("======[Json 转 CSV]======");
				json2csv(args);
				break;

			default:
				break;
			}
		} else {
			System.out.println("空命令。");
		}

	}

	/**
	 * json 转 csv
	 * @param args
	 */
	private void json2csv(String[] args) {
		String inputFile = args[1].toString();
		String outputFileName = "";
		if(args.length>3) {
			outputFileName = args[2].toString();
		}
		
		Json2CSV.json2csv(inputFile,outputFileName);
		
	}

}
