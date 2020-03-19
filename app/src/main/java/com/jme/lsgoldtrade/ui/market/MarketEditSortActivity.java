package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.heaven7.android.dragflowlayout.DragAdapter;
import com.heaven7.android.dragflowlayout.IDraggable;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DensityUtil;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketEditSortBinding;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.MARKETEDITSORT)
public class MarketEditSortActivity extends JMEBaseActivity {

    private ActivityMarketEditSortBinding mBinding;

    private String mMarketUnitValue;
    private int mScreenWidth;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_edit_sort;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.market_edit_sort, true);

        setRightNavigation(getString(R.string.text_save), 0, R.style.ToolbarThemeBlue, () -> timeLineSave());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        getScreenWidth();

        mMarketUnitValue = SharedPreUtils.getString(this, mUser.isLogin() ? SharedPreUtils.MARKET_SORT_LOGIN : SharedPreUtils.MARKET_SORT_UNLOGIN);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.dragFlowLayout.setDragAdapter(new DragAdapter<MarketUnitBean>() {
            @Override
            public int getItemLayoutId() {
                return R.layout.item_market_unit;
            }

            @Override
            public void onBindData(View itemView, int dragState, MarketUnitBean data) {
                itemView.setTag(data);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(DensityUtil.dpTopx(MarketEditSortActivity.this, 5), DensityUtil.dpTopx(MarketEditSortActivity.this, 5)
                        , DensityUtil.dpTopx(MarketEditSortActivity.this, 5), DensityUtil.dpTopx(MarketEditSortActivity.this, 5));
                layoutParams.width = (mScreenWidth - DensityUtil.dpTopx(MarketEditSortActivity.this, 10)) / 3 - 1 - DensityUtil.dpTopx(MarketEditSortActivity.this, 10);

                TextView tv_market_unit = itemView.findViewById(R.id.tv_market_unit);

                tv_market_unit.setLayoutParams(layoutParams);
                tv_market_unit.setText(data.mMarketUnit);
            }

            @NonNull
            @Override
            public MarketUnitBean getData(View itemView) {
                return (MarketUnitBean) itemView.getTag();
            }
        });

        setMarketUnitValue();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityMarketEditSortBinding) mBindingUtil;
    }

    private void getScreenWidth() {
        WindowManager windowManager = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        mScreenWidth = displayMetrics.widthPixels;
    }

    private void setMarketUnitValue() {
        if (TextUtils.isEmpty(mMarketUnitValue))
            return;

        String[] marketUnitValueArray = mMarketUnitValue.split(",");

        if (null == marketUnitValueArray)
            return;

        int size = marketUnitValueArray.length;

        mBinding.dragFlowLayout.prepareItemsByCount(size);

        for (int i = 0; i < size; i++) {
            String marketUnit = marketUnitValueArray[i];

            mBinding.dragFlowLayout.getDragItemManager().addItem(i, new MarketUnitBean(marketUnit, !marketUnit.equals("分时")));
        }
    }

    private String getMarketUnitSortValue() {
        String marketUnitValue = "";

        List<MarketUnitBean> marketUnitBeanList = mBinding.dragFlowLayout.getDragItemManager().getItems();

        if (null != marketUnitBeanList) {
            for (MarketUnitBean marketUnitBean : marketUnitBeanList) {
                if (null != marketUnitBean) {
                    marketUnitValue = marketUnitValue + marketUnitBean.mMarketUnit + ",";
                }
            }
        }

        if (marketUnitValue.endsWith(","))
            marketUnitValue = marketUnitValue.substring(0, marketUnitValue.length() - 1);

        return marketUnitValue;
    }

    private void timeLineSave() {
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", AppConfig.UUID);
        params.put("token", mUser.getToken());
        params.put("sort", getMarketUnitSortValue());

        sendRequest(ManagementService.getInstance().timeLineSave, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "TimeLineSave":
                if (head.isSuccess()) {
                    showShortToast(R.string.market_edit_sort_success);

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_MARKET_UNIT_SORT_SUCCESS, null);
                }

                break;
        }
    }

    private static class MarketUnitBean implements IDraggable {
        String mMarketUnit;
        boolean bDraggable;

        public MarketUnitBean(String marketUnit, boolean draggable) {
            mMarketUnit = marketUnit;
            bDraggable = draggable;
        }

        @Override
        public boolean isDraggable() {
            return bDraggable;
        }

    }

}
