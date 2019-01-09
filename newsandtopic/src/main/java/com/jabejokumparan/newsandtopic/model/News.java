package com.jabejokumparan.newsandtopic.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "news")
public class News {
	@JsonView(View.News.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonView({ View.News.class, View.TopicDetail.class })
	@NotNull
	@Size(max = 250)
	private String name;

	@JsonView({ View.News.class, View.TopicDetail.class })
	@NotNull
	@Size(max = 100)
	private String status;

	// @JsonManagedReference
	@JsonView(View.NewsDetail.class)
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "news_topics", joinColumns = { @JoinColumn(name = "news_id") }, inverseJoinColumns = {
			@JoinColumn(name = "topic_id") })
	private Set<Topic> topics = new HashSet<>();

	public News() {

	}

	public News(@NotNull @Size(max = 250) String name, @NotNull @Size(max = 100) String status) {
		this.name = name;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

}
