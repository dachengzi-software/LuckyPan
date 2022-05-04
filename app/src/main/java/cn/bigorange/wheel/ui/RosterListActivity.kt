package cn.bigorange.wheel.ui

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bigorange.wheel.R
import cn.bigorange.wheel.adapter.RosterListAdapter
import cn.bigorange.wheel.database.DatabaseHelper
import cn.bigorange.wheel.databinding.ActivityNameListBinding
import cn.bigorange.wheel.entity.Roster


class RosterListActivity : cn.bigorange.common.BaseActivity<ActivityNameListBinding>() {

    private val adapter = RosterListAdapter(ArrayList())

    override fun initView() {
        initRecycler()
    }

    override fun initListener() {
        binding.tvAdd.setOnClickListener {
            val intent = Intent(this, EditRosterActivity::class.java)
            startActivityForResult(intent, 101)
        }
    }

    private fun initRecycler() {
        binding.rvList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.home_recycler_divider)
            ?.let { decoration.setDrawable(it) }
        binding.rvList.addItemDecoration(decoration)
        binding.rvList.adapter = adapter
        adapter.setNewInstance(mockData())
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_option -> {
                    showPopupMenu(view, position)
                }
            }
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            val roster = adapter.data[position] as Roster
            val myAlertDialog = AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否确定选择名单：${roster.title}？")
                .setPositiveButton("确定") { dialog, which ->
                    Toast.makeText(this, "选择了：${roster.title}", Toast.LENGTH_SHORT).show()
//                    val intent = Intent()
//                    intent.putExtra("roster", roster)
//                    setResult(RESULT_OK, intent)
//                    finish()
                }
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }
            myAlertDialog.create().show()
        }
    }

    private fun showPopupMenu(view: View, position: Int) {
        // 这里的view代表popupMenu需要依附的view
        val popupMenu = PopupMenu(this@RosterListActivity, view)
        // 获取布局文件
        popupMenu.menuInflater.inflate(R.menu.sample_menu, popupMenu.menu)
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if (item == null) {
                    return true;
                }
                // 控件每一个item的点击事件
                when (item.itemId) {
                    R.id.edit -> {
                        Toast.makeText(this@RosterListActivity, "点击了编辑：$position", Toast.LENGTH_SHORT).show()
                        val selectedRoster = adapter.data[position]
                        val intent = Intent(this@RosterListActivity, EditRosterActivity::class.java)
                        intent.putExtra("id", selectedRoster.id.toString())
                        startActivityForResult(intent, 100)
                    }
                    R.id.share -> {

                    }
                    R.id.delete -> {
                        val selectedRoster = adapter.data[position]
                        val myAlertDialog = AlertDialog.Builder(this@RosterListActivity)
                            .setTitle("提示")
                            .setMessage("是否删除名单：${selectedRoster.title}？")
                            .setPositiveButton("确定") { dialog, which ->
                                val result = DatabaseHelper.getInstance().deleteRosterById(selectedRoster.id)
                                if (result > 0) {
                                    adapter.removeAt(position)
                                } else {
                                    Toast.makeText(this@RosterListActivity, "删除失败！", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .setNegativeButton("取消") { dialog, which ->
                                dialog.dismiss()
                            }
                        myAlertDialog.create().show()
                    }
                }
                return true
            }
        })
        popupMenu.setOnDismissListener { // 控件消失时的事件
//            Toast.makeText(this@NameListActivity, "关闭PopupMenu", Toast.LENGTH_SHORT).show();
        }
        popupMenu.show()
    }

    private fun mockData(): MutableList<Roster> {
        return DatabaseHelper.getInstance().getAllRosters(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 100 || requestCode == 101) && resultCode == RESULT_OK) {
            adapter.setNewInstance(mockData())
        }
    }
}