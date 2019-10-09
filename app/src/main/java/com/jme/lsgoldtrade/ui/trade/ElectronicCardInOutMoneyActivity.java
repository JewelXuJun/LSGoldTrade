package com.jme.lsgoldtrade.ui.trade;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityElectronicCardInOutMoneyBinding;

@Route(path = Constants.ARouterUriConst.ELECTRONICCARDINOUTMONEY)
public class ElectronicCardInOutMoneyActivity extends JMEBaseActivity {

    private ActivityElectronicCardInOutMoneyBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_electronic_card_in_out_money;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trade_capital_transfer, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        setImageViewWideHigh();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityElectronicCardInOutMoneyBinding) mBindingUtil;
    }

    public void setImageViewWideHigh() {
        Bitmap bitmap =  BitmapFactory.decodeResource(getResources(), R.mipmap.ic_inout_money_detail);
        ViewGroup.LayoutParams params = mBinding.img.getLayoutParams();

        float bitmapWidth = bitmap.getWidth();
        float bitmaphight = bitmap.getHeight();
        float bitmapScalew = bitmaphight / bitmapWidth;

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        int imgWidth = outMetrics.widthPixels;

        params.width = imgWidth;
        params.height = (int) (imgWidth * bitmapScalew);

        mBinding.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mBinding.img.setAdjustViewBounds(true);
        mBinding.img.setLayoutParams(params);
        mBinding.img.setImageBitmap(bitmap);
    }
}
