package com.jme.lsgoldtrade.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Administrator on 2017/11/24.
 */
public class MySection extends SectionEntity<SectionBean> {

    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MySection(SectionBean t) {
        super(t);
    }
}
