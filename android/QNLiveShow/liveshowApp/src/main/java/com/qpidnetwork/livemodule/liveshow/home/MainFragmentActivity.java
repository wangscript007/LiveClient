package com.qpidnetwork.livemodule.liveshow.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dou361.dialogui.listener.DialogUIListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qpidnetwork.livemodule.R;
import com.qpidnetwork.livemodule.framework.base.BaseFragmentActivity;
import com.qpidnetwork.livemodule.framework.widget.viewpagerindicator.TabPageIndicator;
import com.qpidnetwork.livemodule.httprequest.LiveDomainRequestOperator;
import com.qpidnetwork.livemodule.httprequest.LiveRequestOperator;
import com.qpidnetwork.livemodule.httprequest.OnGetAccountBalanceCallback;
import com.qpidnetwork.livemodule.httprequest.OnGetMyProfileCallback;
import com.qpidnetwork.livemodule.httprequest.OnRequestCallback;
import com.qpidnetwork.livemodule.httprequest.OnRetrieveBannerCallback;
import com.qpidnetwork.livemodule.httprequest.item.LSLeftCreditItem;
import com.qpidnetwork.livemodule.httprequest.item.LSOtherVersionCheckItem;
import com.qpidnetwork.livemodule.httprequest.item.LSProfileItem;
import com.qpidnetwork.livemodule.httprequest.item.LSRequestEnum;
import com.qpidnetwork.livemodule.httprequest.item.LoginItem;
import com.qpidnetwork.livemodule.im.IMManager;
import com.qpidnetwork.livemodule.im.IMOtherEventListener;
import com.qpidnetwork.livemodule.im.IMShowEventListener;
import com.qpidnetwork.livemodule.im.listener.IMClientListener;
import com.qpidnetwork.livemodule.im.listener.IMLoveLeveItem;
import com.qpidnetwork.livemodule.im.listener.IMPackageUpdateItem;
import com.qpidnetwork.livemodule.im.listener.IMProgramInfoItem;
import com.qpidnetwork.livemodule.im.listener.IMUserBaseInfoItem;
import com.qpidnetwork.livemodule.livechat.contact.ContactManager;
import com.qpidnetwork.livemodule.livechat.contact.OnChatUnreadUpdateCallback;
import com.qpidnetwork.livemodule.liveshow.LiveModule;
import com.qpidnetwork.livemodule.liveshow.WebViewActivity;
import com.qpidnetwork.livemodule.liveshow.authorization.IAuthorizationListener;
import com.qpidnetwork.livemodule.liveshow.authorization.LoginManager;
import com.qpidnetwork.livemodule.liveshow.authorization.LoginNewActivity;
import com.qpidnetwork.livemodule.liveshow.bubble.BubbleMessageBean;
import com.qpidnetwork.livemodule.liveshow.bubble.BubbleMessageManager;
import com.qpidnetwork.livemodule.liveshow.bubble.BubbleMessageType;
import com.qpidnetwork.livemodule.liveshow.bubble.HangoutMsgPopView;
import com.qpidnetwork.livemodule.liveshow.bubble.IBubbleMessageManagerListener;
import com.qpidnetwork.livemodule.liveshow.bubble.IOnHangoutMsgPopListener;
import com.qpidnetwork.livemodule.liveshow.home.menu.DrawerAdapter;
import com.qpidnetwork.livemodule.liveshow.livechat.LiveChatTalkActivity;
import com.qpidnetwork.livemodule.liveshow.liveroom.HangoutTransitionActivity;
import com.qpidnetwork.livemodule.liveshow.liveroom.rebate.LiveRoomCreditRebateManager;
import com.qpidnetwork.livemodule.liveshow.manager.FollowManager;
import com.qpidnetwork.livemodule.liveshow.manager.ShowUnreadManager;
import com.qpidnetwork.livemodule.liveshow.manager.SynConfigerManager;
import com.qpidnetwork.livemodule.liveshow.manager.URL2ActivityManager;
import com.qpidnetwork.livemodule.liveshow.manager.VersionCheckManager;
import com.qpidnetwork.livemodule.liveshow.model.http.HttpRespObject;
import com.qpidnetwork.livemodule.liveshow.personal.SettingsActivity;
import com.qpidnetwork.livemodule.liveshow.sayhi.SayHiListActivity;
import com.qpidnetwork.livemodule.liveshow.urlhandle.AppUrlHandler;
import com.qpidnetwork.livemodule.liveshow.welcome.PeacockActivity;
import com.qpidnetwork.livemodule.utils.ApplicationSettingUtil;
import com.qpidnetwork.livemodule.utils.DisplayUtil;
import com.qpidnetwork.livemodule.utils.FrescoLoadUtil;
import com.qpidnetwork.livemodule.utils.SystemUtils;
import com.qpidnetwork.livemodule.view.MaterialDialogAlert;
import com.qpidnetwork.livemodule.view.NormalWebviewDialog;
import com.qpidnetwork.qnbridgemodule.bean.CommonConstant;
import com.qpidnetwork.qnbridgemodule.bean.WebSiteBean;
import com.qpidnetwork.qnbridgemodule.urlRouter.LiveUrlBuilder;
import com.qpidnetwork.qnbridgemodule.util.BroadcastManager;
import com.qpidnetwork.qnbridgemodule.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MainFragmentActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener,
        IAuthorizationListener, IMShowEventListener, ShowUnreadManager.OnShowUnreadListener,
        IMOtherEventListener, OnChatUnreadUpdateCallback, IBubbleMessageManagerListener {

    private static final String LAUNCH_URL = "launchUrl";
    private static final String CHANGE_WITE_TOKEN = "token";
    private static final String LAUNCH_PARAMS_TABTYPE = "tabType";
    private static final int OFF_SCREEN_PAGE_LIMIT = 3;    //VP预加载页面(3是为了避免用户疯狂切换，导致Fragment不断Create的问题)
    private final double DRAWER_WIDTH_IN_SCREEN = 0.85;     //左则菜单占屏幕宽度比

    //tab相关常量
//    private int TAB_SUM = 4;
//    private int TAB_INDEX_DISCOVER = 0;
//    private int TAB_INDEX_FOLLOW = 1;
//    private int TAB_INDEX_CALENDAR = 2;
//    private int TAB_INDEX_ME = 3;

    //UIHandler常量
    private final int UI_LOGIN_SUCCESS = 3001;
    private final int UI_LOGIN_FAIL = 3002;
    private final int UI_LOGOUT = 3003;
    // 2019/8/22 Hardy
    private final int GET_PROFILE_CALLBACK = 3004;      // 获取个人信息

    //控件
//    private BottomNavigationViewEx mNavView;
//    private QBadgeView mQBadgeViewUnReadMe , mQBadgeViewUnReadCalendar;
//    private RelativeLayout mRlLoginLoading;
//    private LinearLayout mLoginLoading, mLoginFail;
//    private ButtonRaised mBtnRelogin;
    //    private BubbleDialog mBubbleDialogCalendar;
    private ViewPager viewPagerContent;
    //    private MainFragmentPagerAdapter mAdapter;
    private TabPageIndicator tabPageIndicator;
    private MainFragmentPagerAdapter4Top mAdapter;
    //控件－－左则菜单
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLLDrawer;
    private RecyclerView mRvDrawer;
    private DrawerAdapter mDrawerAdapter;
    private LinearLayout mLLHeaderRoot;
    //    private CircleImageView mImgDrawerUserPhoto;
    private SimpleDraweeView mImgUserPhoto;
    public TextView mTvDrawerUserName;
    public TextView mTvDrawerUserId;
    public ImageView mImgDrawerUserLevel, mImgDrawerSetting;
    private View mViewDrawerChangeWebSite, mViewDrawerAddCredit;
    private TextView mTVDrawerAddCredits, mTvCurrCredits;

    // 2019/3/5 Hardy
    private HangoutMsgPopView mMsgPopView;
    private BubbleMessageManager mBubbleMessageManager;

    //内容
    private boolean mNeedShowGuide = true;

    private int mCurrentPageIndex = 0;

    private boolean mIsDrawerOpen = false;          //抽屉是否打开标志

    private boolean hasItemClicked = false;//防止多次点击

    private String mQnToken = "";   //QN换站传入的Token

    //管理信用点及返点
    public LiveRoomCreditRebateManager mLiveRoomCreditRebateManager;

    //存储浮层广告url，防止不可见时无法弹出dialog导致广告丢弃
    private String mOverviewAdvertHtmlString = "";

    //存储主页浮层广告
    private NormalWebviewDialog mNormalWebviewDialog;

    /**
     * 外部启动Url跳转
     *
     * @param context
     * @param url
     * @return
     */
    public static void launchActivityWIthUrl(Context context, String token, String url) {
        Intent intent = new Intent(context, MainFragmentActivity.class);
        intent.putExtra(LAUNCH_URL, url);
        intent.putExtra(CHANGE_WITE_TOKEN, token);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.anim_activity_fade_in, R.anim.anim_activity_fade_out);
        }
    }

    /**
     * 内部启动或者返回
     *
     * @param context
     * @param tabType
     */
    public static void launchActivityWithListType(Context context, MainFragmentPagerAdapter4Top.TABS tabType) {
        Intent intent = new Intent(context, MainFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(LAUNCH_PARAMS_TABTYPE, tabType);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.anim_activity_fade_in, R.anim.anim_activity_fade_out);
        }
    }

    /**
     * 外部启动Url跳转
     *
     * @param context
     * @return
     */
    public static void launchActivityWIthKickoff(Context context, String strKickOffTips) {
        //先回到MAIN
        Intent intent = new Intent(context, MainFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.anim_activity_fade_in, R.anim.anim_activity_fade_out);
        }
        //再去登录
        LoginNewActivity.launchRegisterActivityWithDialog(context, strKickOffTips);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_live_main);
        //
