package com.example.demo.controllers;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import com.example.demo.models.BookIndex;
import com.example.demo.requests.BookRequest;
import com.example.demo.services.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class IndexController
{
    @Autowired
    private ElasticService elasticService;

    @GetMapping("/books/{id}")
    public BookIndex get(@PathVariable(value = "id") String id)
    {
        return elasticService.get(id);
    }

    @PostMapping("/books")
    public BookIndex post(@RequestBody BookRequest request)
    {
        String id = UUID.randomUUID().toString();
        return elasticService.save(new BookIndex(id, request.getName(), request.getAuthor()));
    }

    @PutMapping("/books/{id}")
    public BookIndex put(@PathVariable String id, @RequestBody BookRequest request)
    {
        return elasticService.update(id, request);
    }

    @DeleteMapping("/books/{id}")
    public String delete(@PathVariable(value = "id") String id)
    {
        elasticService.delete(id);
        return "DELETE REQUEST";
    }

    @GetMapping("/books/author")
    public List<BookIndex> getByAuthor(@RequestParam String author) {
        BoolQuery.Builder query = QueryBuilders.bool();

        query.must(b -> b.terms(TermsQuery.of(t -> t
        .field("author")
        .terms(TermsQueryField.of(m -> m
        .value(Collections.singletonList(FieldValue.of(author))))))));

        Query request = NativeQuery.builder()
                .withQuery(query.build()._toQuery()).build();

        return elasticService.search(request, BookIndex.class);
    }
}
