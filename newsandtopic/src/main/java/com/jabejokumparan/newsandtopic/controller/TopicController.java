package com.jabejokumparan.newsandtopic.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.jabejokumparan.newsandtopic.exceptionhandler.ResourceNotFoundException;
import com.jabejokumparan.newsandtopic.model.Topic;
import com.jabejokumparan.newsandtopic.model.View;
import com.jabejokumparan.newsandtopic.repository.TopicRepository;

@RestController
@RequestMapping("/api")
public class TopicController {
	@Autowired
	TopicRepository topicRepository;

	@JsonView(View.TopicDetail.class)
	@GetMapping("/topic")
	public @ResponseBody ResponseEntity<List<Topic>> getAllTopic() {
		List<Topic> allTopic = topicRepository.findAll();
		return new ResponseEntity<List<Topic>>(allTopic, HttpStatus.OK);
	}

	@JsonView(View.Topic.class)
	@GetMapping("/topic/{id}")
	public @ResponseBody ResponseEntity<Topic> getTopicById(@PathVariable(value = "id") Long topicId) {
		return new ResponseEntity<Topic>(topicRepository.findById(topicId)
				.orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId)), HttpStatus.OK);
	}


	
	@JsonView(View.Topic.class)
	@PostMapping(path = "/topic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Topic> createTopic(@Valid @RequestBody Topic topic) {
		Topic topicExist = topicRepository.findByName(topic.getName());
		if (topicExist != null) {
			return new ResponseEntity<Topic>(topicExist, HttpStatus.OK);
		}
		return new ResponseEntity<Topic>(topicRepository.save(topic), HttpStatus.OK);
	}

	@JsonView(View.Topic.class)
	@PutMapping(path = "/topic/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Topic> updateTopic(@PathVariable(value = "id") Long topicId,
			@Valid @RequestBody Topic topicDetail) {
		Topic topic = topicRepository.findById(topicId)
				.orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));
		topic.setName(topicDetail.getName());
		Topic updatedTopic = topicRepository.save(topic);
		return new ResponseEntity<Topic>(updatedTopic, HttpStatus.OK);
	}

	@DeleteMapping("/topic/{id}")
	public ResponseEntity<String> deleteTopic(@PathVariable(value = "id") Long topicId) {
		Topic topic = topicRepository.findById(topicId)
				.orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));
		topicRepository.delete(topic);
		return new ResponseEntity<String>("Success deleted id " + topicId, HttpStatus.OK);
	}
}
