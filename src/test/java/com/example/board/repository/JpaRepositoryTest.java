package com.example.board.repository;

import com.example.board.config.JpaConfig;
import com.example.board.domain.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@DisplayName("JPA 연결 테스트")
class JpaRepositoryTest {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;

    public JpaRepositoryTest(
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired ArticleRepository articleRepository) {
        this.articleCommentRepository = articleCommentRepository;
        this.articleRepository = articleRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorkFine() {

        List<Article> articles = articleRepository.findAll();
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorkFine() {
        long prevCount = articleRepository.count();

        Article savedArticle = articleRepository.save(Article.of("new title", "new content", "#spring"));

        List<Article> articles = articleRepository.findAll();
        assertThat(articleRepository.count())
                .isEqualTo(prevCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashtag = "#springboot";
        article.setHashtag(updateHashtag);

        Article savedArticle = articleRepository.saveAndFlush(article);

        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updateHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deleteCommentSize = article.getArticleComments().size();

        articleRepository.delete(article);


        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deleteCommentSize);
    }
}
