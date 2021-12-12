package cn.bigorange.wheel.adapter

import cn.bigorange.common.BaseAdapter
import cn.bigorange.common.VBViewHolder
import cn.bigorange.wheel.R
import cn.bigorange.wheel.databinding.ItemNameList2Binding
import cn.bigorange.wheel.entity.Record

class NameListAdapter(data: MutableList<Record>? = null) :
    BaseAdapter<ItemNameList2Binding, Record>(data) {

    init {
        addChildClickViewIds(R.id.iv_edit)
    }

    override fun convert(
        holder: VBViewHolder<ItemNameList2Binding>,
        item: Record
    ) {
        with(holder.vb) {
            tvName.text = item.question
        }
    }

}