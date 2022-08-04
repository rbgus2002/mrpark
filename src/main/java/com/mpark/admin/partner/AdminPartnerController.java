package com.mpark.admin.partner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpark.common.util.RestTemplateUtil;

@Controller
@SessionAttributes("token")
public class AdminPartnerController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = { "/", "/admin/index", ""})
	public ModelAndView index(HttpServletRequest request, @ModelAttribute("token") String token, ModelAndView mv) throws JsonMappingException, JsonProcessingException {

		ResponseEntity<String> responseEntity = RestTemplateUtil.sendPostRequest("GetPendingPartners", token);		// 승인 대기중인 파트너사들 URL 
		int resultCode = responseEntity.getStatusCodeValue();
		mv.addObject("resultCode", resultCode);
		
		if (resultCode == 200) {
			ObjectMapper mapper = new ObjectMapper();
			String result = responseEntity.getBody();
			List<?> list = mapper.readValue(result, List.class);
			mv.addObject("list", list);
		} else {
			mv.setViewName("redirect:/admin/logout");
		}
		return mv;
	}
	
	@RequestMapping(value = {"/admin/partnerSingeList"})
	public ModelAndView partnerSingeList(HttpServletRequest request, @ModelAttribute("token") String token, ModelAndView mv) throws JsonMappingException, JsonProcessingException {

		ResponseEntity<String> responseEntity = RestTemplateUtil.sendPostRequest("GetPendingPartners", token);		// 승인 대기중인 파트너사들 URL 
		int resultCode = responseEntity.getStatusCodeValue();
		mv.addObject("resultCode", resultCode);
		
		if (resultCode == 200) {
			ObjectMapper mapper = new ObjectMapper();
			String result = responseEntity.getBody();
			List<?> list = mapper.readValue(result, List.class);
			mv.addObject("list", list);
		}

		mv.setViewName("/admin/partner/partnerSingeList");
		return mv;
	}

/*
	@RequestMapping(value = { "/admin/getPartners" })
	public String partnersList() {
		return "/admin/partner/partnerList";
	}
*/

	
	@RequestMapping(value = { "/admin/partnerList" })
	public ModelAndView getPartners(HttpServletRequest request, @ModelAttribute("token") String token, ModelAndView mv) throws JsonMappingException, JsonProcessingException { 

		MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
		param.add("Mode", "DEFAULT"); 
		param.add("Keyword", "");

		ResponseEntity<String> responseEntity = RestTemplateUtil.sendPostRequest("getPartners", token, param); 
		int resultCode = responseEntity.getStatusCodeValue();
		mv.addObject("resultCode", resultCode);

		if(resultCode == 200) { 
			ObjectMapper mapper = new ObjectMapper();
			String result = responseEntity.getBody();
			List<?> list = mapper.readValue(result, List.class);
			mv.addObject("list", list); 
		}

		mv.setViewName("/admin/partner/partnerList"); 
		return mv; 
	}
}


