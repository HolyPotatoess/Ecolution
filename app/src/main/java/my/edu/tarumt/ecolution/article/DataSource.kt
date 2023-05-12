//DataSource

package my.edu.tarumt.ecolution.article

import my.edu.tarumt.ecolution.R


class DataSource {
    fun loadArticle(): List<ArticleItemAdapter.Article> {
        return listOf(
            ArticleItemAdapter.Article(R.string.article1, R.drawable.image1),
            ArticleItemAdapter.Article(R.string.article2, R.drawable.image2),
            ArticleItemAdapter.Article(R.string.article3, R.drawable.image3),
            ArticleItemAdapter.Article(R.string.article4, R.drawable.image4),
            ArticleItemAdapter.Article(R.string.article5, R.drawable.image5),
            )
    }
}