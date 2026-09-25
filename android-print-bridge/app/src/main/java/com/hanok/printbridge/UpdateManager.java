package com.hanok.printbridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.*;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class UpdateManager {
    private UpdateManager(){}

    public static final String META_URL="https://pycuztuyrhdkqlepbinh.supabase.co/functions/v1/hanok-wagga-update?mode=meta";
    private static final String PREF_PENDING="pending_update_after_permission";
    private static final String PREF_LAST_CHECK="last_app_update_check";
    private static final long AUTO_CHECK_INTERVAL_MS=6L*60L*60L*1000L;

    public static void autoCheck(Activity activity){
        long now=System.currentTimeMillis();
        long last=activity.getSharedPreferences(BridgeConfig.PREFS,Context.MODE_PRIVATE).getLong(PREF_LAST_CHECK,0L);
        if(now-last<AUTO_CHECK_INTERVAL_MS)return;
        activity.getSharedPreferences(BridgeConfig.PREFS,Context.MODE_PRIVATE).edit().putLong(PREF_LAST_CHECK,now).apply();
        check(activity,true);
    }

    public static void check(Activity activity,boolean quiet){
        new Thread(()->{
            HttpURLConnection c=null;
            try{
                c=(HttpURLConnection)new URL(META_URL).openConnection();
                c.setConnectTimeout(7000);
                c.setReadTimeout(10000);
                c.setUseCaches(false);
                c.setRequestProperty("accept","application/json");
                int code=c.getResponseCode();
                if(code<200||code>=300)throw new Exception("Update server HTTP "+code);
                String body=read(c.getInputStream());
                JSONObject j=new JSONObject(body);
                long latest=j.optLong("version_code",0L);
                long current=currentVersionCode(activity);
                if(latest>current){
                    String version=j.optString("version_name",String.valueOf(latest));
                    String notes=j.optString("notes","A new Hanok Wagga Print Bridge version is available.");
                    String apkUrl=j.optString("apk_url","");
                    String sha256=j.optString("sha256","");
                    activity.runOnUiThread(()->showUpdate(activity,version,notes,apkUrl,sha256));
                }else if(!quiet){
                    activity.runOnUiThread(()->Toast.makeText(activity,"You already have the latest version.",Toast.LENGTH_LONG).show());
                }
            }catch(Throwable e){
                if(!quiet)activity.runOnUiThread(()->Toast.makeText(activity,"Update check failed: "+shortMsg(e),Toast.LENGTH_LONG).show());
            }finally{if(c!=null)c.disconnect();}
        },"hanok-update-check").start();
    }

    private static void showUpdate(Activity activity,String version,String notes,String apkUrl,String sha256){
        if(activity.isFinishing()||apkUrl==null||apkUrl.isEmpty())return;
        new AlertDialog.Builder(activity)
            .setTitle("Update available · v"+version)
            .setMessage(notes+"\n\nThe update will download automatically. Android will ask you to confirm installation.")
            .setNegativeButton("Later",null)
            .setPositiveButton("Update now",(d,w)->beginUpdate(activity,apkUrl,version,sha256))
            .show();
    }

    private static void beginUpdate(Activity activity,String apkUrl,String version,String sha256){
        if(Build.VERSION.SDK_INT>=26&&!activity.getPackageManager().canRequestPackageInstalls()){
            activity.getSharedPreferences(BridgeConfig.PREFS,Context.MODE_PRIVATE).edit()
                .putBoolean(PREF_PENDING,true)
                .putString("pending_update_url",apkUrl)
                .putString("pending_update_version",version)
                .putString("pending_update_sha256",sha256)
                .apply();
            try{
                Intent i=new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,Uri.parse("package:"+activity.getPackageName()));
                activity.startActivity(i);
                Toast.makeText(activity,"Allow installs for Hanok Wagga Print Bridge, then return to the app.",Toast.LENGTH_LONG).show();
            }catch(Throwable e){
                Toast.makeText(activity,"Please allow this app to install updates in Android Settings.",Toast.LENGTH_LONG).show();
            }
            return;
        }
        downloadAndInstall(activity,apkUrl,version);
    }

    public static void resumePendingInstall(Activity activity){
        SharedPreferences p=activity.getSharedPreferences(BridgeConfig.PREFS,Context.MODE_PRIVATE);
        if(!p.getBoolean(PREF_PENDING,false))return;
        if(Build.VERSION.SDK_INT>=26&&!activity.getPackageManager().canRequestPackageInstalls())return;
        String url=p.getString("pending_update_url","");
        String version=p.getString("pending_update_version","update");
        p.edit().putBoolean(PREF_PENDING,false).apply();
        if(!url.isEmpty())downloadAndInstall(activity,url,version);
    }

    private static void downloadAndInstall(Activity activity,String apkUrl,String version){
        try{
            DownloadManager dm=(DownloadManager)activity.getSystemService(Context.DOWNLOAD_SERVICE);
            if(dm==null)throw new Exception("Download Manager unavailable");
            String file="Hanok-Wagga-Print-Bridge-v"+version+".apk";
            DownloadManager.Request r=new DownloadManager.Request(Uri.parse(apkUrl));
            r.setTitle("Hanok Wagga Print Bridge v"+version);
            r.setDescription("Downloading app update");
            r.setMimeType("application/vnd.android.package-archive");
            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,file);
            final long id=dm.enqueue(r);

            BroadcastReceiver receiver=new BroadcastReceiver(){
                @Override public void onReceive(Context context,Intent intent){
                    if(intent==null||DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())==false)return;
                    long done=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
                    if(done!=id)return;
                    try{activity.unregisterReceiver(this);}catch(Throwable ignored){}
                    Uri uri=dm.getUriForDownloadedFile(id);
                    if(uri==null){
                        Toast.makeText(activity,"Update download failed.",Toast.LENGTH_LONG).show();
                        return;
                    }
                    try{
                        Intent install=new Intent(Intent.ACTION_VIEW);
                        install.setDataAndType(uri,"application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(install);
                    }catch(Throwable e){
                        Toast.makeText(activity,"Could not open installer: "+shortMsg(e),Toast.LENGTH_LONG).show();
                    }
                }
            };
            IntentFilter filter=new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            if(Build.VERSION.SDK_INT>=33)activity.registerReceiver(receiver,filter,Context.RECEIVER_NOT_EXPORTED);
            else activity.registerReceiver(receiver,filter);
            Toast.makeText(activity,"Downloading v"+version+"…",Toast.LENGTH_LONG).show();
        }catch(Throwable e){
            Toast.makeText(activity,"Update download failed: "+shortMsg(e),Toast.LENGTH_LONG).show();
        }
    }

    private static long currentVersionCode(Context context){
        try{
            PackageInfo p=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            if(Build.VERSION.SDK_INT>=28)return p.getLongVersionCode();
            return p.versionCode;
        }catch(Throwable ignored){return 0L;}
    }

    private static String read(InputStream in)throws Exception{
        ByteArrayOutputStream b=new ByteArrayOutputStream();
        byte[] x=new byte[4096];int n;
        while((n=in.read(x))>0)b.write(x,0,n);
        return b.toString("UTF-8");
    }

    private static String shortMsg(Throwable e){
        String s=e.getMessage();
        if(s==null||s.trim().isEmpty())s=e.getClass().getSimpleName();
        return s.length()>120?s.substring(0,120):s;
    }
}
