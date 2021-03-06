package com.lxw.dailynews.app.presenter;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.BeforeThemeContentBean;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsThemeBean;
import com.lxw.dailynews.app.bean.ThemeContentBean;
import com.lxw.dailynews.app.model.model.IMainModel;
import com.lxw.dailynews.app.model.model.ISplashModel;
import com.lxw.dailynews.app.model.modelImp.MainModel;
import com.lxw.dailynews.app.model.modelImp.SplashModel;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.framework.application.BaseApplication;
import com.lxw.dailynews.framework.base.BaseMvpPresenter;
import com.lxw.dailynews.framework.http.HttpListener;
import com.lxw.dailynews.framework.util.TimeUtil;

import java.util.List;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public class MainPresenter extends BaseMvpPresenter<IMainView> {
    private IMainModel mainModel;
    private ISplashModel splashModel;
    public static boolean frag_offline = false;//离线阅读的标志

    public MainPresenter() {
        mainModel = new MainModel();
        splashModel = new SplashModel();
    }

    //获取最新热闻
    public void getLatestNews() {
        if (isNetworkAvailable()) {
            frag_offline = false;
            splashModel.getLatestNews(new HttpListener<LatestNewsBean>() {
                @Override
                public void onSuccess(LatestNewsBean response) {
                    if (response != null) {
                        getView().setLatestNewsBean(response);
                    }else{
                        getView().stopRefreshAnimation();
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    getView().stopRefreshAnimation();
                    showMessage(error.getMessage());
                }
            });
        } else {
            //展示上次加载的消息
            if(splashModel.getOfflineLatestNews(null) != null){
                getView().setLatestNewsBean(splashModel.getOfflineLatestNews(null));
                frag_offline = true;
            }
            getView().stopRefreshAnimation();
        }
    }

    //获取之前某一天的热闻
    public void getBeforeNews(String beforeDate) {
        if(frag_offline){
            if(splashModel.getOfflineLatestNews(TimeUtil.getBeforeDate(beforeDate, 1)) != null){
                getView().setLatestNewsBean(splashModel.getOfflineLatestNews(TimeUtil.getBeforeDate(beforeDate, 1)));
            }
        }else if (checkNetword()) {
            mainModel.getBeforeNews(beforeDate, new HttpListener<LatestNewsBean>() {
                @Override
                public void onSuccess(LatestNewsBean response) {
                    if (response != null) {
                        getView().setLatestNewsBean(response);
                    }else {
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    showMessage(error.getMessage());
                }
            });
        }
    }

    //获取热闻主题
    public void getNewsThemes(){
        if (checkNetword()) {
            splashModel.getNewsThemes(new HttpListener<NewsThemeBean>() {
                @Override
                public void onSuccess(NewsThemeBean response) {
                    if (response != null) {
                        getView().setNewsThemeBean(response);
                    }else {
                        getView().stopRefreshAnimation();
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    showMessage(error.getMessage());
                    getView().stopRefreshAnimation();
                    showMessage(error.getMessage());
                }
            });
        }else{
            if(splashModel.getOfflineNewsThemes() != null){
                getView().setNewsThemeBean(splashModel.getOfflineNewsThemes());
            }
            getView().stopRefreshAnimation();
        }
    }

    //获取主题新闻内容
    public void getThemeContent(String themeId) {
        if (checkNetword()) {
            mainModel.getThemeContent(themeId, new HttpListener<ThemeContentBean>() {
                @Override
                public void onSuccess(ThemeContentBean response) {
                    if (response != null) {
                        getView().setThemeContentBean(response);
                    } else {
                        getView().stopRefreshAnimation();
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    getView().stopRefreshAnimation();
                    showMessage(error.getMessage());
                }
            });
        }else{
            showMessage(BaseApplication.appContext.getString(R.string.error_network_failure));
            getView().stopRefreshAnimation();
        }
    }

    //获取主题之前的新闻内容
    public void getBeforeThemeContent(String themeId, String newsId) {
        if (checkNetword()) {
            mainModel.getBeforeThemeContent(themeId, newsId, new HttpListener<BeforeThemeContentBean>() {
                @Override
                public void onSuccess(BeforeThemeContentBean response) {
                    if (response != null) {
                        getView().setBeforeThemeContentBean(response);
                    } else {
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    showMessage(error.getMessage());
                }
            });
        }
    }
}
