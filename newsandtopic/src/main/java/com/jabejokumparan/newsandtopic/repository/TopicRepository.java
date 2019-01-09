package com.jabejokumparan.newsandtopic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jabejokumparan.newsandtopic.model.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
	Topic findByName(String name);
}
