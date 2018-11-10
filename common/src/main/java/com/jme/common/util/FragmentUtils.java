package com.jme.common.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jme.common.ui.base.BaseFragment;

import java.util.List;

/**
 * FragmentUtils
 * Created by zhangzhongqiang on 2016/3/28.
 */
public class FragmentUtils {
    static final String ARG_IS_ROOT = "fragment_arg_is_root";
    static final String FRAGMENTATION_STATE_SAVE_IS_HIDDEN = "fragment_state_save_status";
    static final String FRAGMENTATION_ARG_CONTAINER = "fragment_arg_container";
    private FragmentUtils() {

    }

    public static Fragment replaceFragment(FragmentManager fragmentManager, int container,
            Class<? extends Fragment> newFragment, Bundle args) {
        return replaceFragment(fragmentManager, container, newFragment, args, false);
    }

    public static Fragment replaceFragment(FragmentManager fragmentManager, int container,
            Fragment newFragment) {
        return replaceFragment(fragmentManager, container, newFragment, false);
    }

    public static Fragment replaceFragment(FragmentManager fragmentManager, int container,
            Class<? extends Fragment> newFragment, Bundle args, boolean addToBackStack) {

        Fragment fragment = null;

        // 构造新的Fragment
        try {
            fragment = newFragment.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            // 设置参数
            if (args != null && !args.isEmpty()) {
                final Bundle bundle = fragment.getArguments();
                if (bundle != null) {
                    bundle.putAll(args);
                } else {
                    fragment.setArguments(args);
                }
            }
            // 替换
            return replaceFragment(fragmentManager, container, fragment, addToBackStack);
        } else {
            return null;
        }
    }

    public static Fragment replaceFragment(FragmentManager fragmentManager, int container,
            Fragment newFragment, boolean addToBackStack) {
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        final String tag = newFragment.getClass().getSimpleName();

        if (newFragment != null) {
            transaction.replace(container, newFragment, tag);
        }

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
        return newFragment;
    }

    public static Fragment switchFragment(FragmentManager fragmentManager, int container,
            Fragment currentFragment, Class<? extends Fragment> newFragment, Bundle args) {
        return switchFragment(fragmentManager, container, currentFragment, newFragment, args, false);
    }

    /**
     *
     * @param fragmentManager
     * @param container
     * @param currentFragment
     * @param newFragment
     * @param args 新Fragment的参数
     * @param addToBackStack 这个操作是否加入栈中，如果要实现类似返回效果，则需要。
     * @return 新显示的Fragment
     */
    public static Fragment switchFragment(FragmentManager fragmentManager, int container,
            Fragment currentFragment, Class<? extends Fragment> newFragment, Bundle args,
            boolean addToBackStack) {

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        final String tag = newFragment.getSimpleName();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        // 如果在栈中找到相应的Fragment，则显示，否则重新生成一个
        if (fragment != null) {
            if (fragment != currentFragment) {
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.show(fragment);
                if (addToBackStack) {
                    transaction.addToBackStack(null);
                }
                transaction.commitAllowingStateLoss();
            } else {
                if(fragment.getArguments()!=null)
                fragment.getArguments().putAll(args);
            }

            return fragment;
        } else {
            try {
                fragment = newFragment.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // 为新的Fragment添加参数
        if (fragment != null) {
            if (args != null && !args.isEmpty()) {
                final Bundle bundle = fragment.getArguments();
                if (bundle != null) {
                    bundle.putAll(args);
                } else {
                    fragment.setArguments(args);
                }
            }
        }

        // 显示新的Fragment
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        transaction.add(container, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();

        return fragment;
    }

    public static void bindContainerId(int containerId, BaseFragment to) {
        Bundle args = to.getArguments();
        if (args == null) {
            args = new Bundle();
            to.setArguments(args);
        }
        args.putInt(FRAGMENTATION_ARG_CONTAINER, containerId);
    }


    public static void loadFragment(FragmentManager fm, int containerId,int showPosition,BaseFragment... toFragments){
        FragmentTransaction ft = fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        for(int i = 0; i < toFragments.length; i++) {
            BaseFragment fragment = toFragments[i];
            bindContainerId(containerId, fragment);
            String fragmentName = fragment.getClass().getName();

            ft.add(containerId,fragment,fragmentName);
            if(i != showPosition){
                ft.hide(fragment);
            }

//            Bundle bundle = fragment.getArguments();
//            bundle.putBoolean(ARG_IS_ROOT,true);
        }

        ft.commitAllowingStateLoss();
    }

    public static <T extends BaseFragment> T findChildFragment(FragmentManager fm,Class<T> fragmentClass,boolean isChild){
        Fragment fragment = null;
        if(isChild){
            // 如果是 查找子Fragment,则有可能是在FragmentPagerAdapter/FragmentStatePagerAdapter中,这种情况下,
            // 它们的Tag是以android:switcher开头,所以这里我们使用下面的方式
            List<Fragment> childFragmentList = fm.getFragments();
            if(childFragmentList == null) {
                return null;
            }
            for(int i = childFragmentList.size() - 1; i >=0; i--){
                Fragment childFragment = childFragmentList.get(i);
                if(childFragment instanceof BaseFragment && childFragment.getClass().getName().equals(fragmentClass.getName())){
                    fragment = childFragment;
                    break;
                }
            }
        } else {
            fragment = fm.findFragmentByTag(fragmentClass.getName());
        }

        return (T)fragment;
    }

    public static void showHideFragment( FragmentManager fm,BaseFragment showFragment, BaseFragment hideFragment){
        if(showFragment == null || hideFragment == null || showFragment == hideFragment){
            return ;
        }
        fm.beginTransaction().hide(hideFragment).show(showFragment).commitAllowingStateLoss();
    }
}
