package cn.bigorange.wheel.ui

import cn.bigorange.common.BaseActivity
import cn.bigorange.wheel.database.DatabaseHelper
import cn.bigorange.wheel.databinding.ActivityInputBinding
import cn.bigorange.wheel.entity.Record
import com.blankj.utilcode.util.ArrayUtils

class InputActivity : BaseActivity<ActivityInputBinding>() {

    override fun initView() {
    }


    override fun initData() {


    }


    override fun initListener() {
        binding.header.setOnCommonTitleIconSubClickListener {
            val record = Record()
            record.title = binding.editTitle.text.toString()
            record.optionList = ArrayUtils.asList(binding.editContent.text.toString())
            DatabaseHelper.getInstance().insertRecord(record);
            finish()
        }
    }

}