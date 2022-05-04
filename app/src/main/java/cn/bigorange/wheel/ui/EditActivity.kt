package cn.bigorange.wheel.ui

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import cn.bigorange.common.BaseActivity
import cn.bigorange.common.utils.ListUtils
import cn.bigorange.wheel.R
import cn.bigorange.wheel.database.DatabaseHelper
import cn.bigorange.wheel.databinding.ActivityEditBinding
import cn.bigorange.wheel.entity.Record
import com.blankj.utilcode.util.ToastUtils
import org.apache.commons.lang3.StringUtils


class EditActivity : BaseActivity<ActivityEditBinding>() {

    private var record: Record? = null

    override fun initView() {
    }

    override fun initListener() {
        binding.header.setOnCommonTitleBackClickListener {
//            isFangqi()
            finish()
        }
        binding.header.setOnCommonTitleIconSubClickListener {
            record?.let {
                val question = binding.etTitle.text.toString();
                if (StringUtils.isEmpty(question)) {
                    ToastUtils.showShort("请输入名单标题");
                    return@let
                }
                val optionList = ListUtils.strSplitToList(binding.etOptions.text.toString())
                if (optionList == null || optionList.size < 2) {
                    ToastUtils.showShort("请至少输入2个选项");
                    return@let
                }
                it.question = question
                it.optionList = optionList
                if (it.id != 0L) {
                    DatabaseHelper.getInstance().updateRecordById(it)
                } else {
                    DatabaseHelper.getInstance().insertRecord(it)
                }
                val intent = Intent()
                this@EditActivity.setResult(RESULT_OK, intent)
                this@EditActivity.finish()
            }
        }
        binding.etOptions.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s0: Editable) {
                val tempOptionList = ListUtils.strSplitToList(s0.toString())
                binding.tvAmount.text = resources.getString(R.string.total_amount, tempOptionList.size.toString())
            }
        })
    }

    override fun initData() {
        intent.getStringExtra("id")?.let {
            record = DatabaseHelper.getInstance().selectRecordById(it.toLong())
        }
        if (record == null) {
            record = Record()
        }
        if (record?.id != 0L) {
            binding.header.setTvTitle("修改名单")
            binding.etTitle.setText(record!!.question)
            binding.tvAmount.text = resources.getString(R.string.total_amount, record!!.optionList.size.toString())
            binding.etOptions.setText(StringUtils.join(record!!.optionList, ", "))
        } else {
            binding.header.setTvTitle("新增名单")
            binding.etTitle.setText("")
            binding.tvAmount.text = resources.getString(R.string.total_amount, "0")
            binding.etOptions.setText("")
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            isFangqi()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

//    private fun isFangqi() {
//        AlertDialog.Builder(this).setTitle("放弃更改？")
//            .setMessage("未保存的内容将会被丢失").setPositiveButton(
//                "取消"
//            ) { dialogInterface, i ->
//                dialogInterface.dismiss()
//            }.setNegativeButton(
//                "放弃"
//            ) { dialogInterface, i ->
//                dialogInterface.dismiss()
//                finish()
//            }.create().show()
//    }


}