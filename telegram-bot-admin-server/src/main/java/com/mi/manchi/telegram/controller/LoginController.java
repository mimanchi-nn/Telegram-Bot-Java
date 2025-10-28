package com.mi.manchi.telegram.controller;

import com.mi.manchi.telegram.model.result.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

	@PostMapping("/login")
	public R<?> login() {
		// 3. 登录成功，返回 token
		Map<String, Object> response = new HashMap<>();
		response.put("token", "admin-token");
		return R.ok(response);
	}

}