//        setTitle(getString(R.string.live_main_title), R.color.theme_default_black);
//        setTitleVisible(View.GONE);

        TAG = MainFragmentActivity.class.getSimpleName();

        //GA统计，设置Activity不需要report
        SetPageActivity(true);

        //add by Jagger 2018-11-30
        //原本是在登录结果回调检测更新,
        //后来HOT LIST会调取同步配置接口, 同时会取得版本信息,
        //所以改在HOT LIST 初始化之前先监听同步配置, 检测更新, 提高优先级
        SynConfigerManager.getInstance().setSynConfigResultObserver(new Consumer<SynConfigerManager.ConfigResult>() {
            @Override
            public void accept(SynConfigerManager.ConfigResult configResult) throws Exception {
                doCheckUpdate();
            }
        });

        //收藏管理器,在UI前初始化
        FollowManager.newInstance(mContext);

        // 2019/8/22 Hardy
        initGetUserInfoBroadcast();

        initView();

        parseIntent(getIntent());

//        setCenterUnReadNumAndStyle();

        //del by Jagger 2018-2-6 samson说不需要了
        //引导页
//        if(mNeedShowGuide){
//            showGuideView();
//        }

        //监听登录
        LoginManager.getInstance().register(this);

        IMManager.getInstance().registerIMShowEventListener(this);
        IMManager.getInstance().registerIMOtherEventListener(this);

        initBubble();


        // 2018/9/28 Hardy
