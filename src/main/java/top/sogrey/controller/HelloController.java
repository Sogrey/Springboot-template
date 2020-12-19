package top.sogrey.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.sogrey.interfaces.AccessLimit;
import top.sogrey.models.ResponseResult;

@Controller // 标明这是一个SpringMVC的Controller控制器
@RequestMapping(value = "/api")
public class HelloController {
	@Autowired
	private Environment env;

	@RequestMapping("/")
	@ResponseBody
	public String hello() {
		return "hello world";
	}

	@RequestMapping("/hello")
	@ResponseBody
	public String sayHello(String name) {
		System.out.println();
		return "hello " + name;
	}

	@RequestMapping("/runSHW")
	@ResponseBody
	public String runSH(int cmdType) {// String shellPath, String name
//		StringBuffer sb = new StringBuffer();
//		try {
//			String shpath = "./run.sh";
//			Process ps = Runtime.getRuntime().exec(shpath);
//			ps.waitFor();
//
//			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
//
//			String line;
//			while ((line = br.readLine()) != null) {
//				sb.append(line).append("\n");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sb.toString();

		String systemType = env.getProperty("systemType");

		String batPath = "D:/project/demos/runw.bat"; // 把你的bat脚本路径写在这里
		if ("linux".equals(systemType)) {
			batPath = "run.sh";
		}
		File batFile = new File(batPath);
		boolean batFileExist = batFile.exists();
		System.out.println("batFileExist:" + batFileExist);
		String result = "---";
		if (batFileExist) {
			result = callCmd(batPath, systemType, cmdType);
		}
		System.out.println("result:" + result);
		return result;
	}

	private static String callCmd(String locationCmd, String systemType, int cmdType) {
		StringBuilder sb = new StringBuilder();
		String cmdPre = "";
		try {
			System.out.println(systemType);
			if ("linux".equals(systemType)) {
//				Runtime.getRuntime().exec("chmod 755 -R " + locationCmd);
//				cmdPre = "/bin/sh ";
				cmdPre = "bash ";
			} else {
				switch (cmdType) {
				case 1:
					cmdPre = "cmd /c ";// 执行完dir命令后关闭命令窗口
					break;
				case 2:
					cmdPre = "cmd /k ";// 执行完dir命令后不关闭命令窗口
					break;
				case 3:
					cmdPre = "cmd /c start ";// 会打开一个新窗口后执行dir指令, 原窗口会关闭
					break;
				case 4:
					cmdPre = "cmd /k start ";// 会打开一个新窗口后执行dir指令, 原窗口不会关闭
					break;

				default:
					break;
				}
			}
			Process child = Runtime.getRuntime().exec(cmdPre + locationCmd);
			InputStream in = child.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line + " & ");
			}
			in.close();
			try {
				child.waitFor();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			System.out.println("sb:" + sb.toString());
			System.out.println("callCmd execute finished");
			closeStream(bufferedReader);
		} catch (IOException e) {
			System.out.println(e);
			sb.append(e.getMessage());
		}
		return sb.toString();
	}

//	@RequestMapping("/runSH")
//	@ResponseBody
//	public String copyDatabase(HttpServletRequest request, String dbCode, String targetPath) {
//		JSONObject result = new JSONObject();
//
//		String osname = System.getProperty("os.name");
//		if ((osname != null) && (osname.toLowerCase().startsWith("win"))) {
//			LOG.info("当前操作系统是:" + osname);
//			result.put("code", "0");
//			result.put("msg", "当前服务器操作系统不是linux");
//			return result.toJSONString();
//		}
//		LOG.info("接收到参数:dbCode=" + dbCode + " targetDbNfsPath=" + targetPath);
//		if (StringUtil.isBlank(dbCode) || StringUtil.isBlank(targetPath)) {
//			result.put("code", "0");
//			result.put("msg", "dbCode/targetPath不能为空");
//			return result.toJSONString();
//		}
//
//		String dir = DbDirConstant.findDir(dbCode);
//		if (StringUtil.isBlank(dir)) {
//			result.put("code", "0");
//			result.put("msg", "根据dbCode找不到对应的数据库目录");
//			return result.toJSONString();
//		}
//		// 脚本路径
//		String shellPath = request.getServletContext().getRealPath("/") + "WEB-INF/classes";
//		String cmd = shellPath + "/copyDB.sh " + dir + " " + targetPath;
//		ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", cmd);
//		builder.directory(new File(shellPath));//切换到工作目录
//
//		int runningStatus = 0;
//		String s = null;
//		StringBuffer sb = new StringBuffer();
//		try {
//			Process p = builder.start();//启动脚本
//
//			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//			while ((s = stdInput.readLine()) != null) {
//				LOG.info("shell log info ...." + s);
//				sb.append(s);
//			}
//			while ((s = stdError.readLine()) != null) {
//				LOG.error("shell log error...." + s);
//				sb.append(s);
//			}
//			try {
//				runningStatus = p.waitFor();
//			} catch (InterruptedException e) {
//				runningStatus = 1;
//				LOG.error("等待shell脚本执行状态时，报错...", e);
//				sb.append(e.getMessage());
//			}
//
//			closeStream(stdInput);
//			closeStream(stdError);
//
//		} catch (Exception e) {
//			LOG.error("执行shell脚本出错...", e);
//			sb.append(e.getMessage());
//			runningStatus = 1;
//		}
//		LOG.info("runningStatus = " + runningStatus);
//		if (runningStatus == 0) {
//			// 成功
//			result.put("code", "1");
//			result.put("msg", "成功");
//			return result.toJSONString();
//		} else {
//			result.put("code", "0");
//			result.put("msg", "调用shell脚本复制数据库时失败..." + sb.toString());
//			return result.toJSONString();
//		}
//	}

	private static void closeStream(BufferedReader reader) {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			reader = null;
		}
	}

//    @AccessLimit(seconds=5, maxCount=5, needLogin=true)   //接口防刷
	@RequestMapping("/color")
	@ResponseBody
	public String setColor(String colorName) {
		Map<String, String> map = new HashMap<>();
		map.put("黑色", "30");
		map.put("black", "30");
		map.put("红色", "31");
		map.put("red", "31");
		map.put("绿色", "32");
		map.put("green", "32");
		map.put("黄色", "33");
		map.put("yellow", "33");
		map.put("蓝色", "34");
		map.put("blue", "34");
		map.put("紫红色", "35");
		map.put("pred", "35");
		map.put("青蓝色", "36");
		map.put("turquoise", "36");
		map.put("白色", "37");
		map.put("white", "37");
//    	Scanner scanner = new Scanner(System.in);
//    	System.out.println("请输入想要的颜色：黑色，红色，绿色，黄色，蓝色，紫红色，青蓝色，白色");
//    	String importText = scanner.next();
		String colorCode = null;
		for (String key : map.keySet()) {
			if (Objects.equals(key, colorName)) {
				colorCode = (String) map.get(colorName);
			}
		}
		System.out.println("你输入的颜色是\033[1;" + colorCode + "m" + colorName + "\033[0m \n");

		// #start

		// #end

		return "你输入的颜色： " + colorName + ", 颜色值：" + colorCode;
	}

}
