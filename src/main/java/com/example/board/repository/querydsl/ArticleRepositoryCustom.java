package com.example.board.repository.querydsl;

import com.example.board.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
public interface ArticleRepositoryCustom {

    List<String> findAllDistinctHashtags();
}
