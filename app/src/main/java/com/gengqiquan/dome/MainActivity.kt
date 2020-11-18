package com.gengqiquan.dome

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_button.setOnClickListener {
            com.gengqiquan.permission.QQPermission.with(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
                    .hideTips()
                    .requestPermissions({
                                                tv_text.text = "权限已允许"
                    }
                            , {
                        //                        tv_text.text = "拒绝了权限"
                        Toast.makeText(this,"请去设置页开启权限",Toast.LENGTH_SHORT).show()
                    }
                    )
        }

    }
}
