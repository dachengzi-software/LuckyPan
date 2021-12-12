package cn.bigorange.wheel.ui

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import cn.bigorange.common.BaseActivity
import cn.bigorange.common.utils.ListUtils
import cn.bigorange.wheel.database.DatabaseHelper
import cn.bigorange.wheel.databinding.ActivityEditBinding
import cn.bigorange.wheel.entity.Record
import com.blankj.utilcode.util.ToastUtils
import org.apache.commons.lang3.StringUtils
import java.util.*


class EditActivity : BaseActivity<ActivityEditBinding>() {

    private var record: Record? = null

    override fun initView() {
    }

    override fun initListener() {
        binding.header.setOnCommonTitleBackClickListener {
            isFangqi()
        }
        binding.header.setOnCommonTitleIconSubClickListener {
            record?.let {
                val question = binding.etQuestion.text.toString();
                if (StringUtils.isEmpty(question)) {
                    ToastUtils.showShort("请输入问题");
                    return@let
                }
                val optionList = ListUtils.strSplitToList(binding.etOptions.text.toString())
                if (optionList == null || optionList.size < 2) {
                    ToastUtils.showShort("请至少输入2个选项");
                    return@let
                }
                it.question = question
                it.optionList = optionList
                DatabaseHelper.getInstance().updateRecordById(it)
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
                binding.tvAmount.text = tempOptionList.size.toString()
            }
        })
    }

    override fun initData() {
        record = intent.getSerializableExtra("record") as Record?
        record?.let {
            binding.etQuestion.setText(it.question)
            binding.etOptions.setText(StringUtils.join(it.optionList, ", "))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isFangqi()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun isFangqi() {
        AlertDialog.Builder(this).setTitle("放弃更改？")
            .setMessage("未保存的内容将会被丢失").setPositiveButton(
                "取消"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }.setNegativeButton(
                "放弃"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                finish()
            }.create().show()
    }
}