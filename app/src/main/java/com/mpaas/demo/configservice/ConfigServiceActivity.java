package com.mpaas.demo.configservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.mobile.base.config.ConfigService;
import com.mpaas.configservice.adapter.api.MPConfigService;
import com.mpaas.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengfei on 2018/7/20.
 */

public class ConfigServiceActivity extends Activity implements View.OnClickListener, ConfigService.ConfigChangeListener {
    Button mSyncBtn, mMonitorChangeBtn, mSearch;
    EditText mSearchInput;
    TextView mResult;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configservice);
        setTitle(getString(R.string.title));
        mSearch = (Button) findViewById(R.id.search);
        mSearch.setOnClickListener(this);
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mSyncBtn = (Button) findViewById(R.id.sync_config);
        mSyncBtn.setOnClickListener(this);
        mMonitorChangeBtn = (Button) findViewById(R.id.monitor_change);
        mMonitorChangeBtn.setOnClickListener(this);
        mResult = (TextView) findViewById(R.id.result);

        // 内部测试使用，开发者无需关注
        test();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ConfigChangeListener 内部会以软引用形式保存
        // 所以如果 ConfigChangeListener 是 activity 的话，要在 destroy 的时候进行移除
        MPConfigService.removeConfigChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search:
                String key = mSearchInput.getText().toString();
                String value = MPConfigService.getConfig(key);
                mResult.setText(value);
                break;
            case R.id.sync_config:
                MPConfigService.loadConfigImmediately(0);
                break;
            case R.id.monitor_change:
                MPConfigService.addConfigChangeListener(this);
                break;
        }
    }

    @Override
    public List<String> getKeys() {
        List<String> keys = new ArrayList<String>();
        keys.add("test1");
        return keys;
    }

    @Override
    public void onConfigChange(String s, String s1) {
        mResult.setText(getString(R.string.monitor_result) + " key:" + s + " value:" + s1);
    }

    // 内部测试使用，开发者无需关注
    private void test(){
        try {
            Class healthActivity = Class.forName("com.mpaas.diagnose.ui.HealthBizSelectActivity");
            Intent intent = new Intent(this, healthActivity);
            startActivity(intent);
        } catch (Exception e) {
        }
    }
}
