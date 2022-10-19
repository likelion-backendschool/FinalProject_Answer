package com.ll.exam.final__2022_10_08.app.postkeyword.repository;

import com.ll.exam.final__2022_10_08.app.postkeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByAuthorId(Long authorId);
}