//        PushSettingManager.getInstance().registerLoginOrIMEvent();

        //根据数据 刷新UI
        refreshUI();

        initData();

        //刷新浮层广告
        getOverviewAdvert();
    }

    //================  个人中心，修改头像后，通知左侧菜单栏更新头像  ==========================
    private void initGetUserInfoBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.ACTION_USER_UPLOAD_PHOTO_SUCCESS_LIVE);
        BroadcastManager.registerReceiver(mContext, getUserInfoBroadcast, intentFilter);
    }

    BroadcastReceiver getUserInfoBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction()) &&
                    CommonConstant.ACTION_USER_UPLOAD_PHOTO_SUCCESS_LIVE.equals(intent.getAction())) {
                getMyProfile();
            }
        }
    };

    /**
     * 获取个人信息
     */
    private void getMyProfile() {
        LiveDomainRequestOperator.getInstance().GetMyProfile(new OnGetMyProfileCallback() {
            @Override
            public void onGetMyProfile(boolean isSuccess, int errno, String errmsg, LSProfileItem item) {
                Message msg = Message.obtain();
                HttpRespObject obj = new HttpRespObject(isSuccess, errno, errmsg, item);
                msg.what = GET_PROFILE_CALLBACK;
                msg.obj = obj;
                sendUiMessage(msg);
            }
        });
    }
    //================  个人中心，修改头像后，通知左侧菜单栏更新头像  ==========================


    /**
     *
     */
    private void initData() {
        //红点未读
        ShowUnreadManager.getInstance().registerUnreadListener(this);

        // 2018/11/20 Hardy
        ContactManager.getInstance().registerChatUnreadUpdateUpdate(this);

        //GA统计
        onAnalyticsPageSelected(1, mCurrentPageIndex);

        mLiveRoomCreditRebateManager = LiveRoomCreditRebateManager.getInstance();

        //判断是否根activity，否则通知关闭其他activity
        if (!isTaskRoot()) {
            Intent intent = new Intent(CommonConstant.ACTION_ACTIVITY_CLOSE);
//            sendBroadcast(intent);
            BroadcastManager.sendBroadcast(mContext, intent);
        }

        //自动登录
        doAutoLogin();
    }

    /**
     * 初始化冒泡事件
     */
    private void initBubble() {
        //初始化冒泡manager
        mBubbleMessageManager = new BubbleMessageManager(this);
        mBubbleMessageManager.setBubbleMessageManagerListener(this);

        List<BubbleMessageBean> dataList = mBubbleMessageManager.getCurrentShowingList();
        if (dataList.size() > 0) {
            mMsgPopView.addMsg(dataList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hasItemClicked = false;
//        Log.d(TAG, "onResume-hasItemClicked:" + hasItemClicked);
        //界面返回，判断有为显示的bubble时，显示冒泡
        if (mIMProgramInfoItem != null) {
            refreshShowUnreadStatus(true);
            mIMProgramInfoItem = null;
            mProgramPlayNoticeMessage = "";
        }

        //add by Jagger 2018-10-18 登录才刷新,减少请求数
        if (LoginManager.getInstance().getLoginStatus() == LoginManager.LoginStatus.Logined) {
            ShowUnreadManager.getInstance().refreshUnReadData();
            updateCredit();
            // 2018/11/20 Hardy
            updateChatUnread();
        }

        //补弹窗overview dialog
        if(!TextUtils.isEmpty(mOverviewAdvertHtmlString)){
            showOverviewAdvertDialog(mOverviewAdvertHtmlString);
            mOverviewAdvertHtmlString = "";
        }
    }

    /**
     * 粗略地监听到 是否有对话框被显示
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //改变锁状态(hasFocus == true)即代表当前没对话框显示, 可以显示下一个新的对话框
        if (hasFocus) {
            hasItemClicked = false;
        }
    }

    /**
     * 更新信用点余额
     */
    public void updateCredit() {
        LiveRequestOperator.getInstance().GetAccountBalance(new OnGetAccountBalanceCallback() {
            @Override
            public void onGetAccountBalance(boolean isSuccess, int errCode, String errMsg, LSLeftCreditItem creditItem) {
                if (isSuccess && creditItem != null) {
                    mLiveRoomCreditRebateManager.setCredit(creditItem.balance);
                    // 2018/9/27 Hardy
                    mLiveRoomCreditRebateManager.setCoupon(creditItem.coupon);

                    mLiveRoomCreditRebateManager.setLiveChatCount(creditItem.liveChatCount);

                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
//                            if (null != mDrawerAdapter) {
//                                mDrawerAdapter.updateCreditsView();
//                            }
                            doUpdateCreditView(true);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        LoginManager.getInstance().unRegister(this);
        IMManager.getInstance().unregisterIMShowEventListener(this);
        IMManager.getInstance().unregisterIMOtherEventListener(this);
        ShowUnreadManager.getInstance().unregisterUnreadListener(this);

        // 2018/11/20 Hardy
        ContactManager.getInstance().unregisterChatUnreadUpdateUpdata(this);

        // 2019/8/22 Hardy
        if (getUserInfoBroadcast != null) {
            BroadcastManager.unregisterReceiver(mContext, getUserInfoBroadcast);
        }

        if (mMsgPopView != null) {
            mMsgPopView.onDestroy();
        }

        if (mBubbleMessageManager != null) {
            mBubbleMessageManager.onDestroy();
        }

        FollowManager.getInstance().destroy();

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //解释参数
        parseIntent(intent);

        //自动登录
        doAutoLogin();
    }

    /**
     * 启动后，处理初始化数据
     *
     * @param intent
     */
    private void parseIntent(Intent intent) {
        Log.d(TAG, "parseIntent");
        Bundle bundle = intent.getExtras();
        String url = "";
        MainFragmentPagerAdapter4Top.TABS tabType = MainFragmentPagerAdapter4Top.TABS.TAB_INDEX_DISCOVER;
        if (bundle != null) {
            if (bundle.containsKey(LAUNCH_URL)) {
                url = bundle.getString(LAUNCH_URL);
            }

            if (bundle.containsKey(CHANGE_WITE_TOKEN)) {
                mQnToken = bundle.getString(CHANGE_WITE_TOKEN);
                Log.i("Jagger", "MainFragmentActivity parseIntent mQnToken:" + mQnToken);
            }
            if (bundle.containsKey(LAUNCH_PARAMS_TABTYPE)) {
                tabType = (MainFragmentPagerAdapter4Top.TABS) bundle.getSerializable(LAUNCH_PARAMS_TABTYPE);
            }
        }

        //根据Url执行跳转
        if (!TextUtils.isEmpty(url)) {
            //url非指向当前main界面
            new AppUrlHandler(mContext).urlHandle(url);
            mNeedShowGuide = false;
        } else {
            mNeedShowGuide = true;
        }

        //切换默认页
        viewPagerContent.setCurrentItem(mAdapter.tabTypeToIndex(tabType));
    }

    private void initView() {
        Log.d(TAG, "initView");

        tabPageIndicator = (TabPageIndicator) findViewById(R.id.tabPageIndicator);
        viewPagerContent = (ViewPager) findViewById(R.id.viewPagerContent);
        //初始化viewpager
//        mAdapter = new MainFragmentPagerAdapter(this);
//        viewPagerContent.setAdapter(mAdapter);
//        //防止间隔点击会出现回收，导致Fragment onresume走出现刷新异常
//        viewPagerContent.setOffscreenPageLimit(2);
//
//        //新版本
//        //TAB
//        mNavView = (BottomNavigationViewEx)findViewById(R.id.navigation);
//        mNavView.enableAnimation(false);
//        mNavView.enableShiftingMode(false);
//        mNavView.enableItemShiftingMode(false);
//        mNavView.setIconUseSelector();
//        initEvent();
//
//        //ME未读数
//        mQBadgeViewUnReadMe = new QBadgeView(mContext);
//        //设置一些公共样式
//        mQBadgeViewUnReadMe
//                .setBadgeNumber(0)  //先隐藏, 因为初始化时取不到准确的坐标,会在右上角先显示一个图标,不好看
//                .setBadgeGravity(Gravity.END | Gravity.TOP)
//                .setShowShadow(false)//不要阴影
//                .bindTarget(mNavView.getBottomNavigationItemView(MainFragmentPagerAdapter.TABS.TAB_INDEX_ME.ordinal()));
//                //打开拖拽消除模式并设置监听
////                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
////                    @Override
////                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
//////                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
//                                    ToastUtil.showToast(MainFragmentActivity.this,R.string.tips_badge_removed);
////                    }
////                });
//
//        //Calendar未读数
//        mQBadgeViewUnReadCalendar = new QBadgeView(mContext);
//        //设置一些公共样式
//        mQBadgeViewUnReadCalendar
//                .setBadgeNumber(0)  //先隐藏, 因为初始化时取不到准确的坐标,会在右上角先显示一个图标,不好看
//                .setBadgeGravity(Gravity.END | Gravity.TOP)
//                .setShowShadow(false)//不要阴影
//                .bindTarget(mNavView.getBottomNavigationItemView(MainFragmentPagerAdapter.TABS.TAB_INDEX_CALENDAR.ordinal()));

        //初始化ViewPage Adapter
        mAdapter = MainFragmentPagerAdapter4Top.newAdapter4NoHangout(this);
        viewPagerContent.setAdapter(mAdapter);
        //防止间隔点击会出现回收，导致Fragment onresume走出现刷新异常
        viewPagerContent.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);

        //初始化Tab
        tabPageIndicator.setViewPager(viewPagerContent);
        //设置每个ITEM宽与字体相同,注:一定要在setIndicatorMode前
        tabPageIndicator.setSameLine(true);
        // 设置控件的模式，一定要先设置模式
        tabPageIndicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_NOWEIGHT_EXPAND_SAME);
        // 设置两个标题之间的竖直分割线的颜色，如果不需要显示这个，设置颜色为透明即可
        tabPageIndicator.setDividerColor(Color.TRANSPARENT);
        //设置页面切换处理
        tabPageIndicator.setOnPageChangeListener(this);
        findViewById(R.id.fl_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //左则菜单
        mDrawerAdapter = new DrawerAdapter(mContext);
        mDrawerAdapter.setOnItemClickListener(new MyOnItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.d(TAG, "onDrawerSlide");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d(TAG, "onDrawerOpened");
                //更新左侧导航菜单栏未读红点数据
                if (null != mDrawerAdapter) {
                    updateUnReadDataOnLeftMenu();

                    // 2018/11/20 Hardy
                    updateChatUnread();
                }
                //柜桶是否打开标志
                mIsDrawerOpen = true;
                // 统计左侧页
                onAnalyticsPageSelected(0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d(TAG, "onDrawerStateChanged");
                if (mIsDrawerOpen) {
                    //柜桶是否打开标志
                    mIsDrawerOpen = false;
                    // 统计主界面页
                    onAnalyticsPageSelected(1, mCurrentPageIndex);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.d(TAG, "onDrawerStateChanged-newState:" + newState);
            }
        });

        //左则菜单－－整个布局
        mLLDrawer = (LinearLayout) findViewById(R.id.ll_drawer);
        ViewGroup.LayoutParams paraDrawer = mLLDrawer.getLayoutParams();
        paraDrawer.width = (int) (DisplayUtil.getScreenWidth(mContext) * DRAWER_WIDTH_IN_SCREEN);
        mLLDrawer.setLayoutParams(paraDrawer);

        //左则菜单－－列表
        mRvDrawer = (RecyclerView) findViewById(R.id.rv_drawer);
        mRvDrawer.setLayoutManager(new LinearLayoutManager(this));
        mRvDrawer.setAdapter(mDrawerAdapter);

        //左则菜单－－ 个人资料
        mLLHeaderRoot = (LinearLayout) findViewById(R.id.ll_header_root);
        mLLHeaderRoot.setLayerType(View.LAYER_TYPE_SOFTWARE, null); //头像网络慢有时为黑:https://blog.csdn.net/huyawenz/article/details/78863636
        //留出状态栏高度 （4.4+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((LinearLayout.LayoutParams) mLLHeaderRoot.getLayoutParams()).topMargin += DisplayUtil.getStatusBarHeight(mContext);
        }
        mLLHeaderRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doShowMyProfile();
            }
        });

        //左则菜单－－人头
//        mImgDrawerUserPhoto = (CircleImageView) findViewById(R.id.civ_userPhoto);
        mImgUserPhoto = findViewById(R.id.img_userPhoto);

        //左则菜单－－个人资料
        mImgDrawerSetting = (ImageView) findViewById(R.id.img_setting);
        mImgDrawerSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doShowSetting();
            }
        });
        mTvDrawerUserName = (TextView) findViewById(R.id.tv_userName);
        mTvDrawerUserId = (TextView) findViewById(R.id.tv_userId);
        mImgDrawerUserLevel = (ImageView) findViewById(R.id.iv_userLevel);
        mImgDrawerUserLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyLevelDetailWebView();
            }
        });

        //左则菜单－－换站
        mViewDrawerChangeWebSite = findViewById(R.id.ll_changeWebSite);
        mViewDrawerChangeWebSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doChangeWebsite();
            }
        });

        //左则菜单－－买点
        mViewDrawerAddCredit = findViewById(R.id.ll_credit_drawer);
        mViewDrawerAddCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCredits();
            }
        });
        mTVDrawerAddCredits = (TextView) findViewById(R.id.tv_addCredits);
        mTVDrawerAddCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCredits();
            }
        });
        mTvCurrCredits = (TextView) findViewById(R.id.tv_currCredits);

        mMsgPopView = findViewById(R.id.view_hang_out_msg_pop);
        mMsgPopView.setVisibility(View.GONE);
        mMsgPopView.setOnHangoutMsgPopListener(new IOnHangoutMsgPopListener() {
            @Override
            public void onHandoutClick(int pos) {
                Log.i(TAG, "onHandoutClick pos:" + pos);
                BubbleMessageBean bean = mMsgPopView.getItem(pos);
                //删除指定pop冒泡
                mMsgPopView.removeMsg(pos);
                //通知manager同步
                mBubbleMessageManager.removeShowingItem(pos, 1);
                //通知服务器点击
                notifyHangoutInviteClick(bean);
                //点击事件处理
                if (bean.bubbleMsgType == BubbleMessageType.Hangout) {
                    //进入hangout过渡页
                    //生成被邀请的主播列表（这里是目标主播一人）
                    ArrayList<IMUserBaseInfoItem> anchorList = new ArrayList<>();
                    anchorList.add(new IMUserBaseInfoItem(bean.anchorId, bean.anchorName, bean.anchorPhotoUrl));
                    //过渡页
                    Intent intent = HangoutTransitionActivity.getIntent(
                            mContext,
                            anchorList,
                            "",
                            "",
                            "",
                            false);
                    startActivity(intent);
                } else if (bean.bubbleMsgType == BubbleMessageType.LiveChat) {
                    //进入chat聊天页面
                    LiveChatTalkActivity.launchChatActivity(mContext, bean.anchorId, bean.anchorName, bean.anchorPhotoUrl);
                }
            }

            @Override
            public void onScrollItemChange(int pos) {

            }
        });


