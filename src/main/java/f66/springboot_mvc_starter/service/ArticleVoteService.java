package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.VoteResult;
import f66.springboot_mvc_starter.exception.ForbiddenException;
import f66.springboot_mvc_starter.repository.ArticleVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleVoteService {

    private final ArticleVoteRepository articleVoteRepository;

    @Transactional
    public VoteResult voteArticle(Long articleId,
                                  Long currentUserId) {

        int voted = articleVoteRepository.toggleVote(articleId, currentUserId);

        if (voted == 0) {

            throw new ForbiddenException();
        }

        return articleVoteRepository.updateVoteCountAndGetResult(articleId, currentUserId);
    }
}
