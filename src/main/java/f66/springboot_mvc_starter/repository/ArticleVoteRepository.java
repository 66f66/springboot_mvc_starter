package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.ArticleVoteDTO;
import f66.springboot_mvc_starter.dto.VoteResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ArticleVoteRepository {

    void toggleVote(Long articleId, Long userId);

    Optional<ArticleVoteDTO> selectVoteByUserIdAndArticleId(Long articleId, Long userId);

    VoteResult updateVoteCountThenSelectResult(Long articleId, Long userId, int voteCount);
}