//        //登录遮罩
//        mRlLoginLoading = (RelativeLayout) findViewById(R.id.rl_login_loading);
//        mRlLoginLoading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //什么都不用做, 只是为了不让用户点到后面的列表
//            }
//        });
//        mLoginLoading = (LinearLayout) findViewById(R.id.ll_main_login);
//        mLoginFail = (LinearLayout) findViewById(R.id.ll_main_login_fail);
//        mBtnRelogin = (ButtonRaised) findViewById(R.id.btn_reload);
//        mBtnRelogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showLoging();
//
//                LoginManager.getInstance().reLogin();
//            }
//        });
    }

    /**
     * 通知服务器点击了冒泡hangout邀请
     *
     * @param bean
     */
    private void notifyHangoutInviteClick(BubbleMessageBean bean) {
        if (bean != null && bean.bubbleMsgType == BubbleMessageType.Hangout) {
            LiveRequestOperator.getInstance().AutoInvitationClickLog(bean.anchorId, bean.isAuto, new OnRequestCallback() {
                @Override
                public void onRequest(boolean isSuccess, int errCode, String errMsg) {

                }
            });
        }
    }

    @Override
    public void onDataChangeNotify() {
        Log.i(TAG, "onDataChangeNotify");
        mMsgPopView.notifyAdapterDataChange();
    }


    @Override
    public void onDataAdd(BubbleMessageBean bean) {
        Log.i(TAG, "onDataAdd anchorId:" + bean.anchorId + "  anchorName: " + bean.anchorName);
        mMsgPopView.addMsg(bean);
    }

    @Override
    public void onDataRemove(int startPosition, int count) {
        Log.i(TAG, "onDataRemove startPosition:" + startPosition + "  count: " + count);
        mMsgPopView.removeRange(startPosition, count);
    }

    //------------------ 登录遮罩 --------------------
