package cn.bigorange.wheel.ui

import cn.bigorange.common.BaseActivity
import cn.bigorange.wheel.databinding.ActivityEditBinding
import cn.bigorange.wheel.entity.Record
import org.apache.commons.lang3.StringUtils

class EditActivity : BaseActivity<ActivityEditBinding>() {

    override fun initView() {
    }

    override fun initListener() {
        binding.header.setOnCommonTitleIconSubClickListener {

        }
    }

    override fun initData() {
        val record = cn.bigorange.common.utils.ActivityJumpUtils.getExchangeParam(this) as Record?
        if (record != null) {
            binding.editTitle.setText(record.title)
            binding.editContent.setText(StringUtils.join(record.optionList, ","))
        }
    }

}