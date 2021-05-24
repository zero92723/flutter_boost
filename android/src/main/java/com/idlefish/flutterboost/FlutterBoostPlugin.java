package com.idlefish.flutterboost;

import android.util.Log;
import android.util.SparseArray;

import com.idlefish.flutterboost.containers.FlutterContainerManager;
import com.idlefish.flutterboost.containers.FlutterViewContainer;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;

public class FlutterBoostPlugin implements FlutterPlugin, Messages.NativeRouterApi, ActivityAware {
    private static final String TAG = FlutterBoostPlugin.class.getSimpleName();
    private static final boolean DEBUG = true; // Set to true to enable debug logs.

    private Messages.FlutterRouterApi channel;
    private FlutterBoostDelegate delegate;
    private Messages.StackInfo dartStack;
    private SparseArray<String> pageNames;
    private int requestCode = 1000;

    public void setDelegate(FlutterBoostDelegate delegate) {
        this.delegate = delegate;
    }

    public FlutterBoostDelegate getDelegate() {
        return delegate ;
    }

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        Messages.NativeRouterApi.setup(binding.getBinaryMessenger(), this);
        channel = new Messages.FlutterRouterApi(binding.getBinaryMessenger());
        pageNames = new SparseArray<String>();
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
        channel = null;
    }

    @Override
    public void pushNativeRoute(Messages.CommonParams params) {
        if (DEBUG) Log.d(TAG, "#pushNativeRoute: name=" + params.getPageName());
        if (delegate != null) {
            requestCode++;
            if(pageNames!=null){
                pageNames.put(requestCode, params.getPageName());
            }
            delegate.pushNativeRoute(params.getPageName(), (Map<String, Object>) (Object)params.getArguments(), requestCode);
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* set delegate!");
        }
    }

    @Override
    public void pushFlutterRoute(Messages.CommonParams params) {
        if (DEBUG) Log.d(TAG, "#pushFlutterRoute: name=" + params.getPageName());
        if (delegate != null) {
            delegate.pushFlutterRoute(params.getPageName(), params.getUniqueId(), (Map<String, Object>) (Object)params.getArguments());
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* set delegate!");
        }
    }

    @Override
    public void popRoute(Messages.CommonParams params) {
        if (DEBUG) Log.d(TAG, "#popRoute: name=" + params.getPageName());
        String uniqueId = params.getUniqueId();
        if (uniqueId != null) {
            FlutterViewContainer container = FlutterContainerManager.instance().findContainerById(uniqueId);
            if (container != null) {
                container.finishContainer((Map<String, Object>) (Object)params.getArguments());
            }
        } else {
            throw new RuntimeException("Oops!! The unique id is null!");
        }
    }

    @Override
    public Messages.StackInfo getStackFromHost() {
        if (dartStack == null) {
            return Messages.StackInfo.fromMap(new HashMap());
        }
        if (DEBUG) Log.d(TAG, "#getStackFromHost: " + dartStack);
        return dartStack;
    }

    @Override
    public void saveStackToHost(Messages.StackInfo arg) {
        dartStack = arg;
        if (DEBUG) Log.d(TAG, "#saveStackToHost: " + dartStack);
    }

    public interface Reply<T> {
        void reply(T reply);
    }

    public void pushRoute(String uniqueId, String pageName, Map<String, Object> arguments,
                          final Reply<Void> callback) {
        if (channel != null) {
            Messages.CommonParams params = new Messages.CommonParams();
            params.setUniqueId(uniqueId);
            params.setPageName(pageName);
            params.setArguments((Map<Object, Object>)(Object) arguments);
            channel.pushRoute(params, reply -> {
                if (callback != null) {
                    callback.reply(null);
                }
            });
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* have attached to engine yet!");
        }
    }

    public void popRoute(String uniqueId,final Reply<Void> callback) {
        if (channel != null) {
            Messages.CommonParams params = new Messages.CommonParams();
            params.setUniqueId(uniqueId);
            channel.popRoute(params,reply -> {
                if (callback != null) {
                    callback.reply(null);
                }
            });
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* have attached to engine yet!");
        }
    }

    public void removeRoute(String uniqueId, final Reply<Void> callback) {
        if (channel != null) {
            Messages.CommonParams params = new Messages.CommonParams();
            params.setUniqueId(uniqueId);
            channel.removeRoute(params,reply -> {
                if (callback != null) {
                    callback.reply(null);
                }
            });
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* have attached to engine yet!");
        }
    }

    public void onForeground() {
        if (channel != null) {
            Messages.CommonParams params = new Messages.CommonParams();
            channel.onForeground(params, reply -> {});
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* have attached to engine yet!");
        }
        if (DEBUG) Log.d(TAG, "## onForeground: " + channel);
    }

    public void onBackground() {
        if (channel != null) {
            Messages.CommonParams params = new Messages.CommonParams();
            channel.onBackground(params, reply -> {});
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* have attached to engine yet!");
        }
        if (DEBUG) Log.d(TAG, "## onBackground: " + channel);
    }

    public void onContainerShow(String uniqueId) {
        if (channel != null) {
            Messages.CommonParams params = new Messages.CommonParams();
            params.setUniqueId(uniqueId);
            channel.onContainerShow(params, reply -> {});
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* have attached to engine yet!");
        }
        if (DEBUG) Log.d(TAG, "## onContainerShow: " + channel);
    }

    public void onContainerHide(String uniqueId) {
        if (channel != null) {
            Messages.CommonParams params = new Messages.CommonParams();
            params.setUniqueId(uniqueId);
            channel.onContainerHide(params, reply -> {});
        } else {
            throw new RuntimeException("FlutterBoostPlugin might *NOT* have attached to engine yet!");
        }
        if (DEBUG) Log.d(TAG, "## onContainerHide: " + channel);
    }

    public void onContainerCreated(FlutterViewContainer container) {
        if (DEBUG) Log.d(TAG, "#onContainerCreated: " + container.getUniqueId());
    }

    public void onContainerAppeared(FlutterViewContainer container) {
        String uniqueId = container.getUniqueId();
        FlutterContainerManager.instance().reorderContainer(uniqueId, container);
        pushRoute(uniqueId, container.getUrl(), container.getUrlParams(), null);

        onContainerShow(uniqueId);
        if (DEBUG) Log.d(TAG, "#onContainerAppeared: " + uniqueId + ", " + FlutterContainerManager.instance().getContainers());
    }

    public void onContainerDisappeared(FlutterViewContainer container) {
        String uniqueId = container.getUniqueId();
        onContainerHide(uniqueId);
        if (DEBUG) Log.d(TAG, "#onContainerDisappeared: " + uniqueId + ", " +  FlutterContainerManager.instance().getContainers());
    }

    public void onContainerDestroyed(FlutterViewContainer container) {
        String uniqueId = container.getUniqueId();
        removeRoute(uniqueId, null);
        FlutterContainerManager.instance().removeContainer(uniqueId);
        if (DEBUG) Log.d(TAG, "#onContainerDestroyed: " + uniqueId + ", " +  FlutterContainerManager.instance().getContainers());
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
        activityPluginBinding.addActivityResultListener((requestCode, resultCode, intent) -> {
            if (channel != null) {
                Messages.CommonParams params = new Messages.CommonParams();
                String pageName = pageNames.get(requestCode);
                pageNames.remove(requestCode);
                if (null != pageName){
                    params.setPageName(pageName);
                    if(intent!=null){
                        Map<Object, Object> result = FlutterBoostUtils.bundleToMap(intent.getExtras());
                        params.setArguments(result);
                    }
                    channel.onNativeResult(params, reply -> {});
                }
            } else {
                throw new RuntimeException("FlutterBoostPlugin might *NOT* have attached to engine yet!");
            }
            return true;
        });
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {

    }

    @Override
    public void onDetachedFromActivity() {

    }
}
