/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bither.ui.base.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bither.R;
import net.bither.bitherj.utils.Utils;
import net.bither.model.Ticker;
import net.bither.preference.AppSharedPreference;
import net.bither.ui.base.PieChartView;
import net.bither.ui.base.RotatableFrameLayout;
import net.bither.util.MarketUtil;
import net.bither.util.UIUtil;
import net.bither.util.UnitUtilWrapper;

import java.math.BigInteger;

public class DialogTotalBtc extends DialogWithArrow implements PieChartView.RotateListener {
    private static final float PieChartMarginRate = 0.05f;
    private static final float LogoSizeRate = 200.0f / 640.0f;

    private TextView tvBtc;
    private PieChartView vPieChart;
    private FrameLayout flPieContainer;
    private ImageView ivPrivate;
    private TextView tvPrivate;
    private TextView tvPrivateMoney;
    private ImageView ivPrivateSymbol;
    private ImageView ivWatchOnly;
    private TextView tvWatchOnly;
    private TextView tvWatchOnlyMoney;
    private ImageView ivWatchOnlySymbol;
    private ImageView ivHDM;
    private TextView tvHDM;
    private TextView tvHDMMoney;
    private ImageView ivHDMSymbol;
    private ImageView ivHD;
    private TextView tvHD;
    private TextView tvHDMoney;
    private ImageView ivHDSymbol;
    private LinearLayout llPrivate;
    private LinearLayout llWatchOnly;
    private LinearLayout llHDM;
    private LinearLayout llHD;
    private RotatableFrameLayout flLogo;

    private BigInteger btcPrivate;
    private BigInteger btcWatchOnly;
    private BigInteger btcHdm;
    private BigInteger btcHd;

    private double price = 0;

