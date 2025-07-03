package com.example.demo.services;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.example.demo.models.BookIndex;
import com.example.demo.repositories.BookRepository;
import com.example.demo.requests.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public BookIndex get(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    public BookIndex save(BookIndex document) {
        return bookRepository.save(document);
    }

    public BookIndex update (String id, BookRequest request) {
        BookIndex book = get(id);

        if (null != request.getName() && !request.getName().isBlank()) {
            book.setName(request.getName());
        }
        if (null != request.getAuthor() && !request.getAuthor().isBlank()) {
            book.setAuthor(request.getAuthor());
        }

        return bookRepository.save(book);
    }

    public void delete(String id) {
        bookRepository.deleteById(id);
    }

    public <T> List<T> search(Query query, Class<T> documentClass) {
        SearchHits<T> searchHits = elasticsearchOperations.search(query, documentClass);

        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public <T> List<T> getAll(Class<T> documentClass) {
        List<T> list = new ArrayList<>();
        try
        {
            BoolQuery.Builder query = QueryBuilders.bool();
            Query request = NativeQuery.builder()
                    .withQuery(query.build()._toQuery()).build().setPageable(PageRequest.ofSize(1000));
            SearchHits<T> searchHits = elasticsearchOperations.search(request, documentClass);

            list = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
