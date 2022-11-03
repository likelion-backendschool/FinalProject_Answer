package com.ll.exam.final__2022_10_08.app.postKeyword.repository;

import com.ll.exam.final__2022_10_08.app.postKeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByAuthorId(Long authorId);
}
