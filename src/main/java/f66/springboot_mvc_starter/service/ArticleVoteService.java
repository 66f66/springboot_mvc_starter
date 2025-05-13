package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.ArticleVoteDTO;
import f66.springboot_mvc_starter.dto.VoteResult;
import f66.springboot_mvc_starter.repository.ArticleRepository;
import f66.springboot_mvc_starter.repository.ArticleVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleVoteService {

    private final ArticleRepository articleRepository;
    private final ArticleVoteRepository articleVoteRepository;

    @Transactional
    public VoteResult toggleVote(Long articleId,
                                 Long currentUserId) {

        articleVoteRepository.toggleVote(articleId, currentUserId);

        ArticleVoteDTO articleVoteDTO = articleVoteRepository
                .selectVoteByUserIdAndArticleId(articleId, currentUserId)
                .orElseThrow();

        ArticleDTO articleDTO = articleRepository
                .selectArticleByIdForUpdate(articleId)
                .orElseThrow();

        return articleVoteRepository.updateVoteCountThenSelectResult(articleId, currentUserId, articleDTO.getVoteCount() + (articleVoteDTO.isActive() ? 1 : -1));
    }
}
