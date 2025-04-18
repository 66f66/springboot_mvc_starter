package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.VoteResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleVoteRepository {

    int toggleVote(Long articleId, Long userId);

    VoteResult updateVoteCountThenSelectResult(Long articleId, Long userId);
}