//    private void showLoging() {
//        mRlLoginLoading.setVisibility(View.VISIBLE);
//        mLoginLoading.setVisibility(View.VISIBLE);
//        mLoginFail.setVisibility(View.GONE);
//    }
//
//    private void showLoginFail() {
//        mRlLoginLoading.setVisibility(View.VISIBLE);
//        mLoginLoading.setVisibility(View.GONE);
//        mLoginFail.setVisibility(View.VISIBLE);
//    }
//
//    private void showLoginSuccess() {
//        mRlLoginLoading.setVisibility(View.GONE);
//        mLoginLoading.setVisibility(View.VISIBLE);
//        mLoginFail.setVisibility(View.GONE);
//    }
    //----------------end 登录遮罩 --------------------

    /**
     * 自动登录
     */
    private void doAutoLogin() {
        //token登录
        if (!TextUtils.isEmpty(mQnToken)) {
            LoginManager.getInstance().loginByToken(mQnToken);
            mQnToken = "";
        } else {
            //自动登录
            LoginManager.getInstance().autoLogin();
        }
    }


    /**
     * drawer(左则菜单) item 点击事件
     */
    private class MyOnItemClickListener implements DrawerAdapter.OnItemClickListener {

        @Override
        public void itemClick(DrawerAdapter.DrawerItemNormal drawerItemNormal) {
            switch (drawerItemNormal.id) {
                case DrawerAdapter.ITEM_ID_SAYHI:
                    //打开SayHi列表
                    SayHiListActivity.startAct(mContext);
                    break;
                case DrawerAdapter.ITEM_ID_CHAT:
                    showChatListActivity();
                    break;

                case DrawerAdapter.ITEM_ID_MESSAGE: {
                    showMessageListActivity();
                }
                break;
                case DrawerAdapter.ITEM_ID_MAIL: {
                    showEmfListWebView();
                }
                break;
                case DrawerAdapter.ITEM_ID_GREETS: {//意向信
                    showLoiListWebView();
                }
                break;
                case DrawerAdapter.ITEM_ID_HANGOUT: {//Hang-Out
                    viewPagerContent.setCurrentItem(mAdapter.tabTypeToIndex(MainFragmentPagerAdapter4Top.TABS.TAB_INDEX_HANGOUT));
                }
                break;
                case DrawerAdapter.ITEM_ID_MY_CONTEACT: {
                    // 2019/8/9 Hardy   My Contact
                    showMyContactList();
                }
                break;
                case DrawerAdapter.ITEM_ID_FLOWERS:     //鲜花礼品
                    if (!hasItemClicked) {
                        hasItemClicked = true;
                        String urlMyTickets = LiveUrlBuilder.createGiftFlowersListUrl();
                        new AppUrlHandler(mContext).urlHandle(urlMyTickets);
                    }
                    break;
                case DrawerAdapter.ITEM_ID_SHOWTICKETS:
                    if (!hasItemClicked) {
                        hasItemClicked = true;
                        //edit by Jagger 2018-9-21 使用URL方式跳转
                        String urlMyTickets = LiveUrlBuilder.createMyTickets(0);
                        new AppUrlHandler(mContext).urlHandle(urlMyTickets);
                    }
                    break;
                case DrawerAdapter.ITEM_ID_BOOKS:
                    if (!hasItemClicked) {
                        hasItemClicked = true;
                        //edit by Jagger 2018-9-21 使用URL方式跳转
                        String urlMyBooking = LiveUrlBuilder.createScheduleInviteActivity(1);
                        new AppUrlHandler(mContext).urlHandle(urlMyBooking);
                    }
                    break;
                case DrawerAdapter.ITEM_ID_BACKPACK:
                    if (!hasItemClicked) {
                        hasItemClicked = true;
                        //edit by Jagger 2018-9-21 使用URL方式跳转
                        String urlMyBackpack = LiveUrlBuilder.createMyBackpackActivity(0);
                        new AppUrlHandler(mContext).urlHandle(urlMyBackpack);
                    }
                    break;

            }

//            postUiRunnableDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                    onMenuClickDrawerCloseAnalytics();
//                }
//            }, 1000);
            delayDismissDrawableLayout();
        }

        @Override
        public void onChangeWebsiteClicked() {
            doChangeWebsite();
        }

        @Override
        public void onShowMyProfileClicked() {
            if (!hasItemClicked) {
                hasItemClicked = true;
                doShowMyProfile();
            }
        }

        @Override
        public void onAddCreditsClicked() {
            addCredits();
        }

        @Override
        public void onShowLevelDetailClicked() {
            if (!hasItemClicked) {
                hasItemClicked = true;
                showMyLevelDetailWebView();
            }
        }

        @Override
        public void onWebSiteChoose(WebSiteBean webSiteBean) {
            if (webSiteBean != null && !hasItemClicked) {
                hasItemClicked = true;
                LiveModule.getInstance().doChangeWebSite(webSiteBean);
            }
        }
    }

    private void delayDismissDrawableLayout() {
        postUiRunnableDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                onMenuClickDrawerCloseAnalytics();
            }
        }, 1000);
    }

    /**
     * 点击买点响应
     */
    public void addCredits() {
        if (!hasItemClicked) {
            hasItemClicked = true;
            //edit by Jagger 2018-9-21 使用URL方式跳转
            String urlAddCredit = LiveUrlBuilder.createAddCreditUrl("", "B30", "");
            new AppUrlHandler(this).urlHandle(urlAddCredit);
        }
    }

    public void showMessageListActivity() {
        if (!hasItemClicked) {
            hasItemClicked = true;
            //私信
            //edit by Jagger 2018-9-21 使用URL方式跳转
            String urlMessageList = LiveUrlBuilder.createMessageListUrl();
            new AppUrlHandler(this).urlHandle(urlMessageList);
        }
    }

    public void showChatListActivity() {
        if (!hasItemClicked) {
            hasItemClicked = true;

            //edit by Hardy 2018-11-17 使用URL方式跳转
            String urlMessageList = LiveUrlBuilder.createLiveChatListUrl();
            new AppUrlHandler(this).urlHandle(urlMessageList);
        }
    }


    /**
     * 跳转用户等级说明界面
     */
    private void showMyLevelDetailWebView() {
        if (LoginManager.getInstance().getSynConfig() != null) {
            startActivity(WebViewActivity.getIntent(this,
                    getResources().getString(R.string.my_level_title),
                    WebViewActivity.UrlIntent.View_Audience_Level,
                    null, true));
        }
    }

    /**
     * 跳转到意向信列表界面
     */
    public void showLoiListWebView() {
        if (!hasItemClicked) {
            hasItemClicked = true;
            //edit by Jagger 2018-9-21 使用URL方式跳转
            String urlLoiList = LiveUrlBuilder.createGreetMailList();
            new AppUrlHandler(this).urlHandle(urlLoiList);
        }
    }

    /**
     * 查看主播来信
     */
    public void showEmfListWebView() {
        if (!hasItemClicked) {
            hasItemClicked = true;
            //edit by Jagger 2018-9-21 使用URL方式跳转
            String urlMailList = LiveUrlBuilder.createMailList();
            new AppUrlHandler(this).urlHandle(urlMailList);
        }
    }

    /**
     * 查看主播来信
     */
    public void showMyContactList() {
        if (!hasItemClicked) {
            hasItemClicked = true;

            // 2019/8/16 Hardy
            String urlMyContactList = LiveUrlBuilder.createMyContactListUrl();
            new AppUrlHandler(this).urlHandle(urlMyContactList);
        }
    }

    /**
     * 根据数据 刷新UI
     */
    private void refreshUI() {
        //如果登录中, 只看到loading
        if (LoginManager.getInstance().getLoginStatus() == LoginManager.LoginStatus.Logining) {
//            showLoging();
            doUpdateHeaderView(false);
            doUpdateCreditView(false);
            doUpdateDrawerView(false);
            doUpdateTab(false);
        } else if (LoginManager.getInstance().getLoginStatus() == LoginManager.LoginStatus.Default) {
//            showLoginFail();
            doUpdateHeaderView(false);
            doUpdateCreditView(false);
            doUpdateDrawerView(false);
            doUpdateTab(false);
        } else {
//            showLoginSuccess();
            doUpdateHeaderView(true);
            doUpdateCreditView(true);
            doUpdateDrawerView(true);
            doUpdateTab(true);
        }
    }

    /**
     * viewpager与nav之间的事件
     */
