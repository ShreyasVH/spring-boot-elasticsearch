package com.example.demo.repositories;

import com.example.demo.models.BookIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepository extends ElasticsearchRepository<BookIndex, String> {
}
