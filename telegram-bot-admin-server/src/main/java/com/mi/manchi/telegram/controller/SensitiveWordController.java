package com.mi.manchi.telegram.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mi.manchi.telegram.model.entity.SensitiveWord;
import com.mi.manchi.telegram.model.result.R;
import com.mi.manchi.telegram.service.SensitiveWordService;
import com.mi.manchi.telegram.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensitive-word")
@RequiredArgsConstructor
public class SensitiveWordController {

	private final SensitiveWordService sensitiveWordService;

	@GetMapping("/selectPage")
	public R<?> selectPage(Page<SensitiveWord> page, SensitiveWord data) {
		IPage<SensitiveWord> sensitiveWordPage = sensitiveWordService.selectPage(page, data);
		return R.ok(sensitiveWordPage);
	}

	@PostMapping("/addSensitiveWord")
	public R<?> addSensitiveWord(@RequestBody SensitiveWord data) {
		data.setCreateTime(DateTimeUtils.get());
		sensitiveWordService.save(data);
		return R.ok();
	}

}