//    private void initEvent() {
//        // set listener to change the current item of view pager when click bottom nav item
//        mNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            private int previousItemId = -1;
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                // you can write as above.
//                // I recommend this method. You can change the item order or counts without update code here.
//                int tempItemId = item.getItemId();
//                if (previousItemId != tempItemId) {
//                    // only set item when item changed
//                    previousItemId = tempItemId;
//
//                    if(previousItemId == R.id.navigation_discover){
//                        viewPagerContent.setCurrentItem(MainFragmentPagerAdapter.TABS.TAB_INDEX_DISCOVER.ordinal());
//                        return true;
//                    }else if(previousItemId == R.id.navigation_follow){
//                        viewPagerContent.setCurrentItem(MainFragmentPagerAdapter.TABS.TAB_INDEX_FOLLOW.ordinal());
//                        return true;
//                    }
//                    else if(previousItemId == R.id.navigation_me){
//                        viewPagerContent.setCurrentItem(MainFragmentPagerAdapter.TABS.TAB_INDEX_ME.ordinal());
//                        //代表每次切换到PersonalCenterFragment都要刷新数据
//                        //要配合MainFragmentPagerAdapter.getItemPosition()实现
//                        //del by Jagger 移到setUserVisibleHint处理
////                        mAdapter.notifyDataSetChanged();
//                        return true;
//                    }else if(previousItemId == R.id.navigation_calendar){
//                        viewPagerContent.setCurrentItem(MainFragmentPagerAdapter.TABS.TAB_INDEX_CALENDAR.ordinal());
//                        return true;
//                    }
//                }
//                return true;
//            }
//        });
//
//        // set listener to change the current checked item of bottom nav when scroll view pager
//        viewPagerContent.addOnPageChangeListener(this);
//    }

    /**
     * 显示未读数目
     */
    private void setCenterUnReadNumAndStyle() {
//        //刷新邀请未读数目
//        ScheduleInviteUnreadItem inviteItem = mScheduleInvitePackageUnreadManager.getScheduleInviteUnreadItem();
//        //刷新背包未读数目
//        PackageUnreadCountItem packageItem = mScheduleInvitePackageUnreadManager.getPackageUnreadCountItem();
//
//
////        if(null == inviteItem || inviteItem.total == 0) {
////            if(null == packageItem || packageItem.total == 0){
////                setUnReadHide(mQBadgeViewUnReadMe);
////            }else {
////                //没指定数目时,显示一个小小的红点
////                setUnReadTag(mQBadgeViewUnReadMe);
////            }
////        }else {
////            //大于99时,显示99+
////            String strUnreadText = inviteItem.total > 99? "99+":String.valueOf(inviteItem.total);
////            //因为要在Nav画好后,才可以取得NavItem的宽, 所以要重设置位置
////            setUnReadText(mQBadgeViewUnReadMe, strUnreadText);
////        }
    }

//    /**
//     * 隐藏 未读
//     */
//    private void setUnReadHide(QBadgeView qBadgeView){
//        //不显示
//        qBadgeView.setBadgeNumber(0);
//    }
//
//    /**
//     * 未读显示为 红点
//     */
//    private void setUnReadTag(QBadgeView qBadgeView){
//        //因为要在Nav画好后,才可以取得NavItem的宽, 所以要重设置位置
//        qBadgeView.setGravityOffset(getUnReadNumOffset("0"), 5, true)
//                .setBadgePadding(5, true);
//
//        qBadgeView.setBadgeText("");
//    }
//
//    /**
//     * 未读显示 内容
//     * @param text
//     */
//    private void setUnReadText(QBadgeView qBadgeView, String text){
//        //重设部分样式
//        //因为要在Nav画好后,才可以取得NavItem的宽, 所以要重设置位置
//        qBadgeView.setGravityOffset(getUnReadNumOffset(text), 5, true)
//                .setBadgeTextSize(10 ,true)
//                .setBadgePadding(2 , true);
//
//        qBadgeView.setBadgeText(text);
//    }
//
//    /**
//     * 计算未读红点 右偏移, 以文字内容大小向左移
//     * @return
//     */
//    private int getUnReadNumOffset(String text){
//        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        int textWidth = (int)textPaint.measureText(text);
//
//        int offset = 0;
//        //取其中一个TabItem的宽来计算
//        BottomNavigationItemView v = mNavView.getBottomNavigationItemView(MainFragmentPagerAdapter.TABS.TAB_INDEX_ME.ordinal());
//        offset = v.getWidth()/3 - textWidth;    //画在居右1/3的位置
//        offset = DisplayUtil.px2dip(mContext , offset);
//        return offset;
//    }

    /**
     * 切换到页
     *
     * @param pageId
     */
    public void setCurrentPager(int pageId) {
        viewPagerContent.setCurrentItem(pageId);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        //换站
        if (i == R.id.btn_main_title_back) {
//            finish();
            doChangeWebsite();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //按返回键回桌面
            moveTaskToBack(true);
            //发送广播，模似HOME键，BaseFragment要处理HOME键事件
            // 2018/12/19 Hardy 这里不需修改成 App 广播，由于该 aciton 是监听系统 home 键的点击事件，故这里不做处理
            sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 被踢提示
     */
    private void showKickOffDialog() {
        MaterialDialogAlert dialog = new MaterialDialogAlert(this);
        dialog.setCancelable(false);
        dialog.setMessage("Your account logined on another device. Please login again.");
        dialog.addButton(dialog.createButton(getString(R.string.common_btn_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainFragmentActivity.this, PeacockActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }));
        dialog.show();
    }

    /**
     * 换站
     * （QN换站弹出框）
     */
    private void doChangeWebsite() {
        if (!hasItemClicked) {
            hasItemClicked = true;
            LiveModule.getInstance().onChangeWebsiteDialogShow(this);
        }
    }

    /**
     * 打开QN个人资料
     */
    private void doShowMyProfile() {
        if (LoginManager.getInstance().getLoginStatus() == LoginManager.LoginStatus.Logined) {
            // 2018/9/18 Hardy
            //edit by Jagger 2018-9-28 使用URL方式跳转
            String urlMyProfileList = LiveUrlBuilder.createMyProfile();
            URL2ActivityManager.getInstance().URL2Activity(mContext, urlMyProfileList);
        } else {
            LoginNewActivity.launchRegisterActivity(mContext, null);
        }
        delayDismissDrawableLayout();
    }

    /**
     * 打开设置
     */
    private void doShowSetting() {
        SettingsActivity.startAct(this);
        delayDismissDrawableLayout();
    }

    private void setIconUrl(String photoUrl){
        int wh = DisplayUtil.dip2px(mContext, 70);
        //"http://demo.charmdate.com/man_photo/105/CM68107045.jpg" //头像https+test5149验证不行，但http可以
        FrescoLoadUtil.loadUrl(mContext, mImgUserPhoto, photoUrl, wh,
                R.drawable.ic_default_photo_man, true,
                getResources().getDimensionPixelSize(R.dimen.live_size_4dp),
                ContextCompat.getColor(mContext, R.color.white));
    }

    /**
     * 更新左则菜单个人资料
     *
     * @param isLogin
     */
    private void doUpdateHeaderView(boolean isLogin) {
        if (isLogin) {
            LoginItem loginItem = LoginManager.getInstance().getLoginItem();
            if (null != loginItem) {
//                int wh = DisplayUtil.dip2px(mContext, 70);
//                //"http://demo.charmdate.com/man_photo/105/CM68107045.jpg" //头像https+test5149验证不行，但http可以
//                FrescoLoadUtil.loadUrl(mContext, mImgUserPhoto, loginItem.photoUrl, wh,
//                        R.drawable.ic_default_photo_man, true,
//                        getResources().getDimensionPixelSize(R.dimen.live_size_4dp),
//                        ContextCompat.getColor(mContext, R.color.white));
                // 2019/8/22 Hardy
                setIconUrl(loginItem.photoUrl);

                //
                mTvDrawerUserName.setText(loginItem.nickName);
                mTvDrawerUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                mTvDrawerUserId.setText(loginItem.userId);
                mImgDrawerUserLevel.setImageDrawable(DisplayUtil.getLevelDrawableByResName(mContext, loginItem.level));
            }
        } else {
            mTvDrawerUserName.setText(getString(R.string.txt_login));
            mTvDrawerUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginNewActivity.launchRegisterActivity(mContext);
                }
            });
            mTvDrawerUserId.setText("");
            mImgDrawerUserLevel.setImageDrawable(null);
            FrescoLoadUtil.loadRes(mContext, mImgUserPhoto, R.drawable.ic_default_photo_man,
                    R.drawable.ic_default_photo_man, true,
                    getResources().getDimensionPixelSize(R.dimen.live_size_4dp),
                    ContextCompat.getColor(mContext, R.color.white));


        }
    }

    /**
     * 更新左则菜单信用点
     *
     * @param isLogin
     */
    private void doUpdateCreditView(boolean isLogin) {
        if (isLogin) {
            LiveRoomCreditRebateManager liveRoomCreditRebateManager = LiveRoomCreditRebateManager.getInstance();
            String currCredits = ApplicationSettingUtil.formatCoinValue(liveRoomCreditRebateManager.getCredit());
            mTvCurrCredits.setText(mContext.getResources().getString(R.string.left_menu_credits_balance, currCredits));
        } else {
            mTvCurrCredits.setText(mContext.getResources().getString(R.string.left_menu_credits_balance, "0"));
        }
    }

    /**
     * 更新左则菜单视图
     *
     * @param isLogin
     */
    private void doUpdateDrawerView(boolean isLogin) {
        if (isLogin) {
            LoginItem loginItem = LoginManager.getInstance().getLoginItem();
            if (null != loginItem) {
                //是否有HangOut权限
                if (loginItem.userPriv.hangoutPriv.isHangoutPriv) {
                    mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_HANGOUT, true);
                } else {
                    mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_HANGOUT, false);
                }

                //是否有SayHi权限
                if (loginItem.userPriv.isSayHiPriv) {
                    mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_SAYHI, true);
                } else {
                    mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_SAYHI, false);
                }

                //鲜花礼品 权限
                if(loginItem.userPriv.isGiftFlowerPriv && loginItem.isGiftFlowerSwitch){
                    mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_FLOWERS, true);
                    // OFF信息
                    int off = SynConfigerManager.getInstance().getConfigItemCache().flowersGift;
                    if(off > 0 ){
                        mDrawerAdapter.showPopMsg(DrawerAdapter.ITEM_ID_FLOWERS, getResources().getString(R.string.flowers_memu_pop_msg, off));
                    }
                }else{
                    mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_FLOWERS, false);
                }
            }

        } else {
            mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_HANGOUT, false);
            mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_SAYHI, false);
            mDrawerAdapter.setItemVisible(DrawerAdapter.ITEM_ID_FLOWERS, false);
        }
    }

    /**
     * 更新顶部Tab
     *
     * @param isLogin
     */
    private void doUpdateTab(boolean isLogin) {
        if (isLogin) {
            LoginItem loginItem = LoginManager.getInstance().getLoginItem();
            //是否有HangOut权限
            if (loginItem!= null && loginItem.userPriv != null && loginItem.userPriv.hangoutPriv != null && loginItem.userPriv.hangoutPriv.isHangoutPriv) {
                //有HangOut权限
                mAdapter = MainFragmentPagerAdapter4Top.newAdapter4HasHangout(this);
                viewPagerContent.setAdapter(mAdapter);
                tabPageIndicator.notifyDataSetChanged();
            } else {
                //无HangOut权限
                mAdapter = MainFragmentPagerAdapter4Top.newAdapter4NoHangout(this);
                viewPagerContent.setAdapter(mAdapter);
                tabPageIndicator.notifyDataSetChanged();
            }
        } else {
            mAdapter = MainFragmentPagerAdapter4Top.newAdapter4NoHangout(this);
            viewPagerContent.setAdapter(mAdapter);
            tabPageIndicator.notifyDataSetChanged();
        }
    }


    @Override
    protected void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case GET_PROFILE_CALLBACK: {
                HttpRespObject responseProfile = (HttpRespObject) msg.obj;
                if (responseProfile.isSuccess) {
                    LSProfileItem mProfileItem = (LSProfileItem) responseProfile.data;
                    if (mProfileItem != null) {
                        setIconUrl(mProfileItem.photoURL);
                    }
                }
            }
            break;

            //登录成功
            case UI_LOGIN_SUCCESS: {
//                showLoginSuccess();

                //del by Jagger 2019-7-25 for test
//                if (mAdapter.getCurrentFragment() instanceof HotListFragment) {
//                    ((HotListFragment) mAdapter.getCurrentFragment()).reloadData();
//                }

                //更新头像昵称ID
//                if(null != mDrawerAdapter){
//                    mDrawerAdapter.updateHeaderView();
//                }
                doUpdateHeaderView(true);
                doUpdateCreditView(true);
                doUpdateDrawerView(true);
                doUpdateTab(true);

                //检测更新(因为登录时才会去拿同步配置)
//                doCheckUpdate();
                //刷新浮层广告
                getOverviewAdvert();
            }
            break;
            case UI_LOGOUT: {
//                showLoginFail();

                //更新头像昵称ID
                doUpdateHeaderView(false);
                doUpdateCreditView(false);
                doUpdateDrawerView(false);
                doUpdateTab(false);

                //收起正在展示的浮层广告
                if(mNormalWebviewDialog != null && mNormalWebviewDialog.isShowing()){
                    mNormalWebviewDialog.dismiss();
                }
            }break;
        }
    }


    /************************************  ViewPager切换事件  *******************************************/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //记录当前选中页
        mCurrentPageIndex = position;
