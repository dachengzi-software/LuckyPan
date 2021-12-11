package cn.bigorange.wheel.utils

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bigorange.wheel.R

/**
 * 文 件 名: RecyclerUtils
 * 创 建 人: sineom
 * 创建日期: 2019-11-11 14:01
 * 修改时间：
 * 修改备注：
 */

object RecyclerUtils {

    /**
     * 加载recycleView的数据
     * orientation 方向
     * showHorizon 是否显示下划线
     */
    fun initRecycler(
        recyclerView: RecyclerView,
        orientation: Int = RecyclerView.VERTICAL,
        showHorizon: Boolean = true,
    ) {
        val context = recyclerView.context
        recyclerView.layoutManager = LinearLayoutManager(context, orientation, false)
        if (showHorizon) {
            val customDecoration = CustomDecoration(context, CustomDecoration.VERTICAL)
            ContextCompat.getDrawable(context, R.drawable.home_recycler_divider)
                ?.let { customDecoration.setDrawable(it) }
            recyclerView.addItemDecoration(customDecoration)
        }
    }

    /**
     * 加载recycleView的图片数据
     * maxCount 数据 默认3
     * showHorizon 是否显示下划线
     */
    fun initRecyclerView(
        recyclerView: RecyclerView,
        maxCount: Int = 3,
        showHorizon: Boolean = true,
    ) {
        val context = recyclerView.context
        recyclerView.layoutManager = GridLayoutManager(context, maxCount)
        if (showHorizon) {
            val customDecoration = SpaceItemImagesDecoration(context, 3, 11, false)
            recyclerView.addItemDecoration(customDecoration)
        }
    }

}