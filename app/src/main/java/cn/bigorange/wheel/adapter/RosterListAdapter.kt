package cn.bigorange.wheel.adapter

import cn.bigorange.common.BaseAdapter
import cn.bigorange.common.VBViewHolder
import cn.bigorange.wheel.R
import cn.bigorange.wheel.databinding.ItemRosterBinding
import cn.bigorange.wheel.entity.Roster

class RosterListAdapter(data: MutableList<Roster>? = null) :
    BaseAdapter<ItemRosterBinding, Roster>(data) {

    init {
        addChildClickViewIds(R.id.iv_option)
    }

    override fun convert(
        holder: VBViewHolder<ItemRosterBinding>,
        item: Roster
    ) {
        with(holder.vb) {
            tvName.text = item.title
        }
    }

}