//        if(mCurrentPageIndex == 2){
//            //当前选中calendar，清除冒泡
//            hideCalendarBubble();
//        }

        //viewpager与tab事件绑定
//        mNavView.setCurrentItem(position);

        //GA统计
        onAnalyticsPageSelected(1, position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /************************************  认证登录事件回调  *******************************************/
    @Override
    public void onLogin(boolean isSuccess, int errCode, String errMsg, LoginItem item) {
        if (isSuccess) {
//            //监听一次即可
//            LoginManager.getInstance().unRegister(this);

            //更新UI
            sendEmptyUiMessage(UI_LOGIN_SUCCESS);
        }
//        }
// else {
//            //更新UI
//            sendEmptyUiMessage(UI_LOGIN_FAIL);
//        }
    }

    @Override
    public void onLogout(boolean isMannual) {
        //更新UI
        sendEmptyUiMessage(UI_LOGOUT);
    }

    /************************************  节目未读事件通知  *******************************************/
    private boolean mIsProgramHasUnread = false;        //标记节目是否有未读

    /**
     * 获取节目未读状态
     *
     * @return
     */
    public boolean getProgramHasUnread() {
        return mIsProgramHasUnread;
    }

    /**
     * 刷新节目未读状态
     *
     * @param isHasShowTicketsUnRead
     */
    public void refreshShowUnreadStatus(boolean isHasShowTicketsUnRead) {
        Log.d(TAG, "refreshShowUnreadStatus-isHasShowTicketsUnRead:" + isHasShowTicketsUnRead);
        mIsProgramHasUnread = isHasShowTicketsUnRead;
//        if(unreadNum > 0){
//            setUnReadTag(mQBadgeViewUnReadCalendar);
//        }else{
//            setUnReadHide(mQBadgeViewUnReadCalendar);
//        }
        int index = 2;
        if(mAdapter != null) {
            index = mAdapter.tabTypeToIndex(MainFragmentPagerAdapter4Top.TABS.TAB_INDEX_CALENDAR);
        }
        tabPageIndicator.updateTabDiginalHint(index, mIsProgramHasUnread, true, 0);
    }

    /************************************  节目开始结束退票事件通知  *******************************************/
    private IMProgramInfoItem mIMProgramInfoItem;
    private String mProgramPlayNoticeMessage = "";

    @Override
    public void OnRecvProgramPlayNotice(final IMProgramInfoItem showinfo, final IMClientListener.IMProgramPlayType type, final String msg) {
        Log.d(TAG, "OnRecvProgramPlayNotice-showinfo:" + showinfo + " type:" + type + " msg:" + msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (type != IMClientListener.IMProgramPlayType.Unknown && (showinfo != null)) {
                    //收到关注开播处理
                    //Modify by Harry 2018年8月3日 15:54:29 按照原型:
                    // 推送购票/关注节目开播通知时同时出现未读红点
                    // （若刚好停留在calendar页，红点仍会显示，切换到其他tab重新进入或下拉刷新列表后红点消失）
//                    if(mCurrentPageIndex != 2){
                    if (isActivityVisible()) {
                        refreshShowUnreadStatus(true);
                    } else {
                        //不在当前界面，存放本地数据用于返回显示即可
                        mIMProgramInfoItem = showinfo;
                        mProgramPlayNoticeMessage = msg;
                    }
//                    }
                }
            }
        });
    }

    /**
     * 解决点击菜单跳转功能页面时，调用closeDrawer会出现延迟回调onDrawerClosed（activity resume时才回调），
     * 导致GA统计异常
     */
    public void onMenuClickDrawerCloseAnalytics() {
        if (mIsDrawerOpen) {
            mIsDrawerOpen = false;
            // 统计主界面页
            onAnalyticsPageSelected(1, mCurrentPageIndex);
        }
    }

    /**
     * 检测更新
     */
    private void doCheckUpdate() {
        final LSOtherVersionCheckItem versionCheckItem = VersionCheckManager.getInstance().getVersionInfoCache();
        if (versionCheckItem != null) {
            //如果有更新信息
            if (versionCheckItem.isForceUpdate) {
                if (versionCheckItem.verCode > SystemUtils.getVersionCode(mContext)) {
                    VersionCheckManager.getInstance().showUpdateDialog(this, new DialogUIListener() {
                        @Override
                        public void onPositive() {
                            finish();
                            System.exit(9);
                        }

                        @Override
                        public void onNegative() {
                            Uri uri = Uri
                                    .parse(versionCheckItem.storeUrl);
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                            finish();
                            System.exit(9);
                        }
                    });
                }
            }
        }
    }

    //=================================红点未读展示逻辑===================================

    private void updateUnReadDataOnLeftMenu() {
        //del by Jagger 2018-10-26 登录/注销都要刷新界面
//        if (LoginManager.getInstance().getLoginStatus() == LoginManager.LoginStatus.Logined) {
        int unreadNum = ShowUnreadManager.getInstance().getMsgUnReadNum();
        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_MESSAGE, unreadNum);
        unreadNum = ShowUnreadManager.getInstance().getMailUnReadNum();
        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_MAIL, unreadNum);
        unreadNum = ShowUnreadManager.getInstance().getGreetMailUnReadNum();
        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_GREETS, unreadNum);
        unreadNum = ShowUnreadManager.getInstance().getShowTicketUnreadNum();
        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_SHOWTICKETS, unreadNum);
        //修改背包未读，增加Livechat试聊券未读数目
        unreadNum = ShowUnreadManager.getInstance().getBackpackUnreadNum() + ShowUnreadManager.getInstance().getLivechatVocherUnreadNum();
        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_BACKPACK, unreadNum);
        unreadNum = ShowUnreadManager.getInstance().getBookingUnReadNum();
        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_BOOKS, unreadNum);
        unreadNum = ShowUnreadManager.getInstance().getSayHiUnreadNum();
        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_SAYHI, unreadNum);

        // 2018/11/16 Hardy