    public DialogTotalBtc(Context context) {
        super(context);
        setContentView(R.layout.dialog_total_btc);
        tvBtc = (TextView) findViewById(R.id.tv_btc);
        vPieChart = (PieChartView) findViewById(R.id.pie);
        flPieContainer = (FrameLayout) findViewById(R.id.fl_pie_container);
        flPieContainer.getLayoutParams().height = flPieContainer.getLayoutParams().width = UIUtil
                .getScreenWidth() - UIUtil.dip2pix(80);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) vPieChart.getLayoutParams();
        int margin = (int) (flPieContainer.getLayoutParams().width * PieChartMarginRate);
        lp.topMargin = margin;
        lp.leftMargin = margin;
        lp.rightMargin = margin;
        lp.bottomMargin = margin;
        ivPrivate = (ImageView) findViewById(R.id.iv_private);
        tvPrivate = (TextView) findViewById(R.id.tv_private);
        tvPrivateMoney = (TextView) findViewById(R.id.tv_private_money);
        ivPrivateSymbol = (ImageView) findViewById(R.id.iv_private_symbol);
        ivWatchOnly = (ImageView) findViewById(R.id.iv_watchonly);
        tvWatchOnly = (TextView) findViewById(R.id.tv_watchonly);
        tvWatchOnlyMoney = (TextView) findViewById(R.id.tv_watchonly_money);
        ivWatchOnlySymbol = (ImageView) findViewById(R.id.iv_watchonly_symbol);
        ivHDM = (ImageView) findViewById(R.id.iv_hdm);
        tvHDM = (TextView) findViewById(R.id.tv_hdm);
        tvHDMMoney = (TextView) findViewById(R.id.tv_hdm_money);
        ivHDMSymbol = (ImageView) findViewById(R.id.iv_hdm_symbol);
        ivHD = (ImageView) findViewById(R.id.iv_hd);
        tvHD = (TextView) findViewById(R.id.tv_hd);
        tvHDMoney = (TextView) findViewById(R.id.tv_hd_money);
        ivHDSymbol = (ImageView) findViewById(R.id.iv_hd_symbol);
        llPrivate = (LinearLayout) findViewById(R.id.ll_private);
        llWatchOnly = (LinearLayout) findViewById(R.id.ll_watchonly);
        llHDM = (LinearLayout) findViewById(R.id.ll_hdm);
        llHD = (LinearLayout) findViewById(R.id.ll_hd);
        flLogo = (RotatableFrameLayout) findViewById(R.id.fl_logo);
        findViewById(R.id.ll_below_chart).setOnClickListener(dismissClick);
        ivHD.setBackgroundDrawable(vPieChart.getSymbolForIndex(0));
        ivHDM.setBackgroundDrawable(vPieChart.getSymbolForIndex(1));
        ivPrivate.setBackgroundDrawable(vPieChart.getSymbolForIndex(2));
        ivWatchOnly.setBackgroundDrawable(vPieChart.getSymbolForIndex(3));
        flLogo.getLayoutParams().width = flLogo.getLayoutParams().height = (int) (flPieContainer
                .getLayoutParams().width * LogoSizeRate);
        vPieChart.setRotateListener(this);
    }

    public void setPrivateWatchOnlyHDMAndHD(BigInteger btcPrivate, BigInteger btcWatchOnly,
                                            BigInteger btcHdm, BigInteger btcHd) {

        BigInteger total = BigInteger.ZERO;
        this.btcPrivate = btcPrivate;
        this.btcWatchOnly = btcWatchOnly;
        this.btcHdm = btcHdm;
        this.btcHd = btcHd;
        if (btcPrivate != null && btcPrivate.signum() > 0) {
            total = total.add(btcPrivate);
        }
        if (btcWatchOnly != null && btcWatchOnly.signum() > 0) {
            total = total.add(btcWatchOnly);
        }
        if (btcHdm != null && btcHdm.signum() > 0) {
            total = total.add(btcHdm);
        }
        if (btcHd != null && btcHd.signum() > 0) {
            total = total.add(btcHd);
        }
        tvBtc.setText(UnitUtilWrapper.formatValue(total.longValue()));
        Bitmap btcSymbol = UnitUtilWrapper.getBtcSlimSymbol(tvPrivate);
        ivPrivateSymbol.setImageBitmap(btcSymbol);
        ivWatchOnlySymbol.setImageBitmap(btcSymbol);
        ivHDMSymbol.setImageBitmap(btcSymbol);
        ivHDSymbol.setImageBitmap(btcSymbol);
        if (btcPrivate != null && btcPrivate.signum() > 0) {
            tvPrivate.setText(UnitUtilWrapper.formatValue(btcPrivate.longValue()));
            llPrivate.setVisibility(View.VISIBLE);
        } else {
            llPrivate.setVisibility(View.GONE);
        }
        if (btcWatchOnly != null && btcWatchOnly.signum() > 0) {
            tvWatchOnly.setText(UnitUtilWrapper.formatValue(btcWatchOnly.longValue()));
            llWatchOnly.setVisibility(View.VISIBLE);
        } else {
            llWatchOnly.setVisibility(View.GONE);
        }
        if (btcHdm != null && btcHdm.signum() > 0) {
            tvHDM.setText(UnitUtilWrapper.formatValue(btcHdm.longValue()));
            llHDM.setVisibility(View.VISIBLE);
        } else {
            llHDM.setVisibility(View.GONE);
        }
        if (btcHd != null && btcHd.signum() > 0) {
            tvHD.setText(UnitUtilWrapper.formatValue(btcHd.longValue()));
            llHD.setVisibility(View.VISIBLE);
        } else {
            llHD.setVisibility(View.GONE);
        }
    }

    @Override
    public void show() {
        vPieChart.setStartAngle(PieChartView.DefaultStartAngle);
        vPieChart.setTotalAngle(0);
        Ticker ticker = MarketUtil.getTickerOfDefaultMarket();
        if (ticker != null) {
            price = ticker.getDefaultExchangePrice();
        } else {
            price = 0;
        }
        String currencySymbol = AppSharedPreference.getInstance().getDefaultExchangeType()
                .getSymbol();
        if (btcPrivate != null && btcPrivate.signum() > 0 && price > 0) {
            tvPrivateMoney.setVisibility(View.VISIBLE);
            tvPrivateMoney.setText(currencySymbol + " " + Utils.formatDoubleToMoneyString(
                    (double) btcPrivate.longValue() / 100000000.0 * price));
        } else {
            tvPrivateMoney.setVisibility(View.GONE);
        }
        if (btcWatchOnly != null && btcWatchOnly.signum() > 0 && price > 0) {
            tvWatchOnlyMoney.setVisibility(View.VISIBLE);
            tvWatchOnlyMoney.setText(currencySymbol + " " + Utils.formatDoubleToMoneyString(
                    (double) btcWatchOnly.longValue() / 100000000.0 * price));
        } else {
            tvWatchOnlyMoney.setVisibility(View.GONE);
        }
        if (btcHdm != null && btcHdm.signum() > 0 && price > 0) {
            tvHDMMoney.setVisibility(View.VISIBLE);
            tvHDMMoney.setText(currencySymbol + " " + Utils.formatDoubleToMoneyString((double)
                    btcHdm.longValue() / 100000000.0 * price));
        } else {
            tvHDMMoney.setVisibility(View.GONE);
        }
        if (btcHd != null && btcHd.signum() > 0 && price > 0) {
            tvHDMoney.setVisibility(View.VISIBLE);
            tvHDMoney.setText(currencySymbol + " " + Utils.formatDoubleToMoneyString((double)
                    btcHd.longValue() / 100000000.0 * price));
        } else {
            tvHDMoney.setVisibility(View.GONE);
        }
        super.show();
        vPieChart.postDelayed(new Runnable() {
            @Override
            public void run() {
                vPieChart.setAmounts(btcHd == null ? BigInteger.ZERO : btcHd, btcHdm == null ?
                                BigInteger.ZERO : btcHdm,
                        btcPrivate == null ? BigInteger.ZERO : btcPrivate,
                        btcWatchOnly == null ? BigInteger.ZERO : btcWatchOnly);
            }
        }, 100);
    }

    private View.OnClickListener dismissClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    @Override
    public void onRotationChanged(float rotation) {
        flLogo.setRotation(rotation);
    }
}
