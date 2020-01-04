package com.practice.awsDynamoDB.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practice.awsDynamoDB.model.SongsPojo;
import com.practice.awsDynamoDB.service.DynamoService;

@Controller
public class DynamoController {

	@Autowired
	DynamoService service;
	
	@RequestMapping(value="/addMusic", method = {RequestMethod.POST},consumes = "application/json")
	@ResponseBody
	public String addMusic(@RequestBody SongsPojo song) throws Exception {
		System.out.println("Controller called");
		String result = null;
		result = service.addMusic(song);
		return result;
	}
	
	@RequestMapping(value="/getMusicById", method = {RequestMethod.GET},produces = "application/json")
	@ResponseBody
	public List<SongsPojo> getMusicById(@RequestParam String songId) throws Exception {
		System.out.println("Controller called");
		return service.getMusicById(songId);
	}
	
	@RequestMapping(value="/getMusicByName", method = {RequestMethod.GET},produces = "application/json")
	@ResponseBody
	public List<SongsPojo> getMusicByName(@RequestParam String songName) throws Exception {
		System.out.println("Controller called");
		return service.getMusicByName(songName);
	}
	
	@RequestMapping(value="/updateMusicById", method = {RequestMethod.POST},consumes = "application/json")
	@ResponseBody
	public void updateMusicById(@RequestBody SongsPojo song) throws Exception {
		System.out.println("Controller called");
		service.updateMusicById(song);
	}
}