//        unreadNum = ShowUnreadManager.getInstance().getBookingUnReadNum();
//        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_CHAT, unreadNum);
//        }
    }

    @Override
    public void onUnReadDataUpdate() {
        Log.d(TAG, "onUnReadDataUpdate");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUnReadDataOnLeftMenu();
            }
        });
    }


    //===================   2018/11/20  Hardy   LiveChat 未读数刷新逻辑 ================
    private void updateChatUnread(int unreadChatCount, int unreadInviteCount) {
        mDrawerAdapter.showUnReadNum(DrawerAdapter.ITEM_ID_CHAT, unreadChatCount);
    }

    private void updateChatUnread() {
        updateChatUnread(ContactManager.getInstance().getAllUnreadCount(),
                ContactManager.getInstance().getInviteListSize());
    }

    @Override
    public void onUnreadUpdate(final int unreadChatCount, final int unreadInviteCount) {
        Log.d(TAG, "onUnreadUpdate: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateChatUnread(unreadChatCount, unreadInviteCount);
            }
        });
    }

    /**
     * 刷新浮层广告
     */
    private void getOverviewAdvert(){
        LoginManager.LoginStatus loginStatus = LoginManager.getInstance().getLoginStatus();
        LoginItem loginItem = LoginManager.getInstance().getLoginItem();
        String manId = "";
        if(loginItem != null){
            manId = loginItem.userId;
        }
        if(loginStatus != null && loginStatus == LoginManager.LoginStatus.Logined && !TextUtils.isEmpty(manId)){
            //已登陆才刷新
            LiveRequestOperator.getInstance().RetrieveBanner(manId, false, LSRequestEnum.LSBannerType.Unknow, new OnRetrieveBannerCallback() {
                @Override
                public void onRetrieveBanner(boolean isSuccess, int errCode, String errMsg, final String htmlUrl) {
                    if(isSuccess && !TextUtils.isEmpty(htmlUrl)){
                        if(isActivityVisible()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showOverviewAdvertDialog(htmlUrl);
                                }
                            });
                        }else{
                            mOverviewAdvertHtmlString = htmlUrl;
                        }
                    }
                }
            });
        }
    }

    /**
     * 弹出浮层dialog
     * @param htmlData
     */
    private void showOverviewAdvertDialog(String htmlData){
        mNormalWebviewDialog = new NormalWebviewDialog(MainFragmentActivity.this);
        mNormalWebviewDialog.loadData(htmlData, 0);
        mNormalWebviewDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        mNormalWebviewDialog.show();
    }

    //===================   2018/11/20  Hardy   LiveChat 未读数刷新逻辑 ================

    @Override
    public void OnLogin(IMClientListener.LCC_ERR_TYPE errType, String errMsg) {
        Log.d(TAG, "OnLogin-errType:" + errType + " errMsg:" + errMsg);
        if (errType == IMClientListener.LCC_ERR_TYPE.LCC_ERR_SUCCESS) {
            //断线重登陆刷新本地未读红点数据到主界面
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCredit();
                }
            });
        }
    }

    @Override
    public void OnRecvCancelProgramNotice(IMProgramInfoItem showinfo) {

    }

    @Override
    public void OnRecvRetTicketNotice(IMProgramInfoItem showinfo, double leftCredit) {

    }

    @Override
    public void onShowUnreadUpdate(int unreadNum) {

    }

    @Override
    public void OnLogout(IMClientListener.LCC_ERR_TYPE errType, String errMsg) {

    }

    @Override
    public void OnKickOff(IMClientListener.LCC_ERR_TYPE errType, String errMsg) {

    }

    @Override
    public void OnRecvLackOfCreditNotice(String roomId, String message, double credit, IMClientListener.LCC_ERR_TYPE err) {

    }

    @Override
    public void OnRecvCreditNotice(String roomId, double credit) {

    }

    @Override
    public void OnRecvAnchoeInviteNotify(String logId, String anchorId, String anchorName, String anchorPhotoUrl, String message) {

    }

    @Override
    public void OnRecvScheduledInviteNotify(String inviteId, String anchorId, String anchorName, String anchorPhotoUrl, String message) {

    }

    @Override
    public void OnRecvSendBookingReplyNotice(String inviteId, IMClientListener.BookInviteReplyType replyType) {

    }

    @Override
    public void OnRecvBookingNotice(String roomId, String userId, String nickName, String photoUrl, int leftSeconds) {

    }

    @Override
    public void OnRecvLevelUpNotice(int level) {

    }

    @Override
    public void OnRecvLoveLevelUpNotice(IMLoveLeveItem lovelevelItem) {

    }

    @Override
    public void OnRecvBackpackUpdateNotice(IMPackageUpdateItem item) {

    }
}
