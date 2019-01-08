package com.jabejokumparan.newsandtopic.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jabejokumparan.newsandtopic.exceptionhandler.ResourceNotFoundException;
import com.jabejokumparan.newsandtopic.model.News;
import com.jabejokumparan.newsandtopic.repository.NewsRepository;

@RestController
@RequestMapping("/api")
public class NewsController {
	@Autowired
	NewsRepository newsRepository;

	@GetMapping("/news")
	public List<News> getAllNews() {
		return newsRepository.findAll();
	}

	@PostMapping(path = "/news", consumes = "application/json")
	public News createNews(@Valid @RequestBody News news) {
		return newsRepository.save(news);
	}

	@GetMapping("/news/{id}")
	public News getNewsById(@PathVariable(value = "id") Long newsId) {
		return newsRepository.findById(newsId).orElseThrow(() -> new ResourceNotFoundException("News", "id", newsId));
	}

	@PutMapping("/news/{id}")
	public News updateNews(@PathVariable(value = "id") Long newsId, @RequestBody News newsDetail) {
		News news = newsRepository.findById(newsId)
				.orElseThrow(() -> new ResourceNotFoundException("News", "id", newsId));
		news.setName(newsDetail.getName());
		news.setStatus(newsDetail.getStatus());

		News updatedNews = newsRepository.save(news);
		return updatedNews;
	}

	@DeleteMapping("/news/{id}")
	public ResponseEntity<?> deleteNews(@PathVariable(value = "id") Long newsId) {
		News news = newsRepository.findById(newsId)
				.orElseThrow(() -> new ResourceNotFoundException("News", "id", newsId));
		newsRepository.delete(news);
		return ResponseEntity.ok().build();
	}
}
