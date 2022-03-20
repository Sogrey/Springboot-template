package top.sogrey.models;

import org.springframework.beans.factory.annotation.Autowired;

public class ResponseResult {
	@Autowired
	public int resultCode = 0;
	@Autowired
	public String resultMsg = "";
	@Autowired
	public Object data;
}
