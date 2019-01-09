package com.jabejokumparan.newsandtopic.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.jabejokumparan.newsandtopic.model.News;
import com.jabejokumparan.newsandtopic.model.Topic;
import com.jabejokumparan.newsandtopic.model.View;
import com.jabejokumparan.newsandtopic.repository.NewsRepository;
import com.jabejokumparan.newsandtopic.repository.TopicRepository;

@RestController
@RequestMapping("/api")
public class NewsController {
	@Autowired
	NewsRepository newsRepository;
	@Autowired
	TopicRepository topicRepository;

	@JsonView(View.NewsDetail.class)
	@GetMapping("/news")
	public @ResponseBody ResponseEntity<List<News>> getAllNews() {
		List<News> newss = newsRepository.findAll();
		return new ResponseEntity<List<News>>(newss, HttpStatus.OK);
	}

	@JsonView(View.News.class)
	@PostMapping(path = "/news", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<News> createNews(@Valid @RequestBody News news) {
		Set<Topic> topicInserted = new HashSet<>();
		News newsInserted = new News();
		newsInserted.setName(news.getName());
		newsInserted.setStatus(news.getStatus());
		for (Topic t : news.getTopics()) {
			Topic topicExist = topicRepository.findByName(t.getName());
			if (topicExist != null) {
				topicInserted.add(topicExist);
			} else {
				topicInserted.add(t);
			}
		}
		newsInserted.setTopics(topicInserted);
		return new ResponseEntity<News>(newsRepository.save(newsInserted), HttpStatus.OK);
	}

	@JsonView(View.News.class)
	@GetMapping("/news/{id}")
	public @ResponseBody ResponseEntity<News> getNewsById(@PathVariable(value = "id") Long newsId) {
		return new ResponseEntity<News>(
				newsRepository.findById(newsId).orElseThrow(() -> new ResourceNotFoundException("News", "id", newsId)),
				HttpStatus.OK);
	}

	@JsonView(View.News.class)
	@GetMapping("/news/filterbystatus")
	public @ResponseBody ResponseEntity<List<News>> getNewsByStatus(@RequestParam(value = "status") String status) {
		return new ResponseEntity<List<News>>(newsRepository.findByStatus(status), HttpStatus.OK);
	}

	@JsonView(View.TopicDetail.class)
	@GetMapping("/news/filterbytopic")
	public @ResponseBody ResponseEntity<Topic> getNewsByTopic(@RequestParam(value = "topics") String topicName) {
		return new ResponseEntity<Topic>(topicRepository.findByName(topicName), HttpStatus.OK);
	}

	@JsonView(View.News.class)
	@PutMapping(path = "/news/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<News> updateNews(@PathVariable(value = "id") Long newsId,
			@Valid @RequestBody News newsDetail) {
		News news = newsRepository.findById(newsId)
				.orElseThrow(() -> new ResourceNotFoundException("News", "id", newsId));
		news.setName(newsDetail.getName());
		news.setStatus(newsDetail.getStatus());
		if (!newsDetail.getTopics().isEmpty()) {
			Set<Topic> topicInserted = new HashSet<>();
			for (Topic t : newsDetail.getTopics()) {
				Topic topicExist = topicRepository.findByName(t.getName());
				if (topicExist != null) {
					topicInserted.add(topicExist);
				} else {
					topicInserted.add(t);
				}
			}
			news.setTopics(topicInserted);
		}

		News updatedNews = newsRepository.save(news);
		return new ResponseEntity<News>(updatedNews, HttpStatus.OK);
	}

	@DeleteMapping("/news/{id}")
	public ResponseEntity<String> deleteNews(@PathVariable(value = "id") Long newsId) {
		News news = newsRepository.findById(newsId)
				.orElseThrow(() -> new ResourceNotFoundException("News", "id", newsId));
		newsRepository.delete(news);
		return new ResponseEntity<String>("Success deleted id " + newsId, HttpStatus.OK);
	}
}
