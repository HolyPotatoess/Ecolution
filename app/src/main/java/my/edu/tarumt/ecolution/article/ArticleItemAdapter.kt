package my.edu.tarumt.ecolution.article

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.article.ArticleItemAdapter as ItemA

class ArticleItemAdapter(
    private val dataset: List<Article>,
    private val context: Context,
) : RecyclerView.Adapter<ItemA.ItemViewHolder>() {

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textview: TextView = view.findViewById(R.id.item_title)
        val imageView: ImageView = view.findViewById(R.id.image)
        val button = view.findViewById<Button>(R.id.button_read_more)!!
    }

    data class Article (
        @StringRes val stringResourceId : Int,
        @DrawableRes val imageResourceId : Int){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //to show the image of article and headline
        val item = dataset[position]//like for loop
        holder.textview.text = context.resources.getString(item.stringResourceId)
        holder.imageView.setImageResource(item.imageResourceId)

        //create implicit intent, navigate from Read More... button to website
        val website = context.resources.getStringArray(R.array.website)
        val url = website[position]

        holder.button.setOnClickListener {
            // Show the article inside application
            val position = holder.adapterPosition
            val fragment = ArticleWebFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            fragment.arguments = bundle

            val fragmentManager = (holder.view.context as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fl_article, fragment)
                .commit()
        }
    }

    override fun getItemCount(): Int = dataset.size
}


