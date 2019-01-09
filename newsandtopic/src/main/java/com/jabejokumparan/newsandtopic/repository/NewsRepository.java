package com.jabejokumparan.newsandtopic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jabejokumparan.newsandtopic.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
	List<News> findByStatus(String status);
}
