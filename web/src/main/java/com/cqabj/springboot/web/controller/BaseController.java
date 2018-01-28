package com.cqabj.springboot.web.controller;

import com.cqabj.springboot.model.common.CodeEnum;
import com.cqabj.springboot.model.common.ResultInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@ApiIgnore
@RestController
public class BaseController {

	@RequestMapping(value = "/authExp")
	public ResultInfo<String> authExp(){
		return ResultInfo.createResult(CodeEnum.ERROR_20001);
	}
}